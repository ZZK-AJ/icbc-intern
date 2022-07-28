package com.icbcintern.prepaycard.service;

import com.icbcintern.prepaycard.pojo.Review;
import com.icbcintern.prepaycard.utils.Result;

import java.util.List;

public interface ReviewService {

    Result insertReview(Review review);

    Review getReviewById(Integer id);

    List<Review> getReviewByMerchantId(Integer merchantId);

    List<Review> getReviewByStatus(Integer reviewStatus);

    List<Review> getAll();

    boolean updateReviewById(Review review);

    boolean updateStatusById(Integer id, Integer reviewStatus);
}
