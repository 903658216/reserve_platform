package com.jh.coupon.rule;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

/**
 * 规则的接口规范
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ManJianRule.class, name = "ManJianRule")
})
public interface IRule extends Serializable {

    /**
     * 该订单的金额是否满足优惠的金额条件
     * @param price
     * @return
     */
    boolean hasCouponPrice(double price);


    /**
     * 进行价格的优惠计算，返回优惠后的结果(优惠后的订单价格)
     * @param price
     * @return
     */
    double couponCalculate(double price);

    /**
     * 优惠了多少金额，返回扣减的金额
     * @param price
     * @return
     */
    double conponPrice(double price);
}
