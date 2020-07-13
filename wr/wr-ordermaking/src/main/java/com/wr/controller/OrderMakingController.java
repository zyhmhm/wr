package com.wr.controller;

import com.wr.pojo.OrderMaking;
import com.wr.service.OrderMakingService;
import com.wr.util.JsonResult;
import com.wr.vo.EleWareVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
@RequestMapping("/ordermaking")
public class OrderMakingController {
    @Autowired
    private OrderMakingService orderMakingService;

    @GetMapping("/getInfoById")
    public JsonResult getInfoById(Long appointmentId){
        return orderMakingService.getInfoById(appointmentId);
    }

    /**
     * 保存制单申请
     * @param orderMaking
     * @return
     */
    @PostMapping("/doSave")
    public JsonResult doSave(OrderMaking orderMaking){

        return orderMakingService.doSave(orderMaking);
    }

    /**
     * 根据货主进行制单申请的分页查询
     * @param shipper
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/doGetOrderMakingToRepo")
    public JsonResult doGetOrderMakingToRepo(String shipper,Integer currentPage,Integer pageSize){
        return orderMakingService.doGetOrderMakingToRepo(shipper,currentPage,pageSize);
    }
    /**
     * 货主的条件分页查询
     */
    @GetMapping("doGetOrderMakingToShipper")
    public JsonResult doGetOrderMakingToShipper(String shipper,Integer currentPage,Integer pageSize,Integer state) throws IllegalAccessException {
        return orderMakingService.doGetOrderMakingToShipper(shipper,currentPage,pageSize,state);
    }
    /**
     * 查寻制单申请详情根据ID
     */
    @GetMapping("doGetOrderMakingById")
    public JsonResult doGetOrderMakingById(Long id){
        return orderMakingService.doGetOrderMakingById(id);
    }
    @PostMapping("doAcceptOrRefuse")
    public JsonResult doAcceptOrRefuse(Long id,Integer state,String notes){
        return orderMakingService.doAcceptOrRefuse(id,state,notes);
    }

//    @PostMapping("doSaveEleWare")
//    public JsonResult doSaveEleWare(ElectronicWarehouse electronicWarehouse, StorageMark storageMark){
//        return orderMakingService.doSaveEleWare(electronicWarehouse,storageMark);
//    }
    @PostMapping("doSaveEleWare")
    public JsonResult doSaveEleWare(@RequestBody EleWareVo[] eleWareVoList){
        return orderMakingService.doSaveEleWareVo(eleWareVoList);
    }

    @GetMapping("doChangeStateById")
    public JsonResult doChangeStateById(Long orderMakingId,Integer state){
        return orderMakingService.doChangeStateById(orderMakingId,state);
    }
}
