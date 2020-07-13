package com.wr.controller;

import com.wr.pojo.Pledge;
import com.wr.service.PledgeService;
import com.wr.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pledge")
public class PledgeController {
    @Autowired
    private PledgeService pledgeService;

    /**
     * 对质押申请进行分页查询，state是筛选条件
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @GetMapping("getPledgePage")
    public JsonResult getPledgePage(Integer currentPage, Integer pageSize, Integer state){
        return pledgeService.getPledgePage(currentPage,pageSize,state);
    }
    /**
     * 保存质押申请
     */
    @PostMapping("doSavePledge")
    public JsonResult doSavePledge(Pledge entity){
        return pledgeService.doSavePledge(entity);
    }
    /**
     * 改变质押状态
     *  根据entity改变，同时也需要判断是不是需要改变仓单的状态
     */

    @PostMapping("doChangeStateByEntity")
    public JsonResult doChangeStateByEntity(Pledge entity){
        return pledgeService.doChangeStateByState(entity);
    }

    /**
     * 分页查询针对于仓库方，
     * 需要根据仓库的Id进行查询
     */
    @GetMapping("doPledgePageToRepo")
    public JsonResult doPledgePageToRepo(Integer currentPage, Integer pageSize){
        return pledgeService.doPledgePageToRepo(currentPage,pageSize);
    }
}
