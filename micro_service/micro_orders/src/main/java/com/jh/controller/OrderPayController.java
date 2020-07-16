package com.jh.controller;


import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.jh.entity.Orders;
import com.jh.entity.Room;
import com.jh.feign.HotelFeign;
import com.jh.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/pay")
@Slf4j
public class OrderPayController {

    @Autowired
    private  IOrderService iOrderService;

    @Autowired
    private HotelFeign hotelFeign;

    @RequestMapping("/alipay")
    public String alipay(String oid){


        //查询订单
        Orders orders = iOrderService.getById(oid);
        //查询房型的信息
        Room room = hotelFeign.selectHotelRoomByRId(orders.getRid()).getData();

        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
//                "https://openapi.alipay.com/gateway.do",
                "https://openapi.alipaydev.com/gateway.do",
                "2016102800774562",
                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCwD+DPMsVqxlHQG2s915gKiH9Sm/DbAC7gBQgbLNYR1scTjA/aH//6TFBcKAtKYM+q2zNLurUOMxCBus/jjERVf6D6/HWYNqb5EQmdD9SMpJAUjBQpViQeF4QIV4hAo7ougxiO3l1mDIPGKRTTR8FE7kj7fKdqS4xJ6YQ5Je1L7+pSrBslTFNzYi+tHfWxC1vdO+5g2AqAzpuW6lq29Uly9wkV8hX9UxNRAlZnI6Tcfch8sGOOuh637CXiXE5dlwkBKoNfBnr0E0bZp47MNOKDPY0zkoPlW/WEwqEP17VSa/7CmympTZe5+UcA2kB7O/wx9JB4/HgwmMZQCf+63kZhAgMBAAECggEBAIOb+vMoAsckZLfqJypdZTxKeNLp2wv6gQ/Y8wxNYeCH6tzD0H2/efxC27dn/7ij0djobtnnQbzRkz9GxGxWyCYZd97mpYkZIYwBnmwYD0zrHwEdDV0xFWKhNYN+201mlqB6QvtZYfwM0txojTa65tB+R3Qm5Ibi3oXJ15tYrKAcBEFbG43RS/jSMpXvfYp+WTJ8FhH/GQCmBoqeVkTUfFs9p01lMwCoaAU4pY44YBWHDmmC6CmZedp2eaJGctUsyS7fMrOwsL4abfNkQoxHwiQpbgeRGufqufswaFvA+R8DzHl45t2y59pyRtr7i0RI5P1XQGJEtRrLhw8v366qXaECgYEA2wJA+/+8Shj1NQqRk2U4XAXU0aZi5162wCVRwzCF0RofXiQEqghLvMXGabo/Qj0eQOi9mYm3GA1y5Q/l+IFTc5TDrkr0Bgvadcw3YhtB3fQLf3tgFlEvdFSBZFWAJSu+B3MZE3Zs6VAOiCUqzl0WqEtsWa2CFzjaPTxHnybDih0CgYEAzcykyUEw4ViDI3TQXFl+rnGc2VQlQREFakvvpSBbACkiEP6iI+htfoAcUPhcJ0VsyCxcS3mQhBxmRgMsqOLRn3VTL7y25GfoUmV6LTYANHUMkH/hZemtIzEOurcYYazQlTMIOZfw7LdZPQRz+kaXPPrhD3WYddoHSVJYm4JxGhUCgYEAyt8Xm1eV732XBv83QrYssvGj5mCelXyjBPKM/qZgDwaHsAjlWSw+HCuIVD/2gUFkWPQ/TY7IUDuFBrLncH878oEHkthTm5Y3U49MblCZfySl0f1TUNj7gGMXSP36qnQOn4/61pCI6scB7xOex3JrnpHfWGLlcuVzI+5G2iJqV/ECgYEAmZ/8Guf5Lx69tggLSgTcliflaC5yKOIl4rWQzPkcNUB/V6RdMOTeZ6IGgUMEt6QRklPbCdRTVutERVs1SVUYGiqg0G/VLyeJu4hY7crz2DTQDkYH4eXQAwcp8aOgJbm1csrHhwavKGWtzei3EssDNtgojTvBdagt7EP2NQ/okgkCgYAuzK3l0KOu+gbBLgMCrTwWm331hFJUhbRkKg9SEoBbRRN90QA1LJmFO9+E3E/EIjv7ZfVK0BI43RIA2xHfp0DDNGR4kzZ9Z1pbFURo7xsnezHdEIMATWmaCMkhz0UyEr3AdWeAVg1KsKdw54DLVwUiaWaWq2J6COAgusTZHisHYg==",
                "json",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAheY0OpzCEQYLJDbodYxAXTseXZbVDg+fky0NVBJKrCVslMCDx+fmvFb1mf+fGu1qoV37Mjn2q+Npdjanp+RcFZMq7BYh6XnS02KXBB4cXRnunmB8o5Cr8fzg0cgVY/lRAXd4f8EoAxyviTAzuYVPH+isUL/O1tchHRXTsN/iZO5GByUDAnHAQoLvOwM4gqAY/ztSzjHBtNLCC+pBYAzswJlD+zrdtnlgBOt0RcJHuw5LV3ANZtlrJ2OcNx7uY9xNr7Lz11be5IboPXHH5qLusJ0uq4FTGmfkIxcGXfx8VpMoriWxaqjIovOOXMa48BNFXp8PW0sp1miII/A45jsE1wIDAQAB",
                "RSA2");

        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setReturnUrl("http://www.baidu.com");
        alipayRequest.setNotifyUrl("http://jh.free.idcfengye.com/pay/aliPayResult");//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                " \"out_trade_no\":\""+ orders.getOid() +"\"," +
                " \"total_amount\":\""+ orders.getAllPrice() +"\"," +
                " \"subject\":\""+ room.getTitle()+"\"," +
                " \"product_code\":\"QUICK_WAP_PAY\"" +
                " }");//填充业务参数
        String form="";
        try {
            form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        System.out.println("跳转到支付宝支付页面");
        return form;
    }


    @RequestMapping("/aliPayResult")
    public String aliPayResult(HttpServletRequest request){
        //1、接收支付宝的结果
        System.out.println("支付宝支付结果返回！！！");
        //从请求中获取所有的参数
        Map<String,String[]> parameterMap = request.getParameterMap();
        //将异步通知中收到的所有参数存放到paramsMap中
        Map<String,String> paramsMap = new HashMap<>();
        parameterMap.forEach((key,values)->{
            System.out.println( key + " , "+ Arrays.toString(values));
            paramsMap.put(key,values[0]);
        });

        //做支付宝的结果验签 -- 验证是否由支付宝发出的该请求
        boolean signVerified = false;

        try {
             signVerified = AlipaySignature.rsaCheckV1(paramsMap,
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAheY0OpzCEQYLJDbodYxAXTseXZbVDg+fky0NVBJKrCVslMCDx+fmvFb1mf+fGu1qoV37Mjn2q+Npdjanp+RcFZMq7BYh6XnS02KXBB4cXRnunmB8o5Cr8fzg0cgVY/lRAXd4f8EoAxyviTAzuYVPH+isUL/O1tchHRXTsN/iZO5GByUDAnHAQoLvOwM4gqAY/ztSzjHBtNLCC+pBYAzswJlD+zrdtnlgBOt0RcJHuw5LV3ANZtlrJ2OcNx7uY9xNr7Lz11be5IboPXHH5qLusJ0uq4FTGmfkIxcGXfx8VpMoriWxaqjIovOOXMa48BNFXp8PW0sp1miII/A45jsE1wIDAQAB",
                    "UTF-8",
                    "RSA2");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        //判断验签是否成功
        if (signVerified){
            //TODO 支付宝验签成功之后，按照支付结果异步通知中的描述，对支付结果中的业务内容进行二次校验，校验成功后在response中返回success并继续自身业务处理，校验失败返回failure
            System.out.println("支付验签成功");
            //2、根据结果修改订单状态
            String orderId = paramsMap.get("out_trade_no");
            iOrderService.updateOrderStatus(orderId,1);
            return "success";
        }else{
            //TODO验签失败则记录到异常日志中，并在response中返回failure
            System.out.println("支付验签失败");
            return "failure";
        }

    }
}
