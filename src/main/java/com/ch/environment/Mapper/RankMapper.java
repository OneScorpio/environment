package com.ch.environment.Mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.ch.environment.Entity.Urank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import static com.baomidou.mybatisplus.extension.toolkit.Db.updateBatchById;

@Mapper
public interface RankMapper extends BaseMapper<Urank> {
    @Update("UPDATE urank ur SET ur.totalpoint = "+
            " (SELECT COALESCE(SUM(ush.points),0) FROM userscorehistory ush WHERE ush.userid = ur.userid)"+
            " WHERE ur.userid = #{userid}")
    int  updateTotalPoint(@Param("userid") int userid);
    default List<Urank> selectAllByPoint(){
        return selectList(new QueryWrapper<Urank>().orderByDesc("totalpoint"));
    }
    default boolean updateRanks(List<Urank> uranks){
        return updateBatchById(uranks);
    }
}
