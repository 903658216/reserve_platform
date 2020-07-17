package com.jh.timer;

import com.jh.lock.LockUtil;
import com.jh.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 定时器，执行定时任务-- 10秒触发一次
 */
@Component
public class MyTime {


    @Autowired
    private ISearchService iSearchService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private LockUtil lockUtil;

//    @Scheduled(cron = "0/10 * * * * * ")
    @Scheduled(cron = "0/10 * * * * ? ")
    public void  myTask(){

        lockUtil.lock("djlLock");
        try {

            //从redis中读取个各酒店的点击率，将其更新到elasticSearch中去
            Set<Object> hidSet = redisTemplate.opsForHash().keys("djl_cache");
            hidSet.stream().forEach(o -> {
                //获得当前酒店的id
                Integer hid = Integer.parseInt(String.valueOf(o));
                //获得酒店对应的点击数量
                Integer djl = Integer.valueOf(""+ redisTemplate.opsForHash().get("djl_cache",hid+""));

                // TODO 将点击率更新到ES中
                iSearchService.updateDjl(hid,djl);

                //删除redis中对应的点击率
                redisTemplate.opsForHash().delete("djl_cache",hid+"");
            });
        } finally {

            lockUtil.unlock("djlLock");
        }

    }

}
