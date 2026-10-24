package com.ch.environment.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

//这个实体类对应数据库的user表
@TableName("user")
@Data
public class User {
    @TableId("userid")
    private Integer userid;
    private String username;
    private String password;
    private String department;
    private String classname;
    private String status;
    private int totalpoint;
    @TableField(exist = false)
    private String code;

    @Override
    public String toString() {
        return "User{" +
                "userid=" + userid +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                ", classname='" + classname + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
