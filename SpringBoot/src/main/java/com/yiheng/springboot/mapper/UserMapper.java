package com.yiheng.springboot.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yiheng.springboot.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM user WHERE email = #{email}")
    User selectByEmail(String email);
    
}
