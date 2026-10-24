package com.ch.environment.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("urank")
@Data
public class Urank {
    @TableId("userid")
    private Integer userid;
    @TableField("username")
    private String username;
    private Integer totalpoint;
    private Integer ranksort;
    @TableField("updatetime")
    private Date updatetime;

    @Override
    public String toString() {
        return "User_rank{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", points=" + totalpoint +
                ", rank=" + ranksort +
                ", updatetime=" + updatetime +
                '}';
    }
}
