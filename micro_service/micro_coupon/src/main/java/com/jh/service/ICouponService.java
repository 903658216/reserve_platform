package com.jh.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jh.entity.Coupon;
import com.jh.entity.OrderPriceParams;

import java.util.List;
import java.util.Map;

public interface ICouponService extends IService<Coupon>  {


    /**
     * 根据优惠券发布编号领取优惠券
     * @param issid
     * @return
     */
    int receiveCoupon(Integer issid);

    /**
     * 前端根据用户信息查询优惠券列表
     * @param orderPriceParams
     * @return
     */
    Map<String, List<Coupon>> getCouponByUser(OrderPriceParams orderPriceParams);
}
