package com.jh.event.constant;

/**
 * 交换机和路由键常量设置接口
 */
public interface EventConstant {

    /**
     * 交换机的名称
     */
    String EXCHANGE_NAME ="event-exchange";

    /**
     * 酒店新增事件的路由键
     */
    String EVENT_HOTEL_INSERT = "hotel_insert";

    /**
     * 酒店客房类型新增事件的路由键
     */
    String EVENT_HOTEL_ROOM_INSERT ="hotel_room_insert";

    /**
     * 酒店客房价格的修改事件
     */
    String EVENT_HOTEL_ROOM_PRICE_UPDATE ="hotel_room_price_update";

    /**
     * 酒店点击事件
     */
    String EVENT_HOTEL_CLICK="hotel_click";
    /**
     * 订单服务中，酒店客房修改事件
     */
    String EVENT_HOTEL_ROOM_UPDATE="hotel_room_update";

}
