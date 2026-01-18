package com.yiheng.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiheng.springboot.domain.Feedback;
import com.yiheng.springboot.mapper.FeedbackMapper;
import com.yiheng.springboot.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FeedbackImpl implements FeedbackService {
    @Autowired
    FeedbackMapper feedbackMapper;
    @Override
    public List<Feedback> getFeedbacksByUserId(Integer userId) {
        QueryWrapper<Feedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return feedbackMapper.selectList(queryWrapper);
    }

    @Override
    public boolean addFeedback(Feedback feedback) {
        return feedbackMapper.insert(feedback) > 0;
    }
    @Override
    public boolean deleteFeedback(Integer feedbackId) {
        return feedbackMapper.deleteById(feedbackId) > 0;
    }
}
