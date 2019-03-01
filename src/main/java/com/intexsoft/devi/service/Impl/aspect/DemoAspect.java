package com.intexsoft.devi.service.Impl.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class DemoAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());



    //=====================================ALL========================================================

    @Pointcut("execution(public * com.intexsoft.devi.service.interfaces.DemoService.*(..))")
    public void callAtServiceAllMethod() {
    }

    @Before("callAtServiceAllMethod()")
    public void beforeCallAtDemoService(JoinPoint joinPoint) {
        String args = Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(","));
        logger.info("before all method " + joinPoint.toShortString());
    }

    @After("callAtServiceAllMethod()")
    public void afterCallAtDemoService(JoinPoint joinPoint) {
        logger.info("after all method " + joinPoint.toShortString());
    }

    //=====================================SET_LIST========================================================

    @Pointcut("execution(* com.intexsoft.devi.service.interfaces.DemoService.setList(..)) && args(list, anything))")
    public void callAtSetList(List<String> list, Boolean anything) {
    }

    @Before("callAtSetList(list, anything)")
    public void beforeCallAtSetList(JoinPoint joinPoint, List<String> list, Boolean anything) {
        list.add("aspect");
        logger.info("before setList(); " + list.toString());
    }

    @Pointcut("execution(* com.intexsoft.devi.service.interfaces.DemoService.setList(..)) && args(list,..))")
    public void callAtSetList2(List<String> list) {
    }

    @After("callAtSetList2(list)")
    public void afterCallAtSetList(JoinPoint joinPoint, List<String> list) {
        logger.info("after setList(); " + list.toString());
    }
    //=====================================ANNOTATION========================================================

    @Pointcut("@annotation(com.intexsoft.devi.annotation.Annotation)")
    public void callAtDemoAnnotation() {
    }

    @Before("callAtDemoAnnotation ()")
    public void beforeAnnotationMethod(JoinPoint joinPoint) {
        logger.info("before annotation method " + joinPoint.toShortString());
    }

    //=====================================RETURN_BOOL========================================================

    @Pointcut("execution(* com.intexsoft.devi.service.interfaces.DemoService.getTrue(..)) && args(retVal)")
    public void callAtReturnBool(Boolean retVal) {
    }

    @Before("callAtReturnBool(retVal)")
    public void beforeReturnReturnTrueAnotherWay(JoinPoint jp, Boolean retVal) {
        logger.info("before return bool: " + jp.getSignature());
        logger.info("return value: " + retVal);
    }

    @Pointcut("execution(* com.intexsoft.devi.service.interfaces.DemoService.getTrue(..))")
    public void callAtReturnTrue() {
    }

    @AfterReturning(pointcut = "callAtReturnTrue()", returning = "bool")
    public void afterReturnReturnTrue(JoinPoint jp, Boolean bool) {
        logger.info("AfterReturning Signature: " + jp.getSignature());
        logger.info("return value: " + bool);
    }

    @Pointcut("callAtReturnBool(retVal) && within(com.intexsoft.devi.service.Impl.aspect..*)")
    public void afterReturnReturnBoolWithPath(Boolean retVal) {
    }

    @After("afterReturnReturnBoolWithPath(retVal)")
    public void afterReturnBoolWithPath(JoinPoint joinPoint, Boolean retVal) {
        logger.info("after return bool with path " + joinPoint.toShortString());
    }

    @Pointcut("callAtReturnBool(retVal) && target(com.intexsoft.devi.service.interfaces.DemoService)")
    public void afterReturnReturnBoolWithTarget(Boolean retVal) {
    }

    @After("afterReturnReturnBoolWithTarget(retVal)")
    public void afterReturnBoolWithTarget(JoinPoint joinPoint, Boolean retVal) {
        logger.info("after return bool with target " + joinPoint.toShortString());
    }
}
