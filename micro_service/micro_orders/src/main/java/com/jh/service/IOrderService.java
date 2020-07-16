package com.jh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jh.entity.OrderPriceParams;
import com.jh.entity.OrderPriceResult;
import com.jh.entity.Orders;

public interface IOrderService extends IService<Orders> {

    /**
     * 计算订单的价格展示预计订单的详情
     * @param orderPriceParams 请求订单计算的对象
     * @return OrderPriceResult
     */
    OrderPriceResult getOrderPrice(OrderPriceParams orderPriceParams);

    /**
     * 生成订单
     * @param orderPriceParams
     * @param orders
     * @return
     */
    String insertOrder(OrderPriceParams orderPriceParams, Orders orders);

    /**
     * 根据支付宝返回的支付结果修改订单信息
     * @param orderId
     * @param status
     * @return
     */
    int updateOrderStatus(String orderId, Integer status);
}
