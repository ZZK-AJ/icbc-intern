package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.pojo.Consume;
import com.icbcintern.prepaycard.service.PayService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * description: 消费 controller 层
 **/
@RestController
public class ConsumeController {
    @Autowired
    PayService payService;

    @PostMapping("/consume")
    public Result consume(@RequestBody Consume consume) {
        Integer payedCardId = consume.getCardId();
        // 通过 payedCardId 查询 预付卡表，获取合约实例 id

//        payService.g
        return Result.ok();
    }
}
