package com.jh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jh.dao.CouponMapper;
import com.jh.dao.CouponReceiveMapper;
import com.jh.entity.*;
import com.jh.feign.OrderFeign;
import com.jh.login.UserUtil;
import com.jh.service.ICouponService;
import com.jh.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {


    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private  CouponIssueServiceImpl couponIssueService;


    @Autowired
    private CouponReceiveMapper couponReceiveMapper;

    @Autowired
    private OrderFeign orderFeign;


    /**
     * 根据优惠券发布编号领取优惠券
     * @param issid
     * @return
     */
    @Override
    public int receiveCoupon(Integer issid) {

        //获得当前的优惠券发布对象
        CouponIssue couponIssue = couponIssueService.getById(issid);

        //获得当前登录的对象
        User user = UserUtil.getUser();

        //判断是否已经领取过
        Integer count = couponReceiveMapper.selectCount(new QueryWrapper<CouponReceive>().eq("cid", couponIssue.getCid()).eq("uid", user.getId()));

        //还没领取过
        if (count ==0 ){

            CouponReceive couponReceive = new CouponReceive()
                    .setCid(couponIssue.getCid())
                    .setUid(user.getId())
                    .setGetTime(new Date())
                    .setGetType(couponIssue.getMethod())
                    .setTimeout(couponIssue.getType() == 0 ? couponIssue.getEndTime() : DateUtil.getNextDate(couponIssue.getDays()));
            return  couponReceiveMapper.insert(couponReceive);
        }

        return 0;
    }


    /**
     * 前端根据用户信息查询优惠券列表（在用户订单编辑页面的可用的优惠券列表)
     * @param orderPriceParams
     * @return
     */
    @Override
    public Map<String, List<Coupon>> getCouponByUser(OrderPriceParams orderPriceParams) {

        User user = UserUtil.getUser();
        //查询当前用户信息所有的优惠券
        List<Coupon> couponList = couponReceiveMapper.getCouponByUser(user.getId());

        //存放返回结果
        Map<String ,List<Coupon>> resultMap = new HashMap<>();

        //存放可用的优惠券列表
        List<Coupon> canUse =  new ArrayList<>();
        //存放不可用的优惠券列表
        List<Coupon> unCanUse =  new ArrayList<>();

        for (Coupon coupon : couponList) {
            //判断优惠券是否可用
            boolean flag = coupon.getILimit().hasLimit(orderPriceParams);
            if (flag){
                //进行金额的验证
                //获取订单的金额

                System.out.println("=====》"+ orderPriceParams);
                //TODO 在订单编辑页面中查询可用优惠券，feign调用出错
                OrderPriceResult orderPriceResult = orderFeign.getOrderPriceFeign(orderPriceParams).getData();
                //订单的总金额
                double allPrice = orderPriceResult.getAllPrice();

                //判断额度是否满足优惠的最低额度
                if (coupon.getIRule().hasCouponPrice(allPrice)){
                    //当前优惠券可用于本次订单
                    canUse.add(coupon);
                    continue;
                }
            }

            //当前优惠券不可用于本次订单
            unCanUse.add(coupon);
        }


        resultMap.put("canUse",canUse);
        resultMap.put("unCanUse",unCanUse);

        return resultMap;
    }
}
