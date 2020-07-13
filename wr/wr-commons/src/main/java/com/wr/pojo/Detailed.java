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

@Data
@Accessors(chain = true)
@TableName("t_detailed")
@AllArgsConstructor
@NoArgsConstructor
public class Detailed implements Serializable {
    private static final long serialVersionUID = 2274231034928126298L;
    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
//    防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String goodsName;//'商品名称',
    private String manufacturer;//'生产厂商',
    private String encoding;//商品编码',
    private String packinng;//'包装',
    private Integer number;//'商品数量',
    private String unit;//'单位',
    private Double weight;//'重量',
    private Double volume;//'体积',
    private Double unitPrice;//'单价',
    private String notes;//'备注',
    private Integer isClear; // 是否是清洁仓单（0否1是）
    private Double length;
    private Double area;
    private Double totalPrice;// 总价
    @JsonSerialize(using = ToStringSerializer.class)
    private Long position;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long appointmentId;

    private String brand;//品牌',
    private String specifications;//'规格',
    private String model;//'型号',
    private String batchNumber;//'生产批号',
    private String productionAdd; //'产地',
}
