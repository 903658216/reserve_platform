package com.jh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jh.entity.Coupon;
import com.jh.entity.CouponIssue;

import java.util.List;

public interface ICouponIssueService  extends IService<CouponIssue> {

    /**
     * 前端查询优惠券列表
     * @return
     */
    List<Coupon> getCouponCore();


    /**
     * 根据抢券的开始时间的毫秒值，查询该时间段的优惠券列表
     * @param time
     * @return
     */
    List<Coupon> getCouponListByTime(long time);
}
