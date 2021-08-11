package com.whb.tools;

import com.whb.model.UserEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

@Aspect
@Component
public class DoneTimeAspect {
    private Logger logger = LoggerFactory.getLogger(DoneTimeAspect.class);
    private UserEntity userEntity;

    public DoneTimeAspect(){
        logger.info("*******AOP start");
    }
    @Around("@annotation(doneTime) && args(userEntity)")
    public Object around(ProceedingJoinPoint joinPoint, DoneTime doneTime,UserEntity userEntity){
        this.userEntity= userEntity;
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = joinPoint.proceed(new Object[]{this.userEntity});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        logger.info("=======================================spend time :"+(System.currentTimeMillis()-start));
        return result;
    }
}
