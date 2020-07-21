package com.jh.feign;


import com.jh.entity.OrderPriceParams;
import com.jh.entity.OrderPriceResult;
import com.jh.entity.ResultData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("micro-order")
public interface OrderFeign {
    /**
     * 计算订单的价格展示预计订单的详情
     * @param orderPriceParams 请求订单计算的对象
     * @return ResultData<OrderPriceResult>
     */
    @RequestMapping("/order/getOrderPriceFeign")
    ResultData<OrderPriceResult> getOrderPriceFeign(@RequestBody OrderPriceParams orderPriceParams);
}
