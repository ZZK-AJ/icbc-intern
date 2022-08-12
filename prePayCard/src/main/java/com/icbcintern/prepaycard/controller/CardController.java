package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.pojo.Card;
import com.icbcintern.prepaycard.service.CardService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {
    @Autowired
    CardService cardService;

    @GetMapping("/card/{id}")
    public Result getById(@PathVariable("id") int id) {
        Result result = Result.ok();
        Card card = cardService.getCardById(id);
        result.setData(card);
        return result;
    }

    @GetMapping("/card/all")
    public Result getAll() {
        Result result = Result.ok();;
        List<Card> cards = cardService.getAll();
        result.setData(cards);
        return result;
    }

}
