package com.yiheng.springboot.control;

import com.yiheng.springboot.domain.Feedback;
import com.yiheng.springboot.domain.Product;
import com.yiheng.springboot.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
public class FeedbackControl {
    @Autowired
    FeedbackService feedbackService;

    @GetMapping("user/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByUserId(@PathVariable Integer userId) {
        List<Feedback> feedbacksByUserId = feedbackService.getFeedbacksByUserId(userId);
        return new ResponseEntity<>(feedbacksByUserId, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Boolean> addFeedback(@RequestBody Feedback feedback) {
        boolean result = feedbackService.addFeedback(feedback);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @DeleteMapping("/{feedbackId}")
    public ResponseEntity<Boolean> deleteFeedback(@PathVariable Integer feedbackId) {
        boolean result = feedbackService.deleteFeedback(feedbackId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
