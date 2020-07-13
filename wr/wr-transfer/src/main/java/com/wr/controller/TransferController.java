package com.wr.controller;

import com.wr.pojo.Transfer;
import com.wr.service.TransferService;
import com.wr.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransferController {
    @Autowired
    private TransferService transferService;

    /**
     * 对转让申请进行分页查询，state是筛选条件
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @GetMapping("getTransferPage")
    public JsonResult getTransferPage(Integer currentPage,Integer pageSize,Integer state){
        return transferService.getTransferPage(currentPage,pageSize,state);
    }

    /**
     * 保存转让申请
     * @param entity
     * @return
     */
    @PostMapping("doSaveTransfer")
    public JsonResult doSaveTransfer(Transfer entity){
        return transferService.doSaveTransfer(entity);
    }

    /**
     * 改变转让申请状态
     * 受让人确认转让，仓库方确认转让都调用这个方法
     */
    @PostMapping("doChangeState")
    public JsonResult doChangeState(Transfer entity){
        return transferService.doChangeState(entity);
    }
    /**
     * 获取转让单据的分页信息、针对于仓库方
     * 需要根据repoId新型查询
     */
    @GetMapping("getTransferPageToRepo")
    public JsonResult getTransferPageToRepo(Integer currentPage,Integer pageSize,Integer state){
        return transferService.getTransferPageToRepo(currentPage, pageSize, state);
    }
}
