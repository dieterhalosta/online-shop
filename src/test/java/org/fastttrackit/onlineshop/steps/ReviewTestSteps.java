package org.fastttrackit.onlineshop.steps;

import org.fastttrackit.onlineshop.service.ReviewService;
import org.fastttrackit.onlineshop.transfer.review.ReviewResponse;
import org.fastttrackit.onlineshop.transfer.review.SaveReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

@Component
public class ReviewTestSteps {

    @Autowired
    private ReviewService reviewService;

    public ReviewResponse createReview(){
        SaveReviewRequest request = new SaveReviewRequest();
        request.setContent("Bla Bla Bla Review Test");

        ReviewResponse review = reviewService.createReview(request);

        assertThat(review, notNullValue());
        assertThat(review.getId(), greaterThan(0L));
        assertThat(review.getContent(), is(request.getContent()));

        return review;
    }

}
