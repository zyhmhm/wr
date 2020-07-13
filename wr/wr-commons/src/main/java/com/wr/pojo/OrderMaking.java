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

@Data
@Accessors(chain = true)
@TableName("t_ordermaking")
public class OrderMaking implements Serializable {
    private static final long serialVersionUID = 3331956003816759388L;


    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
//    防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String invoiceUrl;// '发票',
    private String contractUrl;//'合同',
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date validityPeriod;//'仓单有效期',
    private String paymentVoucher;//'付款凭证',
    //1.提交制单申请待审核；2.已审核未通过；3.已审核通过正在制单；4.制单完成已发放待兑付
    // 5.已发起兑付申请待审核//6.已审核未通过
    // 7.已审核通过待出库//8.仓库已确认出库
    // 9.货主已确认出库（完成）
    private Integer state;//'状态',
    private String notes;//'备注',
    @JsonSerialize(using = ToStringSerializer.class)
    private Long appointmentId;//'预约单id',
    private String appointmentNumber;//预约单编号',
    private String goodsName;// '货物名称',
    private String shipper;//'存货单位或者个人',
    private String shipperTell;// '存货方电话',
    private String company;// '存储企业',
    private String depositoryName;//'仓库名称',
    @JsonSerialize(using = ToStringSerializer.class)
    private Long depositoryId;//'仓库Id(和预约单Id一样为了以防万一)',
    private String depositoryContacts;//'仓库联系人',
    private String depositoryTell;//'仓库联系电话',
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;//用户id

}
