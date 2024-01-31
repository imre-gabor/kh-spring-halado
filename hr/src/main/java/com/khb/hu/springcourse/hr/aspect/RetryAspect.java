package com.khb.hu.springcourse.hr.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class RetryAspect {

    @Pointcut("@annotation(com.khb.hu.springcourse.hr.aspect.Retry) || @within(com.khb.hu.springcourse.hr.aspect.Retry)")
    public void retryPointCut(){}

    @Around("retryPointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{
        Retry retry = null;

        Signature signature = pjp.getSignature();
        if(signature instanceof MethodSignature){
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            retry = method.getAnnotation(Retry.class);
            if(retry == null) {
                Class<?> declaringType = signature.getDeclaringType();
                retry = declaringType.getAnnotation(Retry.class);
            }
        }

        int times = retry.times();
        long waitTime = retry.waitTime();

        if(times <= 0)
            times = 1;
        if(waitTime < 0)
            waitTime = 0;

        for(int numRetry = 0; numRetry <= times; numRetry ++){
            try {
                System.out.println("trying to call, retry number:" + numRetry);
                return pjp.proceed();
            } catch (Exception e){
                if(numRetry == times)
                    throw e;

                Thread.sleep(waitTime);
            }
        }
        return null;
    }
}
