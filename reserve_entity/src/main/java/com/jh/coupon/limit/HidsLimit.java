package com.jh.coupon.limit;

import com.jh.entity.OrderPriceParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 优惠券使用：酒店ID的限制
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class HidsLimit implements ILimit{

    /**
     * 可用该优惠券的酒店id编号，用逗号隔开
     */
    private  String hids;

    @Override
    public boolean hasLimit(OrderPriceParams orderPriceParams) {

        //下单的酒店id
        Integer hid = orderPriceParams.getHid();

        //获得允许优惠的酒店id
        String [] hidsArray = hids.split(",");
        for (int i = 0; i < hidsArray.length; i++) {
            //判断是否可以参与优惠
//            System.out.println("可以使用该优惠券的酒店编号"+hidsArray[i]);
            if (Integer.parseInt(hidsArray[i]) == hid){

                return true;
            }

        }

        return false;
    }
}
