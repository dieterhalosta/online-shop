package org.fastttrackit.onlineshop;

import org.fastttrackit.onlineshop.exception.ResourceNotFoundException;
import org.fastttrackit.onlineshop.service.ReviewService;
import org.fastttrackit.onlineshop.steps.ReviewTestSteps;
import org.fastttrackit.onlineshop.transfer.review.GetReviewRequest;
import org.fastttrackit.onlineshop.transfer.review.ReviewResponse;
import org.fastttrackit.onlineshop.transfer.review.SaveReviewRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import javax.validation.ConstraintViolationException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReviewServiceIntegrationTests {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewTestSteps reviewTestSteps;

    @Test
    void creatReview_whenValidRequest_thenReturnCreatedReview () {
        reviewTestSteps.createReview();
    }

    @Test
    void createReview_whenMissingMandatoryProperties_thenThrowException() {
        SaveReviewRequest request = new SaveReviewRequest();

        try {
            reviewService.createReview(request);
        } catch (Exception e) {
            assertThat("Unexpected exception thrown.", e instanceof ConstraintViolationException);
        }
    }

    @Test
    void getReview_whenExistingReview_thenReturnReview(){
        ReviewResponse review = reviewTestSteps.createReview();

        ReviewResponse response = reviewService.getReviewResponse(review.getId());

        assertThat(response, notNullValue());
        assertThat(response.getId(), is(review.getId()));
        assertThat(response.getContent(), is(review.getContent()));
    }

    @Test
    void getReview_whenNonExistingReview_thenThrowException(){
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> reviewService.getReviewResponse(0));
    }

    @Test
    void updateReview_whenValidRequest_thenReturnUpdatedReview(){
        ReviewResponse review = reviewTestSteps.createReview();

        SaveReviewRequest request = new SaveReviewRequest();
        request.setContent(review.getContent() + " with an update from the test");

        ReviewResponse updatedReview = reviewService.updateReview(review.getId(), request);

        assertThat(updatedReview, notNullValue());
        assertThat(updatedReview.getId(), is(review.getId()));
        assertThat(updatedReview.getContent(), is(request.getContent()));
    }

    @Test
    void deleteReview_whenExistingReview_thenReviewDoesNotExistAnymore(){
        ReviewResponse review = reviewTestSteps.createReview();

        reviewService.deleteReview(review.getId());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.getReviewResponse(review.getId()));
    }

}
