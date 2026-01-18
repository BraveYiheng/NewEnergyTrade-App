package com.yiheng.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiheng.springboot.domain.Feedback;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {

}
