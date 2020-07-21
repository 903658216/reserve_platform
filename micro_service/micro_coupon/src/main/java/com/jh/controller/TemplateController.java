package com.jh.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jh.entity.CouponTemplate;
import com.jh.entity.ResultData;
import com.jh.service.ICouponTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/couponTemplate")
public class TemplateController {

    @Autowired
    private ICouponTemplateService iCouponTemplateService;

    /**
     * 查询优惠券模板列表
     *  //@CrossOrigin注解用于处理跨域问题
     * @return
     */
    @CrossOrigin
    @RequestMapping("/getCouponTemplates")
    public ResultData<List<CouponTemplate>> getCouponTemplates(){

        List<CouponTemplate> couponTemplateList = iCouponTemplateService.list();
        return new ResultData().setData(couponTemplateList);
    }

    /**
     * 根据优惠券模板的id删除模板
     * @param id
     * @return
     */
    @RequestMapping("/deleteCouponTemplateById")
    public ResultData<Boolean> deleteCouponTemplateById(@RequestParam Integer id){

        boolean flag = iCouponTemplateService.removeById(id);
        return new ResultData().setData(flag);
    }

    /**
     * 根据模板类型值-查询该类型的模板列表
     * @param typeNo
     * @return
     */
    @RequestMapping("/getCouponTemplatesByType")
    public ResultData<List<CouponTemplate>> getCouponTemplatesByType(@RequestParam Integer typeNo){

        List<CouponTemplate> couponTemplateList = iCouponTemplateService.list(new QueryWrapper<CouponTemplate>().eq("template_type",typeNo));
        return new ResultData().setData(couponTemplateList);
    }

    /**
     * 新增优惠券模板
     * @param couponTemplate
     * @return
     */
    @RequestMapping("/insertCouponTemplate")
    public ResultData<Boolean> insertCouponTemplate(@RequestBody CouponTemplate couponTemplate){

        boolean flag = iCouponTemplateService.save(couponTemplate);
        return new ResultData().setData(flag);
    }

}
