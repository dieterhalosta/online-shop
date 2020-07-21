package org.fastttrackit.onlineshop.web;


import org.fastttrackit.onlineshop.service.ReviewService;
import org.fastttrackit.onlineshop.transfer.product.ProductResponse;
import org.fastttrackit.onlineshop.transfer.review.GetReviewRequest;
import org.fastttrackit.onlineshop.transfer.review.ReviewResponse;
import org.fastttrackit.onlineshop.transfer.review.SaveReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody SaveReviewRequest request){
        ReviewResponse review = reviewService.createReview(request);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ReviewResponse> updateReview (@PathVariable long id, @Valid @RequestBody SaveReviewRequest request){
//        ReviewResponse reviewResponse = reviewService.updateReview(id, request);
//        return new ResponseEntity<>(reviewResponse, request);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReview (@PathVariable long id){
        ReviewResponse review = reviewService.getReviewResponse(id);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<Page<ReviewResponse>> getReviews (@Valid GetReviewRequest request, Pageable pageable){
//        reviewService.getReviews(request, pageable);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview (@PathVariable long id){
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
