package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.PledgeDao;
import com.wr.exe.ServiceException;
import com.wr.feignclient.ElectronicWarehouseFeignService;
import com.wr.pojo.ElectronicWarehouse;
import com.wr.pojo.Pledge;
import com.wr.pojo.Transfer;
import com.wr.service.PledgeService;
import com.wr.util.JsonResult;
import com.wr.util.UserThreadLocalUtil;
import com.wr.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PledgeServiceImpl implements PledgeService {
    @Autowired
    private PledgeDao pledgeDao;
    @Autowired
    private ElectronicWarehouseFeignService electronicWarehouseFeignService;

    /**
     * 针对于货主的分页查询
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @Override
    public JsonResult getPledgePage(Integer currentPage, Integer pageSize, Integer state) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        QueryWrapper<Pledge> queryWrapper = new QueryWrapper<Pledge>();
        queryWrapper.and(wrapper -> wrapper.eq("from_user_id", userId).or().eq("to_user_id", userId));
        //查询记录总条数
        Integer total = 0;
        if(state != null){
            queryWrapper.eq("state", state);
        }
        try {
            total = pledgeDao.selectCount(queryWrapper);
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
        List<Transfer> transferList;
        try {
            transferList =  pledgeDao.findPledgePage(startIndex,pageSize,userId,state);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setData(transferList)
                .setTotal(total).setPageTotal(pageTotal);
        return JsonResult.ok(pageVo);
    }

    /**
     * 添加质押申请
     * @param entity
     * @return
     */
    @Override
    public JsonResult doSavePledge(Pledge entity) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        entity.setState(1).setCreateTime(new Date()).setFromUserId(userId);
        try {
            pledgeDao.insert(entity);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        return JsonResult.ok();
    }

    /**
     * 改变状态、如果是仓库方进行确认就说明是最后一次进行确认
     * 这时候就需要将仓单的状态改变了
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public JsonResult doChangeStateByState(Pledge entity) {
        // 获取当前状态
        Integer state = entity.getState();
        // 如果是受让人确认仓单的usert_id不用变
        //如果state=5说明是仓库确认也就是进行最后一步确认，就需要完成转让，需要对改变仓单的user_Id
        ElectronicWarehouse electronicWarehouse = new ElectronicWarehouse();
        if(state == 5) {
            electronicWarehouse.setId(entity.getElectronicWarehouseId()).setUserId(entity.getFromUserId());
            JsonResult jsonResult = electronicWarehouseFeignService.doChangeByEntity(electronicWarehouse);
            if(jsonResult == null || jsonResult.getCode() != 200)
                throw new ServiceException("服务器异常！");
        }
        int row = 0;
        try {

            row = pledgeDao.updateById(entity);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        if (row != 0)
            return JsonResult.ok();
        return JsonResult.err();
    }

    @Override
    public JsonResult doPledgePageToRepo(Integer currentPage, Integer pageSize) {
        //查询记录总条数
        Integer total = 0;
        Long repoId = UserThreadLocalUtil.getUser().getRepoId();
        // 查询总条数
        total = pledgeDao.selectRows(repoId);

        if(total == 0)
            throw new ServiceException("当前没有记录！");
        //计算起始记录
        int startIndex = (currentPage-1)*pageSize;
        //计算总页数
        int pageTotal = (total-1)/pageSize + 1;
        //验证参数合法性
        if(0>currentPage || currentPage > pageTotal)
            throw new ServiceException("传入的参数不合法！");
        List<Transfer> transferList = null;
        // 获取分页数据
        try {
            transferList =  pledgeDao.getTransferPageToRepo(startIndex,pageSize,repoId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        // 封装返回值数据
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setData(transferList)
                .setTotal(total).setPageTotal(pageTotal);
        return JsonResult.ok(pageVo);
    }
}
