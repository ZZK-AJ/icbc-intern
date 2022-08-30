package com.icbcintern.prepaycard.contract.Aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-08-25 14:55
 **/

@Component
@Aspect
public class ContractLogAspect {

    ObjectMapper json = new ObjectMapper();
    @Autowired
    ContractLogMapper contractLogMapper;
    @Pointcut("@annotation(com.icbcintern.prepaycard.contract.Aop.ContractLog)")
    public void logPointcut(){

    }

    @Around(value = "logPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Throwable exception = null;
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable catchException) {
            exception = catchException;
            throw exception;
        } finally {
            long end = System.currentTimeMillis();
            long cost = end - start;
            String targetMethodName = joinPoint.getSignature().getName();
            ContractLog log = new ContractLog();
            log.setStart(new Timestamp(start));
            log.setEnd(new Timestamp(end));
            log.setMethodName(targetMethodName);
            log.setParams(getParam(joinPoint));
            log.setExecTime(cost);
            String res = json.writeValueAsString(result);
            log.setResult(res.substring(0,Math.min(res.length(),199)));
            log.setThrowException(json.writeValueAsString(exception==null?null:exception.getMessage()));
            System.out.println(log);
            contractLogMapper.insertLog(log);

        }
        return result;


    }
    public String getParam(ProceedingJoinPoint proceedingJoinPoint) {
        Map<String, Object> map = new HashMap<String, Object>();
        Object[] values = proceedingJoinPoint.getArgs();
        String[] names = ((CodeSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        try {
            return json.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return String.valueOf(map);
        }
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class ContractLog{
        Timestamp start;
        Timestamp end;
        String methodName;
        String params;
        Long execTime;
        String result;
        String throwException;
    }
    @Mapper
    @Repository
    interface ContractLogMapper{
        @Insert("insert into contractLog(start,end,method_name,params,exec_time,result,throw_exception)" +
                "values (#{start},#{end},#{methodName},#{params},#{execTime},#{result},#{throwException})")
        void insertLog(ContractLog contractLog);
    }
}
