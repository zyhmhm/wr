package com.wr.vo;

import com.wr.pojo.ElectronicWarehouse;
import com.wr.pojo.StorageMark;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 查询的时候的VO
 */
@Data
@Accessors(chain = true)
public class ElectronicWarehouseVo {
    private ElectronicWarehouse electronicWarehouse;
    private StorageMark storageMark;
}
