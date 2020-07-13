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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("t_transfer")
public class Transfer implements Serializable {
    private static final long serialVersionUID = 1351929457955091075L;

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
    private String company;
    private String repoName;
    private String goodsName;
    private String reason;
    // 1.创建转让申请，2.受让方驳回，3.受让方确认，4.仓库方驳回，5.仓库方确认
    private Integer state;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    private String repoNotes;
    private String assigneeNotes;
}
