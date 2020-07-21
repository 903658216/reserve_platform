package com.jh.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.dao.CouponIssueMapper;
import com.jh.dao.CouponMapper;
import com.jh.dao.CouponReceiveMapper;
import com.jh.entity.Coupon;
import com.jh.entity.CouponIssue;
import com.jh.entity.User;
import com.jh.feign.OrderFeign;
import com.jh.login.UserUtil;
import com.jh.service.ICouponIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class CouponIssueServiceImpl extends ServiceImpl<CouponIssueMapper, CouponIssue> implements ICouponIssueService {

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private  CouponIssueMapper couponIssueMapper;


    @Autowired
    private CouponReceiveMapper couponReceiveMapper;

    @Autowired
    private OrderFeign orderFeign;

    @Override
    public List<Coupon> getCouponCore() {

        User user = UserUtil.getUser();
        System.out.println("===>"+user);

        //查询领券中心
       List<Coupon> couponList = couponIssueMapper.getCouponIssueByType(0, user != null ? user.getId() : -1);

        return couponList;
    }


    /**
     * 根据抢券的开始时间的毫秒值，查询该时间段的优惠券列表
     * @param time
     * @return
     */
    @Override
    public List<Coupon> getCouponListByTime(long time) {

        String beginTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
        System.out.println("查询当前抢券场次的时间"+ beginTime);
        List<Coupon> couponList = couponIssueMapper.getCouponIssueByTime(1,beginTime);
        return couponList;
    }


}
