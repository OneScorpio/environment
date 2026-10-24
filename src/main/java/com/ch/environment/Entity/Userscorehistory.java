package com.ch.environment.Entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("userscorehistory")
public class Userscorehistory {
    @TableId(value = "recordid",type = IdType.AUTO)
    private Integer recordid;
    private String  username;
    private Integer userid;
    private String projectdesc;
    private Integer points;
}
