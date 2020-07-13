package com.wr.controller;

import com.wr.pojo.Position;
import com.wr.pojo.Repo;
import com.wr.service.PositionService;
import com.wr.service.RepoService;
import com.wr.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repo")
public class RepoController {
    @Autowired
    private RepoService repoService;

    @Autowired
    private PositionService positionService;

    /**
     * 获取仓库的基本信息
     */
    @GetMapping("getRepoInfo")
    public JsonResult getRepoInfo(){
        return repoService.getRepoInfo();
    }

    /**
     * 查询仓库所用偶的的仓位
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @GetMapping("doGetPositionPage")
    public JsonResult doGetPositionPage(Integer currentPage,Integer pageSize,Integer state){
        return positionService.doGetPositionPage(currentPage,pageSize,state);
    }
    /**
     * 添加仓库
     */
    @PostMapping("doSaveRepo")
    public JsonResult doSaveRepo(Repo entity){
        return repoService.saveRepo(entity);
    }

    /**
     * 更新仓库信息
     * @param entity
     * @return
     */
    @PostMapping("doUpdateRepo")
    public JsonResult doUpdateRepo(Repo entity){
        return repoService.doUpdateRepo(entity);
    }

    /**
     * 针对于仓位的操作
     */
    /**
     * 修改仓位信息
     * @param entity
     * @return
     */
    @PostMapping("doUpdatePosition")
    public JsonResult doUpdatePosition(Position entity){
        return positionService.doUpdatePosition(entity);
    }

    /**
     * 新增仓位
     */
    @PostMapping("doSavePosition")
    public JsonResult doSavePosition(Position entity){
        return positionService.doSavePosition(entity);
    }

    /**
     * 获取当前所有未被使用的仓位
     */
    @GetMapping("/getNoUsedPosition")
    public JsonResult getNoUsedPosition(){
        return positionService.getNoUsedPosition();
    }
    @GetMapping("getPositionById")
    public JsonResult getPosition(Long positionId){
        return positionService.getPositionById(positionId);
    }

    @PostMapping("doChangeByEntity")
    public JsonResult doChangeByEntity(@RequestBody Position entity){
        return positionService.doChangeByEntity(entity);
    }

    @GetMapping("getAllRepo")
    public JsonResult getAllRepo(){
        return repoService.getAllRepo();
    }

}
