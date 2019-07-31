package com.web.aop;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Enumeration;

/**
 * @author lizehao
 * */
@Aspect
@Component
public class WebLogAspect {

    private static final int MAX_LOG_LENGTH = 120;

    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("execution(public * com.controller..*.*(..))")
    public void webLog(){}

    @Around("webLog()")
    public Object controllerAround(ProceedingJoinPoint joinPoint) throws Throwable{
        /**
         * 请求日志打印
         */
        StringBuilder classAndMethodName = new StringBuilder();
        //获取当前请求属性集
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //获取请求
        HttpServletRequest request = sra.getRequest();
        //获取请求地址
        String requestUrl = request.getRequestURL().toString();
        //记录请求地址
        log.info("请求路径：---> {}", requestUrl);
        //记录请求开始时间
        long startTime = System.currentTimeMillis();
        Class<?> target = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Class<?>[] paramTypes = methodSignature.getParameterTypes();
        String methodName = joinPoint.getSignature().getName();
        Method currentMethod = target.getMethod(methodName, paramTypes);
        classAndMethodName.append(target.getName() ).append(".").append(currentMethod.getName()).append("()");
        boolean flag =  currentMethod.isAnnotationPresent(ApiOperation.class);
        if(flag){
            ApiOperation annotation = currentMethod.getAnnotation(ApiOperation.class);
            classAndMethodName.append(" >>> ").append(annotation.value());
        }
        log.info("执行函数：---> {}", classAndMethodName);
        /**
         * 打印key/value参数
         */
        Enumeration<String> enumeration = request.getParameterNames();
        while (enumeration.hasMoreElements()){
            String param = enumeration.nextElement();
            log.info("key/value参数：---> {}", param+" : "+request.getParameter(param));
        }

        /**
         * 打印@RequestBody参数、RequestParam注解参数非空参数拦截
         */
        Parameter[] params = currentMethod.getParameters();
        if(params != null && params.length > 0){
            Object[] args = joinPoint.getArgs();
            for(int i=0; i< params.length; i++){
                Annotation[] annos = params[i].getDeclaredAnnotations();
                if(annos != null && annos.length > 0){
                    for(Annotation anno : annos){
                        if(anno instanceof RequestBody){
                            log.info("@RequestBody参数：---> {}", JSONObject.toJSONString(args[i]));
                        }
                    }
                }
            }
        }
        Object object = joinPoint.proceed();
        String data = JSONObject.toJSONString(object);
        log.info("返回结果: ---> {}",data.length() < MAX_LOG_LENGTH ? data:data.substring(0,MAX_LOG_LENGTH));
        long endTime = System.currentTimeMillis();
        log.info("响应时间：---> {} 毫秒", endTime-startTime);
        return object;
    }
}
