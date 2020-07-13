package com.wr.controller;

import com.wr.pojo.ElectronicWarehouse;
import com.wr.service.ElectronicWarehouseService;
import com.wr.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin
@RestController
public class ElectronicWarehouseController {
    @Autowired
    private ElectronicWarehouseService electronicWarehouseService;

    /**
     * 对仓单进行分页查询——————货主方
     */
    /**
     * 根据Id查询电子仓单的信息
     */
    @GetMapping("getEleWarePage")
    public JsonResult getEleWarePage(String inventor, Integer currentPage,Integer pageSize,Integer state){
        return electronicWarehouseService.getEleWarePage(inventor,currentPage,pageSize,state);
    }

    @GetMapping("/getEleWareInfoById")
    public JsonResult getEleWareInfoById(Long id){
        return electronicWarehouseService.getEleWareInfoById(id);
    }

//    ---------------------------------------------------------------------
    /**
     * 根据Id查询当前仓单状态
      * @return
     */
    @GetMapping("getStateById")
    public JsonResult getStateById(Long electronicWarehouseId){
        return electronicWarehouseService.getStateById(electronicWarehouseId);
    }

    @GetMapping("doChangeEleWareStateById")
    public JsonResult doChangeEleWareStateById(Long electronicWarehouseId,Integer state,Long cashId){
        return electronicWarehouseService.doChangeEleWareStateById(electronicWarehouseId,state,cashId);
    }
    @GetMapping("doChangeEleStateByCashId")
    public JsonResult doChangeEleStateByCashId(Long cashId,Integer state){
        return electronicWarehouseService.doChangeEleStateByCashId(cashId,state);
    }
    @GetMapping("getEleWareInfoByTypeId")
    public JsonResult getEleWareInfoByTypeId(String type,Long id){
        return electronicWarehouseService.getEleWareInfoByTypeId(type,id);
    }
    /**
     * 跟新状态根据entity还要改变仓单的所有人Id
     * 用语转让等业务
     */
    @PostMapping("doChangeByEntity")
    public JsonResult doChangeByEntity(@RequestBody ElectronicWarehouse entity){
        return electronicWarehouseService.doChangeByEntity(entity);
    }

    /**
     * 查找基本信息
     *  用作制作仓单时自动生成
     */
    @GetMapping("doGetEleWareInfoToMake")
    public JsonResult dogetEleWareInfoToMake(Long appointmentId,Long repoId,Long orderMakingId){
        return electronicWarehouseService.doGetEleWareInfoToMake(appointmentId,repoId,orderMakingId);
    }
}
