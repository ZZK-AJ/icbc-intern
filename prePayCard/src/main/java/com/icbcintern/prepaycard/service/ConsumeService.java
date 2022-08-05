package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.Consume;
import com.icbcintern.prepaycard.utils.Result;

import java.util.List;

public interface ConsumeService {
    Result insertConsume(Consume consume) throws Exception;

    List<Consume> getConsumeByPayedCardId(Integer payedCardId);
}
