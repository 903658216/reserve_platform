package com.jh.controller;

import com.jh.entity.*;
import com.jh.feign.CouponFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/system")
public class SystemDictionariesController {


    @Autowired
    private CouponFeign couponFeign;

    /**
     * 查询字典列表
     * @param model
     * @return
     */
    @RequestMapping("/dictionariesList")
    public String dictionariesList(Model model){
        //调用优惠券服务
        List<Dictionaries> dictionariesList = couponFeign.getDictionariesList().getData();
        model.addAttribute("dictionariesList",dictionariesList);

        return "dictionariesList";
    }

    /**
     * 跳转到添加字典页面
     * @return
     */
    @RequestMapping("/toDictionariesAdd")
    public String toDictionariesAdd(){

        return "dictionariesAdd";
    }

    /**
     * 跳转到添加字典内容数据页面
     * @return
     */
    @RequestMapping("/toDictionariesContentAdd")
    public String toDictionariesContentAdd(){

        return "dictionariesContentAdd";
    }

    /**
     * 新增字典
     * @param dictionaries
     * @return
     */
    @RequestMapping("/insertDictionaries")
    public String insertDictionaries(Dictionaries dictionaries){

        Boolean flag = couponFeign.insertDictionaries(dictionaries).getData();

        return "redirect:/system/dictionariesList";
    }

    /**
     * 新增字典内容数据
     * @param dictionariesContent
     * @return
     */
    @RequestMapping("/insertDictionariesContent")
    public String insertDictionariesContent(DictionariesContent dictionariesContent){

        Boolean flag = couponFeign.insertDictionariesContent(dictionariesContent).getData();

        return "redirect:/system/dictionariesList";
    }

    /**
     * 根据字典编号删除字典
     * @param id
     * @return
     */
    @RequestMapping("/deleteDictionariesById")
    public String deleteDictionariesById(Integer id){

        Boolean flag = couponFeign.deleteDictionariesById(id).getData();
        return "redirect:/system/dictionariesList";
    }


    /**
     * 查询优惠券模板列表
     * @param model
     * @return
     */
    @RequestMapping("/couponTemplateList")
    public String couponTemplateList(Model model){

        List<CouponTemplate> couponTemplateList = couponFeign.getCouponTemplates().getData();
        model.addAttribute("couponTemplateList",couponTemplateList);
        return "couponTemplateList";
    }

    /**
     * 新增优惠券模板
     * @param couponTemplate
     * @return
     */
    @RequestMapping("/insetCouponTemplate")
    public String insetCouponTemplate(CouponTemplate couponTemplate){
        ResultData<Boolean> resultData = couponFeign.insertCouponTemplate(couponTemplate);
        return "redirect:/system/couponTemplateList";
    }

    /**
     * 根据优惠券模板的id删除模板
     * @param id
     * @return
     */
    @RequestMapping("/deleteCouponTemplateById")
    public String  deleteCouponTemplateById(Integer id){

        ResultData<Boolean> resultData =couponFeign.deleteCouponTemplateById(id);
        return "redirect:/system/couponTemplateList";
    }

    /**
     * 查询优惠券列表
     * @param model
     * @return
     */
    @RequestMapping("/couponList")
    public String couponList(Model model){

        List<Coupon> couponList = couponFeign.getCouponList().getData();
        model.addAttribute("couponList",couponList);

        return "couponList";
    }

    /**
     * 跳转到添加优惠券模本页面
     * @return
     */
    @RequestMapping("/toCouponTemplateAdd")
    public String toCouponTemplateAdd(){

        return "couponTemplateAdd";
    }

    /**
     * 跳转到添加优惠券页面
     * @return
     */
    @RequestMapping("/toCouponAdd")
    public String toCouponAdd(){
        return "couponAdd";
    }


    /**
     * 新增优惠券
     * @param coupon
     * @return
     */
    @RequestMapping("/addCoupon")
    public String addCoupon(Coupon coupon){
        System.out.println("新增优惠券"+coupon);

        //新增优惠券
        couponFeign.insertCoupon(coupon);
        return "redirect:/system/couponList";
    }

    /**
     * 根据优惠券编号删除优惠券
     * @param cid
     * @return
     */
    @RequestMapping("/deleteCouponByCid")
    public String deleteCouponByCid(Integer cid){

        System.out.println("后台根据优惠券编号删除"+ cid);
        ResultData<Boolean> flag = couponFeign.deleteCouponByCid(cid);

        return "redirect:/system/couponList";
    }


    /**
     * 跳转到优惠券发布页面
     * @return
     */
    @RequestMapping("/toCouponIssue")
    public String toCouponIssue(){

        return "couponIssue";
    }


    /**
     * 发布优惠券
     * @param couponIssue
     * @return
     */
    @RequestMapping("/addCouponIssue")
    public String addCouponIssue(CouponIssue couponIssue){

        couponFeign.insertCouponIssue(couponIssue);
        return "redirect:/system/couponList";
    }
}
