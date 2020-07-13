package com.wr.controller;

import com.wr.pojo.Escort;
import com.wr.service.EscortService;
import com.wr.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/escort")
public class EscortController {
    @Autowired
    private EscortService escortService;

    /**
     * 对解押申请进行分页查询，state是筛选条件
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @GetMapping("getEscortPage")
    public JsonResult getPledgePage(Integer currentPage, Integer pageSize, Integer state){
        return escortService.getEscortPage(currentPage,pageSize,state);
    }
    /**
     * 保存解押申请
     */
    @PostMapping("doSaveEscort")
    public JsonResult doSavePledge(Escort entity){
        return escortService.doSaveEscort(entity);
    }
    /**
     * 改变解押状态
     *  根据entity改变，同时也需要判断是不是需要改变仓单的状态
     */

    @PostMapping("doChangeStateByEntity")
    public JsonResult doChangeStateByEntity(Escort entity){
        return escortService.doChangeStateByState(entity);
    }

    /**
     * 分页查询针对于仓库方，
     * 需要根据仓库的Id进行查询
     */
    @GetMapping("doEscortPageToRepo")
    public JsonResult doEscortPageToRepo(Integer currentPage, Integer pageSize){
        return escortService.doEscortPageToRepo(currentPage,pageSize);
    }
}
