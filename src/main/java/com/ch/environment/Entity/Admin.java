package com.ch.environment.Entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("admin")
public class Admin {
    @TableId("adminid")
    private Integer adminid;//admin_id
    @TableField("adminname")
    private String adminname;
    private String password;
}
