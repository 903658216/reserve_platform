package com.jh.controller;


import com.jh.entity.*;
import com.jh.login.IsLogin;
import com.jh.service.ICouponIssueService;
import com.jh.service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/coupon")
public class CouponController {


    @Autowired
    private ICouponService iCouponService;

    @Autowired
    private ICouponIssueService iCouponIssueService;
    /**
     * 新增优惠券
     * @param coupon
     * @return
     */
    @RequestMapping("/insertCoupon")
    public ResultData<Boolean> insertCoupon(@RequestBody Coupon coupon){

        boolean flag = iCouponService.save(coupon);
        return new ResultData().setData(flag);
    }

    /**
     * 根据优惠券的编号，查询优惠券
     * @param cid
     * @return
     */
    @RequestMapping("/getCouponById")
    public ResultData<Coupon> getCouponById(@RequestParam Integer cid){

        Coupon coupon = iCouponService.getById(cid);
        return new ResultData().setData(coupon);
    }

    /**
     * 查询优惠券列表
     * @return
     */
    @RequestMapping("/getCouponList")
    public ResultData<List<Coupon>> getCouponList(){
        List<Coupon> couponList = iCouponService.list();
        return new ResultData().setData(couponList);
    }

     /**
     * 前端查询优惠券列表
     * @return
     */
    @RequestMapping("/getCouponCore")
    @IsLogin
    public ResultData<List<Coupon>> getCouponCore(){
        List<Coupon> couponList = iCouponIssueService.getCouponCore();
        return new ResultData().setData(couponList);
    }

    /**
     * 领取优惠券
     * @param issid
     * @return
     */
    @IsLogin(mustLogin = true)
    @RequestMapping("/receiveCoupon")
    public ResultData<Boolean> receiveCoupon(Integer issid){

        //领取指定的优惠券
        int result = iCouponService.receiveCoupon(issid);

        return new ResultData().setData(result>0);

    }


    /**
     * 前端根据用户信息查询优惠券列表
     * @param orderPriceParams
     * @return
     */
    @RequestMapping("/getCouponByUser")
    @IsLogin(mustLogin = true)
    public ResultData<Map<String,List<Coupon>>> getCouponByUser(OrderPriceParams orderPriceParams){

        System.out.println("优惠券服务收到的下订单的请求参数"+ orderPriceParams);

        Map<String,List<Coupon>> couponList = iCouponService.getCouponByUser(orderPriceParams);
        return new ResultData().setData(couponList);
    }





    /**
     * 发行优惠券
     * @param couponIssue
     * @return
     */
    @RequestMapping("/insertCouponIssue")
    public ResultData<Boolean> insertCouponIssue(@RequestBody CouponIssue couponIssue){

        boolean flag = iCouponIssueService.save(couponIssue);
        return new ResultData().setData(flag);
    }


    /**
     * 根据优惠券编号删除优惠券
     * @param cid
     * @return
     */
    @RequestMapping("/deleteCouponByCid")
    public ResultData<Boolean> deleteCouponByCid(@RequestParam Integer cid){
        //TODO 根据优惠券编号删除优惠券
        boolean flag = iCouponService.removeById(cid);
        System.out.println("优惠券根据优惠券编号删除"+ cid +" ," + flag);


        return new ResultData().setData(flag);
    }

    /**
     * 根据抢券的开始时间的毫秒值，查询该时间段的优惠券列表
     * @param time
     * @return
     */
    @RequestMapping("/getCouponListByTime")
    public ResultData<List<Coupon>> getCouponListByTime(long time){

        List<Coupon> couponList= iCouponIssueService.getCouponListByTime(time);
        return new ResultData().setData(couponList);
    }

}
