package com.wr.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_pledge")
public class Pledge implements Serializable {
    private static final long serialVersionUID = -4731899345736858821L;

    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
    // 防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long fromUserId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long toUserId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long electronicWarehouseId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long pledgeId;
    private String company;
    private String repoName;
    private String goodsName;
    private Integer state;
    private Date createTime;
    private String repoNotes;
    private String assigneeNotes;
    private String reason;
}
