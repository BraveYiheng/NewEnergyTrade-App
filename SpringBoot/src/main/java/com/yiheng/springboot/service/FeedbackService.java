package com.yiheng.springboot.service;
import com.yiheng.springboot.domain.Feedback;

import java.util.List;


public interface FeedbackService {
    List<Feedback> getFeedbacksByUserId(Integer userId);
    boolean addFeedback(Feedback feedback);
    public boolean deleteFeedback(Integer feedbackId);
}
