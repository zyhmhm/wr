package com.wr.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName("t_storage_mark")
@Data
@Accessors(chain = true)
public class StorageMark implements Serializable {
    private static final long serialVersionUID = 8355912630496550270L;

    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
//    防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String brand;//品牌',
    private String specifications;//'规格',
    private String model;//'型号',
    private String batchNumber;//'生产批号',
    private String productionAdd; //'产地',
    private String identifier;//'识别号',
}
