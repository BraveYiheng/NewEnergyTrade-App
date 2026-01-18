package com.yiheng.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiheng.springboot.domain.Evaluation;
import com.yiheng.springboot.mapper.EvaluationMapper;
import com.yiheng.springboot.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EvaluationServiceImpl implements EvaluationService {
     @Autowired
    EvaluationMapper evaluationMapper;
    @Override
    public List<Evaluation> getEvaluationByFromUserId(Integer fromUserId) {
        return evaluationMapper.getEvaluationByFromUserId(fromUserId);
    }
    public List<Evaluation> getEvaluationByToUserId(Integer toUserId) {
        return evaluationMapper.getEvaluationByToUserId(toUserId);
    }
    public boolean addEvaluation(Evaluation evaluation) {
        return evaluationMapper.insert(evaluation) > 0;
    }
    public boolean deleteEvaluationById(Integer evaluationId) {
        return evaluationMapper.deleteById(evaluationId) > 0;
    }


}
