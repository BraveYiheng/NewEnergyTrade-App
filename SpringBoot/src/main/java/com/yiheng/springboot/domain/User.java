package com.yiheng.springboot.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;//自动生成get，set，tostring
import java.util.Date;

@Data
public class User {
    @TableId(type = IdType.AUTO)
    private Integer userId;
    private String companyName;
    private String contactPerson;
    private String contactNumber;
    private String email;
    private String password;
    private Integer role;
    private Date createTime;
}