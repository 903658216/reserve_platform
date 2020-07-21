package com.jh.controller;


import com.jh.entity.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/qiangquan")
public class QiangQuanController {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 获取当前的抢券场次（当前抢券的时间段）
     * 这里定义整点抢券，并且是偶数的整点开抢 如 0 2 4 6 8
     *
     * @return
     */
    @RequestMapping("/queryQiangQuanTimes")
    public ResultData<List<Map<String,Object>>>  queryQiangQuanTimes(){

        List<Map<String,Object>> resultMap = new ArrayList<>();


        //获取当前最早的显示场次的时间
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        for (int i = 0; i < 3; i++) {
            //每次 循环都基于当前时间生成时间戳
            calendar = Calendar.getInstance();
            //判断是否为偶数
            int first = hour %2 ==0 ? hour : hour-1;
            first = first + 2*i;
            calendar.set(Calendar.HOUR_OF_DAY ,first);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);

            Map<String,Object> map = new HashMap<>();
            map.put("showTitle",calendar.get(Calendar.HOUR_OF_DAY)+"点场");
            map.put("queryInfo",calendar.getTime().getTime());
            System.out.println("map====="+map);
            resultMap.add(map);
        }


        return new ResultData().setData(resultMap);
    }


    /**
     * 获取当前的系统时间
     * @return
     */
    @RequestMapping("/getNowTime")
    public ResultData<Long> getNowTime(){

        long nowTime = new Date().getTime();
        return new ResultData().setData(nowTime);
    }


    /**
     * 抢取优惠券的业务
     * @param cid
     * @return
     */
    @RequestMapping("/getCoupon")
    public ResultData<Integer> getCoupon(Integer cid){


        return new ResultData().setData(1);
    }











}
