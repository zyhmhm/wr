package com.wr.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@TableName("t_electronic_warehouse")
public class ElectronicWarehouse implements Serializable {

    private static final long serialVersionUID = 4669723443717261945L;


    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
//    防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String inventor;//存货人（单位全称）',
    private String iAddr;//存货人地址',
    private String custodian;//'保管人（单位全称）',
    private String cAddr;// '保管仓地址',
    private String goodsCategory;// '货物品类',
    private String contractNumber;//'仓储合同号',
    private String rate;// '费率',
    private Integer isClean;//'清洁仓单（1是/0否）',
    private String goodsName;//'货物名称',
    private String manufacturer;//'生产厂商',
    private String lossStandard;//'货物损耗标准',
    private String goodsEncoding;// '商品编码',
    private String packing;//'包装',
    private Long number;//'数量',
    private String unit;//'单位',
    private Double weight;//'重量',
    private String storehouse;//'库位',
    private String receiptValue;//'仓单货值（大写）',
    private Double receiptValues;//'仓单货值（小写）',
    private String qualityOrganization;//'质检单位',
    private String qualityNumber;//'质检报告单号',
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date effectiveDates;//'仓单有效期',
    private String insurance;// '保险险种',
    private String insuranceNumber;//'保险单号',
    private String notes;// '备注',
    private String documentMaker;// '仓单制单人',
    private String checkPerson;//'复核记账人',
    private Integer isSeal;// '保管人（单位）盖章(1盖章/0没有盖章)',
    @JsonSerialize(using = ToStringSerializer.class)
    private Long warehouseMarkId;//仓储物标记表id',
    @JsonSerialize(using = ToStringSerializer.class)
    private Long appointmentId; //预约单Id,
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;//用户id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long cashId;//对付申请Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long repoId;//仓库Id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ordermakingId; //制单申请Id
    private Integer state; // 状态——————————1.完成制单待兑付
    // 2.已发起兑付申请待审核//3.已审核未通过
    // 4.已审核通过待出库//5.仓库已确认出库
    // 6.货主已确认出库（完成）
    // 7.发起转让申请 //8.受让方已经确认 //9.仓库放确认转让
    // 10.发起转让申请 //11.受让方已经确认 //12.仓库放确认转让
    // 13.发起转让申请 //14.受让方已经确认 //15.仓库放确认转让
    @JsonSerialize(using = ToStringSerializer.class)
    private Long position;
}
