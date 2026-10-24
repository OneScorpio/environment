package com.ch.environment.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ch.environment.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends  BaseMapper<User> {
    @Update("UPDATE user u SET u.totalpoint = " +
            "(SELECT COALESCE(SUM(ush.points),0) FROM userscorehistory ush WHERE ush.userid = u.userid) " +
            "WHERE u.userid = #{userid}")
            int updateTotalPoint(@Param("userid") int userid);
    //todo
}




//select SUM(points) as sum_points  from  userscorehistory where userid='126' GROUP BY userid;