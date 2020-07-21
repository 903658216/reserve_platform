package com.jh.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jh.entity.Coupon;
import com.jh.entity.CouponReceive;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CouponReceiveMapper extends BaseMapper<CouponReceive> {

    /**
     * 前端根据用户信息查询优惠券列表
     * @param uid
     * @return
     */
    List<Coupon> getCouponByUser(@Param("uid") Integer uid);


}
