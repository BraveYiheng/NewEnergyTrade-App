package com.yiheng.springboot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.util.Date;
@Data
public class Evaluation { //评估
    @TableId(type = IdType.AUTO)
    private Integer evaluationId;
    private Integer orderId;
    private Integer fromUserId;
    private Integer toUserId;
    private Integer starRating;  //星级
    private String comment;
    private Date createTime;  //前端显示时间
    @TableField(exist = false)
    private String fromUserCompanyName;
    @TableField(exist = false)
    private String toUserCompanyName;
    @TableField(exist = false)
    String orderNumber;

}
