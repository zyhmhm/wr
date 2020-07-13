package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.PositionDao;
import com.wr.exe.ServiceException;
import com.wr.pojo.Position;
import com.wr.service.PositionService;
import com.wr.util.JsonResult;
import com.wr.util.UserThreadLocalUtil;
import com.wr.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {
    @Autowired
    private PositionDao positionDao;

    @Override
    public JsonResult doUpdatePosition(Position entity) {
        try {
            positionDao.updateById(entity);

        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常");
        }
        return JsonResult.ok();
    }

    @Override
    public JsonResult doSavePosition(Position entity) {
        Long repoId = UserThreadLocalUtil.getUser().getRepoId();
        if(repoId == null){
            entity.setCreateTime(new Date()).setIsUsed(0);
        }else
            entity.setRepoId(repoId).setCreateTime(new Date()).setIsUsed(0);
        try {
            positionDao.insert(entity);

        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常");
        }
        return JsonResult.ok();
    }

    /**
     * 对仓库的仓位进行分页查询
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @Override
    public JsonResult doGetPositionPage(Integer currentPage, Integer pageSize, Integer state) {
        Long repoId = UserThreadLocalUtil.getUser().getRepoId();
        QueryWrapper<Position> queryWrapper = new QueryWrapper<Position>();
        queryWrapper.eq("repo_id", repoId);
        //查询记录总条数
        Integer total = 0;
        if(state != null){
            queryWrapper.eq("state", state);
        }
        try {
            total = positionDao.selectCount(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护！");
        }
        if(total == 0)
            throw new ServiceException("当前没有记录！");
        //计算起始记录
        int startIndex = (currentPage-1)*pageSize;
        //计算总页数
        int pageTotal = (total-1)/pageSize + 1;
        //验证参数合法性
        if(0>currentPage || currentPage > pageTotal)
            throw new ServiceException("传入的参数不合法！");
        List<Position> positionList;
        try {
            positionList =  positionDao.findPositionPage(startIndex,pageSize,state,repoId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setData(positionList)
                .setTotal(total).setPageTotal(pageTotal);
        return JsonResult.ok(pageVo);
    }

    @Override
    public JsonResult getNoUsedPosition() {
        Long repoId = UserThreadLocalUtil.getUser().getRepoId();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("repo_id", repoId);
        queryWrapper.eq("is_used",0);
        List positionList = null;
        try {
            positionList = positionDao.selectList(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        if(positionList == null)
            return JsonResult.err();
        return JsonResult.ok(positionList);
    }

    @Override
    public JsonResult getPositionById(Long positionId) {
        Position position = positionDao.selectById(positionId);
        return JsonResult.ok(position);
    }

    /**
     * 更新仓位信息
     * @param entity
     * @return
     */
    @Override
    public JsonResult doChangeByEntity(Position entity) {
        positionDao.updateById(entity);
        return JsonResult.ok();
    }
}
