package com.jh.coupon.limit;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.jh.entity.OrderPriceParams;

import java.io.Serializable;

/**
 * 限制的规范接口
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = HidsLimit.class, name = "HidsLimit")
})
public interface ILimit extends Serializable {

    /**
     * 判断下单的酒店是否符合该优惠规则
     * @param orderPriceParams
     * @return
     */
    boolean hasLimit(OrderPriceParams orderPriceParams);

}
