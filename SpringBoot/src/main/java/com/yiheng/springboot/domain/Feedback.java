package com.yiheng.springboot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Feedback {    //意见反馈
    @TableId(type = IdType.AUTO)
    private Integer feedbackId;
    private Integer userId;
    private String content;
    private Date createTime;
    @TableField(exist = false)
    private User user;
}
