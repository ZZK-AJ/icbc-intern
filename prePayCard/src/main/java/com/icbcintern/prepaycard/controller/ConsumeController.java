package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.contract.service.ContractService;
import com.icbcintern.prepaycard.pojo.Consume;
import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.pojo.Review;
import com.icbcintern.prepaycard.service.ConsumeService;
import com.icbcintern.prepaycard.service.PayService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description: 消费 controller 层
 **/
@RestController
public class ConsumeController {
    @Autowired
    PayService payService;
    @Autowired
    ContractService contractService;
    @Autowired
    ConsumeService consumeService;

    /**
     * 用户对应预付卡进行消费
     *
     * @param consume 消费记录
     * @return Result
     * @throws Exception 消费报错信息
     */
    @PostMapping("/consume")
    @Transactional
    public Result consume(@RequestBody Consume consume) throws Exception {
        Result result = new Result();
        Integer payedCardId = consume.getPayedCardId();  // 预付卡id
        // 通过 payedCardId 查询 预付卡表，获取合约实例 id
        PayedCard payedCard = payService.getPayedCardById(payedCardId);
        Integer instanceId = payedCard.getInstanceId();

        result = consumeService.insertConsume(consume); // 写入消费记录表
        result = contractService.transfer(instanceId, consume.getPayedAmount());  // 执行合约进行转账

        return result;
    }

    /**
     * 根据预付卡 id 查询对应消费记录
     */
    @GetMapping("/consume/id/{payed_card_id}")
    public Result getConsumeById(@PathVariable("payed_card_id") int payed_card_id) {
        Result result = new Result();
        List<Consume> consumes = consumeService.getConsumeByPayedCardId(payed_card_id);
        if (consumes == null) {
            result.setCode(1);
            result.setMsg("查询的消费记录不存在");
        } else {
            Result.ok();
            result.setData(consumes);
        }
        return result;
    }


}
