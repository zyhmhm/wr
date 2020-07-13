package com.wr.feignclient;

import com.wr.feignclient.fb.ElectronicWarehouseFeignServiceFB;
import com.wr.pojo.ElectronicWarehouse;
import com.wr.util.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ordermaking-service",fallback = ElectronicWarehouseFeignServiceFB.class)
public interface ElectronicWarehouseFeignService {

 /**
     * 跟新状态根据entity还要改变仓单的所有人Id
     * 用语转让等业务
     */
    @PostMapping(value = "doChangeByEntity",consumes = "application/json")
    JsonResult doChangeByEntity(ElectronicWarehouse entity);
}
