package com.wr.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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

@TableName("t_appointment")
@Data
@Accessors(chain = true)
public class Appointment implements Serializable {
    private static final long serialVersionUID = 7080647102358983660L;

    // 使用数据库主键自增
    @TableId(value = "id",type = IdType.AUTO)
//    防止数据到达前端精度不够
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;//创建用户id
    private Long depositoryId;//仓库Id
    //忽略未知属性
    @TableField(exist = false)
    private String depositoryName;//仓库名称
    private String goodsName;//商品名称
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;//'创建时间',
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;//更新时间',
    //state---1、已保存未提交；2、已提交待审核；3、已审核未通过；
    // 4、已审核通过待指派入库；5、正在入库6、仓库方已确认入库
    //7、货主已确认入库；（完成入库）8、待制单、9已发起制单申请,//10\制单申请未通过
    //11、已通过正在制单；12、制单完成待兑付；13、已发起兑付申请；14、兑付申请已审核未通过
    //15、兑付申请已审核通过待出库；16、仓库已确认出库17、货主已确认出库
    private Integer state;//状态',
    private String auditNotes; //'审核备注',
    private String tell;//'联系电话',
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date storageTime;//'入库时间';
    private String appointmentNumber;//预约单单号
    private String contacts;//联系人姓名
    private String shipper;//货主名称
    private String shipperTell;//货主(存货方电话)

}
