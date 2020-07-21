package com.jh.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jh.entity.Coupon;
import com.jh.entity.CouponIssue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CouponIssueMapper extends BaseMapper<CouponIssue> {


    /**
     * 根据优惠券发布的方式和用户的编号来查询优惠券中心
     * @param method
     * @param uid
     * @return
     */
    List<Coupon> getCouponIssueByType(@Param("method") int method, @Param("uid") int uid);

    /**
     * 根据优惠券的领取类型，和开始抢券的时间来查询优惠券
     * @param method
     * @param beginTime
     * @return
     */
    List<Coupon> getCouponIssueByTime(@Param("method") int method,@Param("beginTime") String beginTime);
}
