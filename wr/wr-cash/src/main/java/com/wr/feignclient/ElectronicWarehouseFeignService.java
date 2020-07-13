package com.wr.feignclient;

import com.wr.feignclient.fb.ElectronicWarehouseFeignServiceFB;
import com.wr.util.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
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
    JsonResult doChangeEleWareStateById(@RequestParam Long electronicWarehouseId,@RequestParam Integer state,@RequestParam Long cashId);
    @GetMapping("doChangeEleStateByCashId")
    public JsonResult doChangeEleStateByCashId(@RequestParam Long cashId,@RequestParam Integer state);
}
