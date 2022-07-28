package com.icbcintern.prepaycard.controller;

import com.icbcintern.prepaycard.pojo.Review;
import com.icbcintern.prepaycard.service.ReviewService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description:审核controller层
 * @author: He Yihui
 * @create: 2022-07-27 14:34
 **/
@RestController
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @PostMapping("/review/submit")
    public Result submit(@RequestBody Review review) {
        //TODO: 校验提交者的商户id是否与审核表中一致

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
    public Result pass(@PathVariable("id") int id){
        //TODO: 鉴权,确保是管理员审核的
        if(reviewService.updateStatusById(id,Review.STATUS_TYPE_PASSED)){
            return Result.ok();
        }
        return new Result(1,"failed",null);
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
