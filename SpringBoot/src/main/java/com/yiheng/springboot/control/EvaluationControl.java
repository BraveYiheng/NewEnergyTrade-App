package com.yiheng.springboot.control;

import com.yiheng.springboot.domain.Evaluation;
import com.yiheng.springboot.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/evaluation")
public class EvaluationControl{
    @Autowired
    EvaluationService evaluationService;

    @GetMapping("/fromUser/{fromUserId}")
    public  ResponseEntity<List<Evaluation>> getEvaluationByFromUserId(@PathVariable Integer fromUserId){
        List<Evaluation> evaluationByFromUserId = evaluationService.getEvaluationByFromUserId(fromUserId);
        return  new ResponseEntity<>(evaluationByFromUserId, HttpStatus.OK);
    }
    @GetMapping("toUser/{toUserId}")
    public  ResponseEntity<List<Evaluation>> getEvaluationByToUserId(@PathVariable Integer toUserId){
        List<Evaluation> evaluationByToUserId = evaluationService.getEvaluationByToUserId(toUserId);
        return  new ResponseEntity<>(evaluationByToUserId, HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<Boolean> addEvaluation(@RequestBody Evaluation evaluation) {
        boolean result = evaluationService.addEvaluation(evaluation);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @DeleteMapping("/{evaluationId}")
    public ResponseEntity<Boolean> deleteEvaluation(@PathVariable Integer evaluationId) {
        boolean result = evaluationService.deleteEvaluationById(evaluationId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



}
