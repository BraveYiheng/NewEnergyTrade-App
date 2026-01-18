package com.yiheng.springboot.service;

import com.yiheng.springboot.domain.Evaluation;
import com.yiheng.springboot.domain.Product;

import java.util.List;

public interface EvaluationService {
    List<Evaluation> getEvaluationByFromUserId(Integer fromUserId);
     List<Evaluation> getEvaluationByToUserId(Integer fromUserId);
    public boolean addEvaluation(Evaluation evaluation);
    public boolean deleteEvaluationById(Integer evaluationId);
}
