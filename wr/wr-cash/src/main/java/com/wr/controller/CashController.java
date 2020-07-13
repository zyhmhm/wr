package com.wr.controller;

import com.wr.pojo.Cash;
import com.wr.service.CashService;
import com.wr.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
public class CashController {
    @Autowired
    private CashService cashService;

    /**
     * 保存对付申请
     */
    @ResponseBody
    @PostMapping("doSaveCash")
    public JsonResult doSaveCash(Cash cash,Long electronicWarehouseId) {
        return cashService.doSaveCash(cash,electronicWarehouseId);
    }
    /**
     * 针对于仓库的对付申请分页条件查询
     */
    @GetMapping("doGetCashPage")
    public JsonResult doGetCashPage(String consigneeName, Integer currentPage,Integer pageSize){
        Long repoId = 1l;
        return cashService.doGetCashPage(consigneeName,currentPage,pageSize,repoId);
    }

    /**
     * 针对于仓促企业的分页查询
     */
    @GetMapping("doGetCashPageToShipper")
    public JsonResult doGetCashPageToShipper(String consigneeName, Integer currentPage,Integer pageSize){
        return cashService.doGetCashPageToShipper(consigneeName,currentPage,pageSize);
    }
    /**
     * 针对于仓储企业：改变状态，用于审核对付申请的方法
     */
    @PostMapping("doAcceptOrRefuse")
    public JsonResult doAcceptOrRefuse(Cash entity){
        return cashService.doAcceptOrRefuse(entity);
    }

}
