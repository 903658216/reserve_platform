package com.jh.lock;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
public class LockUtil {


    @Autowired
    private StringRedisTemplate redisTemplate;

    //加锁的lua脚本
    private String lockLua = "--锁的名称\n" +
            "local lockName=KEYS[1]\n" +
            "--锁的value\n" +
            "local lockValue=ARGV[1]\n" +
            "--过期时间 秒\n" +
            "local timeout=tonumber(ARGV[2])\n" +
            "--尝试进行加锁\n" +
            "local flag=redis.call('setnx', lockName, lockValue)\n" +
            "--判断是否获得锁\n" +
            "if flag==1 then\n" +
            "--获得分布式锁，设置过期时间\n" +
            "redis.call('expire', lockName, timeout)\n" +
            "end\n" +
            "--返回标识\n" +
            "return flag ";

    //解锁的lua脚本
    private String unLockLua = "--锁的名称\n" +
            "local lockName=KEYS[1]\n" +
            "--锁的value\n" +
            "local lockValue=ARGV[1]\n" +
            "--判断锁是否存在，以及锁的内容是否为自己加的\n" +
            "local value=redis.call('get', lockName)\n" +
            "--判断是否相同\n" +
            "if value == lockValue then\n" +
            "     redis.call('del', lockName)\n" +
            "     return 1\n" +
            "end\n" +
            "return 0";

    private ThreadLocal<String> tokens = new ThreadLocal<>();

    /**
     * 加锁
     * @param lockName
     */
    public void lock(String lockName){
        lock(lockName, 30);
    }

    public void lock(String lockName, Integer timeout){

        String token = UUID.randomUUID().toString();
        //设置给threadLocal
        tokens.set(token);

        //分布式锁 - 加锁
        Long flag = (Long) redisTemplate.execute(new DefaultRedisScript(lockLua, Long.class),
                Collections.singletonList(lockName),
                token, timeout + ""
        );

        System.out.println("获得锁的结果：" + flag);

        //设置锁的自旋
        if (flag == 0) {
            //未获得锁
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock(lockName, timeout);
        }
    }

    /**
     * 解锁
     * @return
     */
    public boolean unlock(String lockName){

        //获得ThreadLocal
        String token = tokens.get();

        //解锁
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript(unLockLua, Long.class),
                Collections.singletonList(lockName),
                token);

        System.out.println("删除锁的结果：" + result);

        return result == 1;
    }

}
