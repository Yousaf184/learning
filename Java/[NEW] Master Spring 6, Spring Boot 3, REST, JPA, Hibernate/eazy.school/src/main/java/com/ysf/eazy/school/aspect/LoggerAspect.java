package com.ysf.eazy.school.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

@Component
@Aspect
public class LoggerAspect {

    private static final Logger log = Logger.getLogger(LoggerAspect.class.getName());

    @Around("execution(* com.ysf.eazy.school..*.*(..))")
    public Object log(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Beginning execution for method: " + pjp.getSignature());

        Instant start = Instant.now();
        Object returnVal = pjp.proceed();
        Instant end = Instant.now();
        long duration = Duration.between(start, end).toMillis();

        log.info("End execution of method: " + pjp.getSignature() + ". Method" +
                "took " + duration + "(ms) to execute");

        return returnVal;
    }

    @AfterThrowing(value = "execution(* com.ysf.eazy..*.*(..))", throwing = "ex")
    public void logException(JoinPoint jp, Exception ex) {
        log.severe("EXCEPTION occurred in method: " + jp.getSignature() + ". " +
                "Error message: " + ex.getMessage());
    }
}
