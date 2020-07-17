package com.jh.micro_hotel;

import com.jh.bloom.BloomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 初始化springboot项目
 */
@Component
public class SpringBootInit implements CommandLineRunner {

    @Autowired
    private BloomUtil bloomUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {

        if (!stringRedisTemplate.hasKey("djl")){
            //初始化布隆过滤器
            bloomUtil.initBloom("djl","0.001","10000000");
        }

    }
}
