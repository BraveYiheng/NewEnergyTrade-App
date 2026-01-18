package com.yiheng.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiheng.springboot.domain.Evaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface EvaluationMapper extends BaseMapper<Evaluation> {
    @Select("SELECT e.*, u1.company_name as fromUserCompanyName, u2.company_name as toUserCompanyName, o.order_number as orderNumber " +
            "FROM evaluation e " +
            "LEFT JOIN user u1 ON e.from_user_id = u1.user_id " +
            "LEFT JOIN user u2 ON e.to_user_id = u2.user_id " +
            "LEFT JOIN `order` o ON e.order_id = o.order_id " +
            "WHERE e.from_user_id = #{fromUserId}")
    List<Evaluation> getEvaluationByFromUserId(Integer fromUserId);

    // 根据被评价用户ID（toUserId）查询评价信息及关联的用户和订单信息
    @Select("SELECT e.*, u1.company_name as fromUserCompanyName, u2.company_name as toUserCompanyName, o.order_number as orderNumber " +
            "FROM evaluation e " +
            "LEFT JOIN user u1 ON e.from_user_id = u1.user_id " +
            "LEFT JOIN user u2 ON e.to_user_id = u2.user_id " +
            "LEFT JOIN `order` o ON e.order_id = o.order_id " +
            "WHERE e.to_user_id = #{toUserId}")
    List<Evaluation> getEvaluationByToUserId(Integer toUserId);
}
