package com.thoughtworks.taskmaster.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LogManager.getLogger(LoggingAspect.class);

    @Before("@annotation(com.thoughtworks.taskmaster.annotations.Log)")
    public void logBefore(JoinPoint joinPoint){

        Object[] args=joinPoint.getArgs();
        String parameters;
        String className= joinPoint.getTarget().getClass().getSimpleName();
        String methodName=joinPoint.getSignature().getName();
        log.info("Entered: {}.{}", className,methodName);

    }

    @After("@annotation(com.thoughtworks.taskmaster.annotations.Log)")
    public void logAfter(JoinPoint joinPoint){

        String className= joinPoint.getTarget().getClass().getSimpleName();
        String methodName=joinPoint.getSignature().getName();
        log.info("Completed: {}.{}", className,methodName);
    }
}
