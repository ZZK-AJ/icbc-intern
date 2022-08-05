package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.mapper.ConsumeMapper;
import com.icbcintern.prepaycard.pojo.Consume;
import com.icbcintern.prepaycard.service.ConsumeService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumeServiceImpl implements ConsumeService {
    @Autowired
    ConsumeMapper consumeMapper;

    @Override
    public Result insertConsume(Consume consume) throws Exception {
        try {
            consumeMapper.insertConsume(consume);
        } catch (Exception e) {
            return new Result(1, e.toString(), null);
        }
        Result result = Result.ok();
        result.setData(consume);
        return result;
    }

    @Override
    public List<Consume> getConsumeByPayedCardId(Integer payedCardId) {
        return consumeMapper.getConsumeByPayedCardId(payedCardId);
    }
}
