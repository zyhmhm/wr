package com.wr.feignclient;

import com.wr.feignclient.fb.ElectronicWarehouseFeignServiceFB;
import com.wr.pojo.ElectronicWarehouse;
import com.wr.util.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ordermaking-service",fallback = ElectronicWarehouseFeignServiceFB.class)
public interface ElectronicWarehouseFeignService {
    /**
     * 根据Id查询当前仓单状态
     * @return
     */
    @GetMapping("/getStateById")
    JsonResult getStateById(@RequestParam Long electronicWarehouseId);

    @GetMapping("/doChangeEleWareStateById")
    JsonResult doChangeEleWareStateById(@RequestParam Long electronicWarehouseId, @RequestParam Integer state, @RequestParam Long cashId);
    @GetMapping("doChangeEleStateByCashId")
    JsonResult doChangeEleStateByCashId(@RequestParam Long cashId, @RequestParam Integer state);
    /**
     * 跟新状态根据entity还要改变仓单的所有人Id
     * 用语转让等业务
     */
    @PostMapping("doChangeByEntity")
    JsonResult doChangeByEntity(@RequestBody ElectronicWarehouse entity);
}
