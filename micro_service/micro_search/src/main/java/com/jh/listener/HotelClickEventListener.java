package com.jh.listener;

import com.jh.entity.Hotel;
import com.jh.event.EventListener;
import com.jh.event.constant.EventConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 搜索服务监听酒店点击率事件
 */
@Component
public class HotelClickEventListener  implements EventListener<Hotel> {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public String getEventType() {
        return EventConstant.EVENT_HOTEL_CLICK;
    }

    @Override
    public void eventHandler(Hotel hotel) {
        //将点击率保存在redis中  hash djl - hid:xxx
        Boolean flag = redisTemplate.opsForHash().hasKey("djl_cache",hotel.getId()+"");
        if (flag){
            //存在
            redisTemplate.opsForHash().increment("djl_cache",hotel.getId()+"",1);
        }else {
            //不存在，第一次该酒店被点击，
            redisTemplate.opsForHash().put("djl_cache",hotel.getId()+"","1");
        }


    }
}
