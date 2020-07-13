package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.TransferDao;
import com.wr.exe.ServiceException;
import com.wr.feignclient.ElectronicWarehouseFeignService;
import com.wr.pojo.ElectronicWarehouse;
import com.wr.pojo.Transfer;
import com.wr.service.TransferService;
import com.wr.util.JsonResult;
import com.wr.util.UserThreadLocalUtil;
import com.wr.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TransferServiceImpl implements TransferService {
    @Autowired
    private TransferDao transferDao;

    @Autowired
    private ElectronicWarehouseFeignService electronicWarehouseFeignService;

    /**
     * 对转让申请执行分页查询，针对于货主用户
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @Override
    public JsonResult getTransferPage(Integer currentPage, Integer pageSize, Integer state) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        QueryWrapper<Transfer> queryWrapper = new QueryWrapper<Transfer>();
        queryWrapper.and(wrapper -> wrapper.eq("from_user_id", userId).or().eq("to_user_id", userId));
        //查询记录总条数
        Integer total = 0;
        if(state != null){
            queryWrapper.eq("state", state);
        }
        try {
            total = transferDao.selectCount(queryWrapper);
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
            transferList =  transferDao.findTransferPage(startIndex,pageSize,userId,state);
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
     * 针对于货主，发起转让申请并保存
     * @param entity
     * @return
     */
    @Override
    public JsonResult doSaveTransfer(Transfer entity) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        entity.setState(1).setCreateTime(new Date()).setFromUserId(userId);
        try {
            transferDao.insert(entity);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        return JsonResult.ok();
    }

    /**
     * 对转让申请进行状态的改变
     * @param entity
     * @return
     */
    @Transactional
    @Override
    public JsonResult doChangeState(Transfer entity) {
        // 获取当前状态
        Integer state = entity.getState();
        // 如果是受让人确认仓单的usert_id不用变
        //如果state=3说明是仓库确认也就是进行最后一步确认，就需要完成转让，需要对改变仓单的user_Id
        ElectronicWarehouse electronicWarehouse = new ElectronicWarehouse();
        if(state == 5) {
            electronicWarehouse.setId(entity.getElectronicWarehouseId()).setUserId(entity.getToUserId());
            JsonResult jsonResult = electronicWarehouseFeignService.doChangeByEntity(electronicWarehouse);
            if(jsonResult == null || jsonResult.getCode() != 200)
                throw new ServiceException("服务器异常！");
        }
        int row = 0;
        try {

            row = transferDao.updateById(entity);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        if (row != 0)
            return JsonResult.ok();
        return JsonResult.err();
    }

    /**
     * 针对于仓库方的分页查询
     * 需要获取当前用户的仓库Id,然后根据对应的仓库Id进行查询
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @Override
    public JsonResult getTransferPageToRepo(Integer currentPage, Integer pageSize, Integer state) {
        //查询记录总条数
        Integer total = 0;
        Long repoId = UserThreadLocalUtil.getUser().getRepoId();
        // 查询总条数
        total = transferDao.selectRows(repoId);

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
            transferList =  transferDao.getTransferPageToRepo(startIndex,pageSize,repoId);
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
