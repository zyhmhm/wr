package com.wr.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@TableName("t_cash")
@Data
@Accessors(chain = true)
public class Cash implements Serializable {

    private static final long serialVersionUID = 7080647102358983660L;

    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
//    防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String goodsName;//商品名称
    private String consigneeName;//提货人姓名
    private String consigneeTell;//提货人电话
    private String consigneeIdNumber;//提货人身份证号,
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date cashDate;//兑付日期
//    private Long cashWrId;//兑付的额电子仓单id
    private Integer state;//状态//1.已提交待审核//2.审核未通过//3.审核已通过//4.仓库方已确认出库//5.货主已确认出库
    private String notes;//审核备注
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;//用户id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long repoId;
}
