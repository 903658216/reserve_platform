package com.jh.coupon.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 满减规则实现类 ：
 *  满多少 减多少
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ManJianRule implements IRule{

    /**
     * 订单满足这个金额可以使用优惠券
     */
    private  double mustPrice;
    /**
     * 订单使用优惠券可以减多少钱
     */
    private  double jianPrice;

    /**
     * 该订单的金额是否满足优惠的金额条件
     * @param price
     * @return
     */
    @Override
    public boolean hasCouponPrice(double price) {
        return mustPrice <= price;
    }

    /**
     * 进行价格的优惠计算，返回优惠后的结果(优惠后的订单价格)
     * @param price
     * @return
     */
    @Override
    public double couponCalculate(double price) {
        return BigDecimal.valueOf(price).subtract(BigDecimal.valueOf(jianPrice)).doubleValue();
    }

    /**
     * 优惠了多少金额，返回扣减的金额
     * @param price
     * @return
     */
    @Override
    public double conponPrice(double price) {
        return jianPrice;
    }
}
