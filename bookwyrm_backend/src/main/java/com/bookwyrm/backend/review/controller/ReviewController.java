package com.bookwyrm.backend.review.controller;

import com.bookwyrm.backend.review.dao.ReviewDao;
import com.bookwyrm.backend.review.dao.ReviewService;
import com.bookwyrm.backend.review.input.ReviewUploadInput;
import com.bookwyrm.backend.review.payload.ReviewUploadPayload;
import com.bookwyrm.backend.review.validator.ReviewValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    ReviewService reviewService;

    @CrossOrigin
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ReviewUploadPayload> createReview(
            @RequestBody ReviewUploadInput reviewUploadInput) {
        ReviewUploadPayload response = new ReviewUploadPayload();

        List<String> errorList = ReviewValidator.validateUploadInformation(reviewUploadInput);
        HttpStatus status = HttpStatus.OK;

        if (errorList.isEmpty()) {
            ReviewDao reviewDao = new ReviewDao(
                    reviewUploadInput.getBookId(),
                    reviewUploadInput.getAuthor(),
                    reviewUploadInput.getAnonymousFlag(),
                    reviewUploadInput.getContent());
            reviewService.save(reviewDao);
        } else {
            response.setMessages(errorList);
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(response);
    }
}
