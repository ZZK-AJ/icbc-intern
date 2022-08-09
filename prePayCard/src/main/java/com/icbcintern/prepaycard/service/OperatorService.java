package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.Operator;


public interface OperatorService {
    // 注册运营方
    boolean insertOperator(Operator operator);

    Operator getOperatorByName(String operatorName);
}
