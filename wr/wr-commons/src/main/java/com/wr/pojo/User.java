package com.wr.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {
    private static final long serialVersionUID = -4240328495084529097L;


    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
//    防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String userName;
    private String password;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long repoId;
}
