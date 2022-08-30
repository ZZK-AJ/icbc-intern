package com.icbcintern.prepaycard.contract.Aop;

import java.lang.annotation.*;

@Target({ElementType.METHOD}) //Type代表该注解可以放在类上，METHOD代表可以放在方法上
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ContractLog {
    String value() default "";
}
