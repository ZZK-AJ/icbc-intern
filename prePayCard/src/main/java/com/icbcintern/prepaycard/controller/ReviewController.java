package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.pojo.Card;
import com.icbcintern.prepaycard.pojo.Review;
import com.icbcintern.prepaycard.service.CardService;
import com.icbcintern.prepaycard.service.ReviewService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description:审核controller层
 *
 * @author: He Yihui
 * @create: 2022-07-27 14:34
 **/
@RestController
public class ReviewController {
    @Autowired
    ReviewService reviewService;
    @Autowired
    CardService cardService;

    @PostMapping("/review/submit")
    public Result submit(@RequestBody Review review) {
        //TODO: 判断是否商户本人提交，校验提交者的商户id是否与审核表中一致

        review.setReviewStatus(Review.STATUS_TYPE_PROCESSING);
        return reviewService.insertReview(review);
    }

    @GetMapping("/review/id/{id}")
    public Result getById(@PathVariable("id") int id) {
        Result result = new Result();
        Review review = reviewService.getReviewById(id);
        if (review == null) {
            result.setCode(1);
            result.setMsg("查询的id不存在");
        } else {
            result.setMsg("success");
            result.setCode(0);
            result.setData(review);
        }
        return result;
    }

    @GetMapping("/review/merchantId/{merchantId}")
    public Result getByMerchantId(@PathVariable("merchantId") int merchantId) {
        List<Review> reviews = reviewService.getReviewByMerchantId(merchantId);

        Result result = Result.ok();
        result.setData(reviews);

        return result;
    }

    @GetMapping("/review/status/{reviewStatus}")
    public Result getByReviewStatus(@PathVariable("reviewStatus") int reviewStatus) {

        List<Review> reviews = reviewService.getReviewByStatus(reviewStatus);
        Result result = Result.ok();
        result.setData(reviews);

        return result;
    }

    @GetMapping("/review/list")
    public Result getAll() {
        List<Review> reviews = reviewService.getAll();

        Result result = Result.ok();
        result.setData(reviews);

        return result;
    }

    @PostMapping("/review/pass/{id}")
    public Result pass(@PathVariable("id") int id) {
        //TODO: 鉴权,确保是管理员审核的

        if (reviewService.updateStatusById(id, Review.STATUS_TYPE_PASSED)) {
            // 审核通过之后，生成预付卡 和 预付卡商户关系表 记录
            Review review = reviewService.getReviewById(id);  // 通过 id 查询通过的审核记录
            Card card = new Card();
            card.setMerchantId(review.getMerchantId());
            card.setReviewId(id);
            card.setWalletId(review.getWalletId());
            card.setCardName(review.getCardName());
            card.setCardType(review.getCardType());
            card.setCardInfo(review.getCardInfo());
            card.setCardAmount(review.getCardAmount());
            card.setGiftAmount(review.getGiftAmount());
            card.setDiscountRate(review.getDiscountRate());
            // 插入 预付卡类型表 Card  预付卡产品的记录
            return cardService.insertCard(card);
            // todo 应该在用户购卡之后生成 插入 商户预付卡关系表 merchantCard 记录
//            return cardService.insertMerchantCard(card.getMerchantId(), card.getId());

        }
        return new Result(1, "failed", null);
    }

    @PostMapping("/review/reject/{id}")
    public Result reject(@PathVariable("id") int id){
        //TODO: 鉴权,确保是管理员审核的
        if(reviewService.updateStatusById(id,Review.STATUS_TYPE_FAILED)){
            return Result.ok();
        }
        return new Result(1,"failed",null);
    }

}
