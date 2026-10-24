package com.ch.environment.Entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
@TableName("projectapplication")
@Data
public class Projectapplication {
    @TableId("projectid")
    private Integer projectid;
    private Integer userid;
    private String username;
    private String projectname;
    private Integer points;
    private String projectdesc;
    private String status;
    private String imagepath;
    private Date createtime;
    private Date updatetime;
}
