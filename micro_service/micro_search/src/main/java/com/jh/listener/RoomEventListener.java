package com.jh.listener;

import com.jh.entity.OrderPriceParams;
import com.jh.event.EventListener;
import com.jh.event.constant.EventConstant;
import com.jh.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 根据订单服务中，酒店客房数量改变的监听事件
 */
@Component
public class RoomEventListener implements EventListener<OrderPriceParams> {

    @Autowired
    private ISearchService iSearchService;

    @Override
    public String getEventType() {
        //订单，改变酒店客房数量的路由键
        return EventConstant.EVENT_HOTEL_ROOM_UPDATE;
    }

    @Override
    public void eventHandler(OrderPriceParams msg) {
        System.out.println("搜索服务监听到订单服务，修改酒店客房数量的事件"+msg);
        iSearchService.updateRoomNumber(msg.getHid(),msg.getRid(),msg.getRNumber(),msg.getBeginTime(),msg.getEndTime());


    }
}
