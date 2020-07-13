package com.wr.vo;

import com.wr.pojo.ElectronicWarehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EleWareVo extends ElectronicWarehouse {
    private static final long serialVersionUID = -3687593546603063969L;

    private String brand;//品牌',
    private String specifications;//'规格',
    private String model;//'型号',
    private String batchNumber;//'生产批号',
    private String productionAdd; //'产地',
}
