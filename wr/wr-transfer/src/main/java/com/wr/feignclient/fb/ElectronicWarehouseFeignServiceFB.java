package com.wr.feignclient.fb;

import com.wr.feignclient.ElectronicWarehouseFeignService;
import com.wr.pojo.ElectronicWarehouse;
import com.wr.util.JsonResult;
import org.springframework.stereotype.Component;

@Component
public class ElectronicWarehouseFeignServiceFB implements ElectronicWarehouseFeignService {
    @Override
    public JsonResult getStateById(Long electronicWarehouseId) {
        return null;
    }

    @Override
    public JsonResult doChangeEleWareStateById(Long electronicWarehouseId, Integer state,Long cashId) {
        return null;
    }

    @Override
    public JsonResult doChangeEleStateByCashId(Long cashId, Integer state) {
        return null;
    }

    @Override
    public JsonResult doChangeByEntity(ElectronicWarehouse entity) {
        return JsonResult.err();
    }
}
