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
@TableName("t_repo")
public class Repo implements Serializable {
    private static final long serialVersionUID = 4027375481837064662L;


    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
//    防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String depositoryName;//'仓库名称',
    private String depositoryAdd;//'仓库地址（是不是要分开存储）',
    private Double totalCapacity;//'仓库总容量',
    private Double residualCapacity;//'仓库剩余容量',
    private Long userId;//'仓库所有人id',
    private String company;// '仓库所属企业（公司）',
    private String depositoryContacts;//'仓库联系人',
    private String depositoryTell;//'仓库电话',
    private String notes;
    private String coordinate;//仓库位置坐标
}
