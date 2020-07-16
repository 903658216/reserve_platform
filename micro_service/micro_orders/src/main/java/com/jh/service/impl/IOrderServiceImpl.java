package com.jh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.dao.OrdersMapper;
import com.jh.entity.*;
import com.jh.event.constant.EventConstant;
import com.jh.event.util.EventUtil;
import com.jh.feign.HotelFeign;
import com.jh.lock.LockUtil;
import com.jh.login.UserUtil;
import com.jh.service.IOrderPriceService;
import com.jh.service.IOrderService;
import com.jh.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class IOrderServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private IOrderPriceService iOrderPriceService;

    @Autowired
    private HotelFeign hotelFeign;

    @Autowired
    private LockUtil lockUtil;

    @Autowired
    private EventUtil eventUtil;





    /**
     * 计算订单的价格展示预计订单的详情
     * @param orderPriceParams 请求订单计算的对象
     * @return OrderPriceResult
     */
    @Override
    public OrderPriceResult getOrderPrice(OrderPriceParams orderPriceParams) {

        //获得房间的信息的价格列表
        List<RoomPrice> roomPrices = hotelFeign.selectRoomPriceListByRid(orderPriceParams.getRid()).getData();

        //设置订单总价格
        BigDecimal allPrice = BigDecimal.valueOf(0);
        //定义一个list存放订单展示详情
        List<List<String>> priceDetail = new ArrayList<>();

        //定义日期时间格式，便于日期的比较
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

        for (RoomPrice roomPrice : roomPrices) {
            //筛选满足用户预订日期的客房价格信息
            if (roomPrice.getDate().getTime()>= orderPriceParams.getBeginTime().getTime() &&
                    roomPrice.getDate().getTime()<orderPriceParams.getEndTime().getTime()
            ){
                //获取客房价格
                BigDecimal price = roomPrice.getPrice();
                allPrice = allPrice.add(price);

                //设置订单详情
                List<String> list = new ArrayList<>();
                list.add(sdf.format(roomPrice.getDate()));
                list.add(orderPriceParams.getRNumber() +" x "+roomPrice.getPrice().doubleValue());
                priceDetail.add(list);
            }
        }

        allPrice = allPrice.multiply(BigDecimal.valueOf(orderPriceParams.getRNumber()));

        OrderPriceResult orderPriceResult = new OrderPriceResult()
                .setAllPrice(allPrice.doubleValue())
                .setPriceDetails(priceDetail);

        return orderPriceResult;
    }

    /**
     * 添加订单
     * @param orderPriceParams
     * @param orders
     * @return
     */
    @Override
    @Transactional
    public String insertOrder(OrderPriceParams orderPriceParams, Orders orders) {

        //TODO 加上redis锁,该锁的生命时长为30分钟
        lockUtil.lock("orders_"+orderPriceParams.getRid());


        try {
            //获得当前用户的信息
            User user = UserUtil.getUser();

            //进行订单的价格计算
            OrderPriceResult orderPrice = getOrderPrice(orderPriceParams);

            orders.setOid(UUID.randomUUID().toString().replace("-",""))
                    .setAllPrice(orderPrice.getAllPrice())
                    .setNumber(orderPriceParams.getRNumber())
                    .setBeginTime(orderPriceParams.getBeginTime())
                    .setEndTime(orderPriceParams.getEndTime())
                    .setDays(DateUtil.getDates(orderPriceParams.getBeginTime(),orderPriceParams.getEndTime()))
                    .setUid(user.getId());


            //将订单详情存放到数据库中
            List<OrderPriceDetails> orderPriceDetailsList = new ArrayList<>();

//            ResultData<Room> roomResultData = hotelFeign.selectHotelRoomByRId(orders.getRid());
//            List<RoomPrice> roomPriceList = roomResultData.getData().getRoomPriceList();

            //获得房间的信息的价格列表
            List<RoomPrice> roomPrices = hotelFeign.selectRoomPriceListByRid(orderPriceParams.getRid()).getData();
            for (RoomPrice price : roomPrices) {
                if (price.getDate().getTime()>= orderPriceParams.getBeginTime().getTime()&& price.getDate().getTime()<orderPriceParams.getEndTime().getTime()){

                    //判断本次订单中，该房型的房间数量是否充足
                    if (price.getHasNumber() - price.getNumber() < orderPriceParams.getRNumber()){
                        //这一天的房间已经预订满了，返回null
                        return null;
                    }

                    //订单中，其中的某一条详情
                    OrderPriceDetails orderPriceDetails = new OrderPriceDetails()
                            .setOid(orders.getOid())
                            .setPrice(price.getPrice())
                            .setTime(price.getDate());
                    //将订单详情添加到订单详情列表中
                    orderPriceDetailsList.add(orderPriceDetails);
                }
            }

            System.out.println("向数据库插入的数据为"+orders);
            ordersMapper.insert(orders);

            //将订单详情列表插入到数据库中
            iOrderPriceService.saveBatch(orderPriceDetailsList);
            //TODO 发布添加订单事件
            //修改数据库中该客房的剩余数量 -- 酒店微服务
            //修改ElasticSearch中房间的剩余数量--搜索服务
            eventUtil.publishEvent(EventConstant.EVENT_HOTEL_ROOM_UPDATE,orderPriceParams);

        } finally {
            //需要确保解锁
            lockUtil.unlock("orders_"+orderPriceParams.getRid());

        }


        return orders.getOid();
    }

    /**
     * 根据支付宝返回的支付结果修改订单信息
     * @param orderId
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateOrderStatus(String orderId,Integer status) {
        Orders orders = ordersMapper.selectById(orderId);
        orders.setStatus(status);

        return ordersMapper.updateById(orders);
    }
}
