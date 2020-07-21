package com.jh.feign;

import com.jh.entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("micro-coupon")
public interface CouponFeign {

    /**
     * 查询字典列表
     * @return ResultData<List<Dictionaries>>
     */
    @RequestMapping("/couponDictionaries/getDictionariesList")
    ResultData<List<Dictionaries>> getDictionariesList();

    /**
     * 根据字典编号查询字典数据实体类信息
     * @param did
     * @return
     */
//    @RequestMapping("/couponDictionaries/getDictionariesByDId")
//    ResultData<List<DictionariesContent>> getDictionariesByDId(@RequestParam Integer did);

    /**
     * 根据字典编号查询字典(内容)数据实体类信息
     * @param did
     * @return
     */
    @RequestMapping("/couponDictionaries/getDictionariesContentsByDId")
    ResultData<List<DictionariesContent>> getDictionariesContentsByDId(@RequestParam Integer did);

    /**
     * 新增字典
     * @param dictionaries
     * @return
     */
    @RequestMapping("/couponDictionaries/insertDictionaries")
    ResultData<Boolean> insertDictionaries(@RequestBody Dictionaries dictionaries);

    /**
     * 根据字典的id删除字典
     * @param id
     * @return
     */
    @RequestMapping("/couponDictionaries/deleteDictionariesById")
    ResultData<Boolean> deleteDictionariesById(@RequestParam Integer id);
    /**
     * 新增字典数据内容
     * @param content
     * @return
     */
    @RequestMapping("/couponDictionaries/insertDictionariesContent")
    ResultData<Boolean> insertDictionariesContent(@RequestBody DictionariesContent content);

    /**
     * 查询优惠券模板列表
     * @return
     */
    @RequestMapping("/couponTemplate/getCouponTemplates")
    ResultData<List<CouponTemplate>> getCouponTemplates();

    /**
     * 根据模板类型值-查询该类型的模板列表
     * @param typeNo
     * @return
     */
    @RequestMapping("/couponTemplate/getCouponTemplatesByType")
    ResultData<List<CouponTemplate>> getCouponTemplatesByType(@RequestParam Integer typeNo);

    /**
     * 新增优惠券模板
     * @param couponTemplate
     * @return
     */
    @RequestMapping("/couponTemplate/insertCouponTemplate")
    ResultData<Boolean> insertCouponTemplate(@RequestBody CouponTemplate couponTemplate);


    /**
     * 查询优惠券列表
     * @return
     */
    @RequestMapping("/coupon/getCouponList")
    ResultData<List<Coupon>> getCouponList();

    /**
     * 新增优惠券
     * @param coupon
     * @return
     */
    @RequestMapping("/coupon/insertCoupon")
    ResultData<Boolean> insertCoupon(@RequestBody Coupon coupon);

    /**
     * 发行优惠券
     * @param couponIssue
     * @return
     */
    @RequestMapping("/coupon/insertCouponIssue")
    ResultData<Boolean> insertCouponIssue(@RequestBody CouponIssue couponIssue);


    /**
     * 根据优惠券模板的id删除模板
     * @param id
     * @return
     */
    @RequestMapping("/couponTemplate/deleteCouponTemplateById")
    ResultData<Boolean> deleteCouponTemplateById(@RequestParam Integer id);

    /**
     * 根据优惠券编号删除优惠券
     * @param cid
     * @return
     */
    @RequestMapping("/coupon/deleteCouponByCid")
    ResultData<Boolean> deleteCouponByCid(@RequestParam Integer cid);

    /**
     * 根据优惠券的编号，查询优惠券
     * @param cid
     * @return
     */
    @RequestMapping("/coupon/getCouponById")
    ResultData<Coupon> getCouponById(@RequestParam Integer cid);
}
