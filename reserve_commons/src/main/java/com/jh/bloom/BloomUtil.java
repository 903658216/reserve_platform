package com.jh.bloom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;


/**
 * 布隆工具类
 */
@Component
public class BloomUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //初始化布隆过滤（防止击穿redis缓存，判断，一个数据可能存在或者一定不存在）
    String initScript="return redis.call('bf.reserve',KEYS[1],ARGV[1],ARGV[2])";

    String addScript="return redis.call('bf.add',KEYS[1],ARGV[1])";

    String existsScript="return redis.call('bf.exists',KEYS[1],ARGV[1])";

    /**
     * 初始化布隆过滤器
     * @param key
     * @param error
     * @param initSize
     */
    public void  initBloom(String key,String error,String initSize){
        //执行初始化bloom过滤器的脚本
        redisTemplate.execute(new DefaultRedisScript(initScript,String.class),
                Collections.singletonList(key),
                error,initSize);
    }


    /**
     * 添加元素
     * @param key
     * @param value
     * @return
     */
    public boolean addBloom(String key,String value){
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript(addScript,Long.class),
                Collections.singletonList(key),
                value);

        return result == 1;
    }


    /**
     * 判断该数据是否存在
     * @param key
     * @param value
     * @return
     */
    public  boolean isExists(String key,String value){
        Long result = (Long) redisTemplate.execute(new DefaultRedisScript(existsScript,Long.class),
                Collections.singletonList(key),
                value);

        return result == 1;

    }

}
