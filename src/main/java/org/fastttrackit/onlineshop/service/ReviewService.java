package org.fastttrackit.onlineshop.service;

import org.fastttrackit.onlineshop.domain.Review;
import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.persistance.ReviewRepository;
import org.fastttrackit.onlineshop.transfer.review.ReviewResponse;
import org.fastttrackit.onlineshop.transfer.review.SaveReviewRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PutMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewService.class);

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    //ToDo: create all CRUD methods

    public ReviewResponse createReview (SaveReviewRequest request){
        LOGGER.info("Creating review {}", request);
        Review review = new Review();
        review.setContent(request.getContent());

        Review savedReview = reviewRepository.save(review);

        return mapReviewResponse(savedReview);
    }

    public Page<ReviewResponse> getReviews(long productId, Pageable pageable){

        Page<Review> page = reviewRepository.findByProductId(productId, pageable);

        List<ReviewResponse> reviewDtos = new ArrayList<>();
        for (Review review : page.getContent()){
            ReviewResponse reviewResponse = mapReviewResponse(review);
            reviewDtos.add(reviewResponse);
        }

        return new PageImpl<>(reviewDtos, pageable, page.getTotalElements());
    }

    public ReviewResponse getReviewResponse(long id){
        LOGGER.info("Retrieving review {}", id);

        Review review = getReview(id);
        return mapReviewResponse(review);
    }

    public Review getReview (long id){
        return reviewRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review " + id + " not found."));
    }


    public ReviewResponse updateReview(long id, SaveReviewRequest request){
        LOGGER.info("Updating review {}: {}", id, request);

        Review review = getReview(id);
        BeanUtils.copyProperties(request, review);

        Review updatedReview = reviewRepository.save(review);

        return mapReviewResponse(updatedReview);
    }

    public void deleteReview(long id){
        LOGGER.info("Deleting review {}", id);
        reviewRepository.deleteById(id);
    }

    private ReviewResponse mapReviewResponse(Review review){
        ReviewResponse reviewResponse = new ReviewResponse();
        reviewResponse.setId(review.getId());
        reviewResponse.setContent(review.getContent());

        return reviewResponse;

    }
}
