package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.contract.service.ContractService;
import com.icbcintern.prepaycard.pojo.Consume;
import com.icbcintern.prepaycard.pojo.PayedCard;
import com.icbcintern.prepaycard.service.ConsumeService;
import com.icbcintern.prepaycard.service.MerchantService;
import com.icbcintern.prepaycard.service.PayService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

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

    @Autowired
    MerchantService merchantService;
    /**
     * 用户对应预付卡进行消费
     *
     * @param consume 消费记录
     * @return Result
     * @throws Exception 消费报错信息
     */
    @PostMapping({"/consume","/consume/{merchantId}"})
    @Transactional
    public Result consume(@RequestBody Consume consume,@PathVariable(required = false) Integer merchantId) throws Exception {
        Result result = new Result();
        Integer payedCardId = consume.getPayedCardId();  // 预付卡id
        // 通过 payedCardId 查询 预付卡表，获取合约实例 id
        PayedCard payedCard = payService.getPayedCardById(payedCardId);
        // 消费时校验预付卡是否超出过期时间
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (payedCard.getExpireTime().before(now)) {   // 过期时间早于当下时间
            return Result.setFailMsg("预付卡已过期", null);
        }

        Integer instanceId = payedCard.getInstanceId();

        consume.setPayedTime(new Timestamp(System.currentTimeMillis()));
        String walletId = null;
        if (merchantId != null) {
            walletId = merchantService.getMerchantWalletByMerchantId(merchantId);
            if(walletId==null){
                return Result.setFailMsg("商户不存在或未开通收款钱包", null);
            }
        }
        result = contractService.transfer(instanceId, consume.getPayedAmount(), walletId);  // 执行合约进行转账

        try {
            if (result.getCode() != 0) throw new Exception("扣款失败");
            Map data = (Map) result.getData();
            consume.setDiscountPrice(Long.valueOf(String.valueOf(data.get("discountPrice"))));
            consume.setActualPrice(Long.valueOf(String.valueOf(data.get("actualPrice"))));
            consume.setGift(Long.valueOf(String.valueOf(data.get("gift"))));
            consume.setState(0);
        } catch (Exception ignore) {
            consume.setState(result!=null?result.getCode():1);
        }

        Result consumeResult = consumeService.insertConsume(consume); // 写入消费记录表
        consumeResult.setCode(consume.getState());
        if (consume.getState() != 0) {
            //输出详细的错误信息
            consumeResult.setMsg(result.getMsg());
        }
        return consumeResult;
    }

    /**
     * 根据预付卡 id 查询对应消费记录
     */
    @GetMapping("/consume/id/{payed_card_id}")
    public Result getConsumeByPayCardId(@PathVariable("payed_card_id") int payed_card_id) {
        Result result = new Result();
        List<Consume> consumes = consumeService.getConsumeByPayedCardId(payed_card_id);

        System.out.println("/consume/id/{payed_card_id}" + payed_card_id);
        System.out.println(consumes);

        if (consumes == null) {
            result.setCode(1);
            result.setMsg("查询的消费记录不存在");
        } else {
            Result.ok();
            result.setData(consumes);
        }
        return result;
    }

    @GetMapping("/consume/consumeId/{consumeId}")
    public Result getConsumeById(@PathVariable("consumeId") int consumeId) {
        Result result = new Result();
        Consume consume = consumeService.getConsumeById(consumeId);
        if (consume == null) {
            result.setCode(1);
            result.setMsg("查询的消费记录不存在");
        } else {
            result.setCode(0);
            result.setData(consume);
        }
        return result;
    }

}
