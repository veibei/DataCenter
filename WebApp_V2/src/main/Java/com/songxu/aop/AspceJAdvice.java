package com.songxu.aop;

import com.alibaba.fastjson.JSON;
import com.songxu.service.TokenService;
import com.songxu.util.ConstantValue;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import javax.annotation.Resource;


/**
 * Created by songxu on 2016/11/2.
 */
@Aspect
public class AspceJAdvice {

    @Resource
    private TokenService tokenService;

    @Around("@annotation(AopMethod)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        if (args != null && args.length == 1) {
            String token = args[0].toString();
            if (tokenService.getUser(token) == null) {
                return JSON.toJSONString(ConstantValue.TIMEOUT);//µÇÂ¼³¬Ê±
            }
        }
        try {
            Object ret = joinPoint.proceed();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return JSON.toJSONString(ConstantValue.ERROR);
        }


    }

}
