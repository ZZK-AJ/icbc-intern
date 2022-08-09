package com.icbcintern.prepaycard.service.impl;


import com.icbcintern.prepaycard.mapper.OperatorMapper;
import com.icbcintern.prepaycard.pojo.Merchant;
import com.icbcintern.prepaycard.pojo.Operator;
import com.icbcintern.prepaycard.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperatorServiceImpl implements OperatorService {
    @Autowired
    OperatorMapper operatorMapper;

    /**
     * 注册运营方
     */
    @Transactional
    @Override
    public boolean insertOperator(Operator operator) {
        if (operator.getName() != null && !"".equals(operator.getName())) {
            int effectNum = operatorMapper.insertOperator(operator);
            return effectNum > 0;
        }
        return false;
    }

    /**
     * 按 operatorName 查找 operator
     */
    @Override
    public Operator getOperatorByName(String operatorName) {
        return operatorMapper.getOperatorByName(operatorName);
    }
}
