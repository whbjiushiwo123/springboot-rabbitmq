package com.whb.tools;

import com.whb.model.UserEntity;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Aspect
@Service
public class StartTime {
    private Logger logger = LoggerFactory.getLogger(StartTime.class);
    private UserEntity userEntity;

    public StartTime(){
        logger.info("*******AOP start");
    }

    @Pointcut("execution(* com.whb.service.impl.*.*Register())")
    public void start(){

    }
    @Around("start() && args(userEntity)")
    public Object startTime(ProceedingJoinPoint proceedingJoinPoint,UserEntity userEntity){
        this.userEntity= userEntity;
        long start = System.currentTimeMillis();
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed(new Object[]{this.userEntity});
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        logger.info("*****spend time :"+(System.currentTimeMillis()-start));
        return result;
    }
}
