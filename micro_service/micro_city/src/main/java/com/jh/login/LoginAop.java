package com.jh.login;


import com.jh.entity.ResultData;
import com.jh.entity.User;
import com.jh.util.JWTUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 切面类
 *
 *
 * 前置、后置、环绕、异常、后置完成
 */
@Aspect
@Component
public class LoginAop {

    /**
     * 环绕增强
     * @param joinPoint
     * @return
     */
    @Around("@annotation(IsLogin)")
    public  Object isLogin(ProceedingJoinPoint joinPoint){

        //通过请求获得jwtToken和devId
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        //通过参数获取jwtToken和devId
        String jwtToken = request.getParameter("jwtToken");
        String devId = request.getParameter("devId");
        User user = null;

        //进行判断
        if (jwtToken != null && devId != null){
            //有可能已经登录
            user = JWTUtil.isToken(jwtToken,devId);
        }

        if (user == null){
            //说明为合法登录
            //获取方法上的IsLogin注解，判断mustLogin方法的返回值,方法签名
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            IsLogin isLogin = method.getAnnotation(IsLogin.class);

            boolean flag = isLogin.mustLogin();
            if (flag){
                //强制要求登录才能使用
                //方式，通知前端，让前端配合操作
                return  new ResultData().setCode(ResultData.Code.TOLOGIN);
            }
        }

        //设置登录的用户信息
        UserUtil.setUser(user);
        Object result = null;

        try {
            //前置增强
            //目标方法的调用
            result = joinPoint.proceed();
            //后置增强
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //异常增强
        } finally {
            //后置完成时增强
        }

        return result;
    }
}
