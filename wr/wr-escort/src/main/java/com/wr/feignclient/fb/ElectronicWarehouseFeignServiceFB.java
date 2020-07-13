package com.wr.feignclient.fb;

import com.wr.feignclient.ElectronicWarehouseFeignService;
import com.wr.pojo.ElectronicWarehouse;
import com.wr.util.JsonResult;
import org.springframework.stereotype.Component;

@Component
public class ElectronicWarehouseFeignServiceFB implements ElectronicWarehouseFeignService {
    @Override
    public JsonResult doChangeByEntity(ElectronicWarehouse entity) {
        return null;
    }
}
