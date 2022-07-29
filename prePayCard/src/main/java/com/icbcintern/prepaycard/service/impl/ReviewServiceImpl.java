package com.icbcintern.prepaycard.service.impl;

import com.icbcintern.prepaycard.mapper.ReviewMapper;
import com.icbcintern.prepaycard.pojo.Review;
import com.icbcintern.prepaycard.service.ReviewService;
import com.icbcintern.prepaycard.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description:
 * @author: He Yihui
 * @create: 2022-07-27 14:36
 **/
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewMapper reviewMapper;

    @Override
    public Result insertReview(Review review) {
        reviewMapper.insertReview(review);
        Result result = Result.ok();
        result.setData(review);
        return result;
    }

    @Override
    public Review getReviewById(Integer id) {
        return reviewMapper.getReviewById(id);
    }

    @Override
    public List<Review> getReviewByMerchantId(Integer merchantId) {
        return reviewMapper.getReviewByMerchantId(merchantId);
    }

    @Override
    public List<Review> getReviewByStatus(Integer reviewStatus) {
        return reviewMapper.getReviewByStatus(reviewStatus);
    }

    @Override
    public List<Review> getAll() {
        return reviewMapper.getAll();
    }

    @Override
    public boolean updateReviewById(Review review) {
        int effectNum = reviewMapper.updateReviewById(review);
        return effectNum>0;
    }

    @Override
    public boolean updateStatusById(Integer id, Integer reviewStatus) {
        int effectNum= reviewMapper.updateStatusById(id,reviewStatus);
        return effectNum>0;
    }
}
