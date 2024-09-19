package com.ysf.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

import java.util.logging.Level;
import java.util.logging.Logger;

@Aspect
@Component
public class GenericAspect {
    private final Logger logger = Logger.getLogger(GenericAspect.class.getName());

//    @Around("execution(* com.ysf.services.*.*(..))")
    public void duration(ProceedingJoinPoint pjp) {
        System.out.println("duration aspect called");
        Instant start = Instant.now();

        try {
            pjp.proceed();
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "Exception in duration aspect", e);
        }

        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println("Total duration: " + duration.toSeconds() + " seconds");
    }

    @Before("execution(* com.ysf.*.*.*(..)) && args(isVehicleStarted, ..)")
    public void isVehicleStarted(boolean isVehicleStarted) {
        if (!isVehicleStarted) {
            throw new RuntimeException("Cannot move, vehicle not started");
        } else {
            System.out.println("Vehicle is already started");
        }
    }

    @AfterThrowing(value = "execution(* com.ysf.*.*.*(..))", throwing = "ex")
    public void exceptionThrown(JoinPoint jp, Exception ex) {
        System.out.println("Exception thrown by " + jp.getSignature().getName() +
                " method. Exception message: " + ex.getMessage());
    }

    @AfterReturning(value = "within(com.ysf..*)", returning = "returnValue")
    public void valueReturned(JoinPoint jp, Object returnValue) {
        System.out.println("Value returned by " + jp.getSignature().getName() +
                " method: " + returnValue);
    }

    @Before("@annotation(com.ysf.aspects.LogAspect)")
    public void log(JoinPoint jp) {
        System.out.println("logging aspect called for " + jp.getSignature().getName() + " method");
    }
}
