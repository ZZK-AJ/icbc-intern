package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.pojo.Operator;
import com.icbcintern.prepaycard.service.OperatorService;
import com.icbcintern.prepaycard.utils.JwtTools;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class OperatorController {
    @Autowired
    OperatorService operatorService;

    /**
     * 运营方 注册
     */
    @PostMapping("/operatorRegister")
    public Result OperatorRegister(@RequestBody Map<String, String> operatorMap) {
        Operator operator = new Operator();
//        Result result = new Result();

        if (operatorMap.get("operatorName").isEmpty() || operatorMap.get("loginPasswd").isEmpty()) {
            return Result.setFailMsg("注册失败，运营方名和密码不能为空", null);
        }
        Operator existOperator = operatorService.getOperatorByName(operatorMap.get("operatorName"));
        if (existOperator != null) {
            return Result.setFailMsg("注册失败，该运营方名存在", null);
        }

        operator.setName(operatorMap.get("operatorName"));
        operator.setLoginPasswd(operatorMap.get("loginPasswd"));
        boolean r = operatorService.insertOperator(operator);
        if (r) {
            return Result.setSuccessMsg("运营方注册成功", null);
        } else {
            return Result.setSuccessMsg("运营方注册失败", null);
        }
    }

    /**
     * 运营方登录
     */
    @PostMapping("/operatorLogin")
    public Result operatorLogin(@RequestBody Map<String, String> operatorMap) {
        Result result = new Result();
        Operator existOperator = operatorService.getOperatorByName(operatorMap.get("operatorName"));
        if (existOperator == null) {
            return Result.setFailMsg("该运营方未注册,请先注册", null);
        }
        if (existOperator.getLoginPasswd().equals(operatorMap.get("loginPasswd"))) {
            // 返回 token
            String token = JwtTools.createToken(existOperator);
            return Result.setSuccessMsg("运营方登录中", token);
        } else {
            return Result.setFailMsg("运营方登录失败，查询登录密码是否错误", null);
        }
    }

}
