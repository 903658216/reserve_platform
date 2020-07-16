package com.jh.controller;


import com.jh.entity.OrderPriceParams;
import com.jh.entity.OrderPriceResult;
import com.jh.entity.Orders;
import com.jh.entity.ResultData;
import com.jh.login.IsLogin;
import com.jh.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    @Autowired
    private IOrderService iOrderService;

    /**
     *
     * @return
     */
    @RequestMapping("/insertOrder")
    @IsLogin(mustLogin = true)
    public ResultData<String> insertOrder(OrderPriceParams orderPriceParams, Orders orders){

        System.out.println("哈哈哈哈。你成功的配置了订单服务"+orderPriceParams+","+orders);

        //TODO 需要跟数据进行交互，查询当前房间是否能够满足该订单
        String oid = iOrderService.insertOrder(orderPriceParams,orders);

        if (oid != null){

            System.out.println("生成的订单编号"+oid);
            return new ResultData<String>().setData(oid);
        }else {
            System.out.println("订单生成失败");
            return new ResultData<String>().setCode(ResultData.Code.ERROR).setMsg("房间不足，下单失败!");

        }
    }

    /**
     * 计算订单的价格展示预计订单的详情
     * @param orderPriceParams 请求订单计算的对象
     * @return ResultData<OrderPriceResult>
     */
    @RequestMapping("/getOrderPrice")
    public ResultData<OrderPriceResult> getOrderPrice(OrderPriceParams orderPriceParams){
        System.out.println("请求计算价格");
        OrderPriceResult orderPriceResult = iOrderService.getOrderPrice(orderPriceParams);

        return new ResultData<OrderPriceResult>().setData(orderPriceResult);
    }

}
