package com.jh.coupon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 使用反射将动态的json转换成实体类
 *    将动态的json -> 实现类
 *
 * {
 *     "myclass":"com.qf.coupon.rule.ManJianRule",
 *     "fields":[
 *         {
 *             "type":"0",
 *             "name":"a",
 *             "value":"100"
 *         },
 *         {
 *             "type":"0",
 *             "name":"b",
 *             "value":"20"
 *         }]
 * }
 *
 *
 *
 * 对象: {key:value, key:value ...}
 * 数组：[value,value,value...]
 *
 * {key:[], key:{}, key:[{},{}]}
 *
 */
public class CouponUtil {

    public static  <T> T dynamicFieldToObject(String dynamicJson){

        try {
            //解析JSON字符串
            JSONObject jsonObject = JSON.parseObject(dynamicJson);
            //TODO 获得当前实现类的全限定类名
            String myClass = jsonObject.getString("myclass");
            System.out.println("优惠券全限定类名"+myClass);

            //反射动态创建实现类,通过类的全路径限定名获得该类的反射对象
            Class aClass = Class.forName(myClass);

            //通过反射对象，创建实例对象
            Object obj = aClass.newInstance();

            //解析类中的动态属性
            JSONArray jsonArray = jsonObject.getJSONArray("fields");
            for (int i = 0; i < jsonArray.size(); i++) {
                //从数组中解析出属性的json
                JSONObject fieldJson = jsonArray.getJSONObject(i);
                //获得属性的name, mustPrice 1000
                String name = fieldJson.getString("name");
                //获得属性的value, jianPrice 20
                String value = fieldJson.getString("value");

                //通过反射将动态属性set给指定的对象
                Field field = aClass.getDeclaredField(name);
                //赋予操作类中私有属性的权限
                field.setAccessible(true);

                //判断属性的类型
                Class typeClass = field.getType();
                if (typeClass == int.class || typeClass == Integer.class){
                    field.setInt(obj,Integer.parseInt(value));
                }else if (typeClass == float.class || typeClass == Float.class || typeClass == double.class || typeClass== Double.class){
                    field.setFloat(obj,Float.parseFloat(value));
                }else if (typeClass == Date.class){
                    field.set(obj,new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value));
                }else {
                    field.set(obj,value);
                }

            }
            return (T) obj;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}
