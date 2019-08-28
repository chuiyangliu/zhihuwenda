package com.willow.advancednc.aspect;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Aspect
@Component
public class LogAspect {

    private static final Logger logger= LoggerFactory.getLogger(LogAspect.class);
    @Before("execution(* com.willow.advancednc.controller.*.*(..))")
    public void before(){
        logger.info("before method");
    }

    @After("execution(* com.willow.advancednc.controller.*.*(..))")
    public void after(){
    }

}
