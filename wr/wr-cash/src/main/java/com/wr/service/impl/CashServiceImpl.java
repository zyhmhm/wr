package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.CashDao;
import com.wr.exe.ServiceException;
import com.wr.feignclient.ElectronicWarehouseFeignService;
import com.wr.feignclient.RepoFeignService;
import com.wr.pojo.Cash;
import com.wr.service.CashService;
import com.wr.util.JsonResult;
import com.wr.util.UserThreadLocalUtil;
import com.wr.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CashServiceImpl implements CashService {
    @Autowired
    private CashDao cashDao;
    @Autowired
    private ElectronicWarehouseFeignService electronicWarehouseFeignService;
    @Autowired
    private RepoFeignService repoFeignService;
    /**
     * 提交对付申请
     * 同时改变电子仓单、制单、电子仓单的状态改变
     * @param cash
     * @return
     */
    @Override
    @Transactional
    public JsonResult doSaveCash(Cash cash,Long electronicWarehouseId) {
        cash.setState(1).setUserId(UserThreadLocalUtil.getUser().getId());
        JsonResult stateById = electronicWarehouseFeignService.getStateById(electronicWarehouseId);
        if(stateById.getCode() == 200){
            if((Integer)stateById.getData()>=2)
                throw new ServiceException("已经发起过对付申请不能再发送");
        }
        Long repoId = cashDao.getRepoId(electronicWarehouseId);
        cash.setRepoId(repoId);
        try {
            //保存对付申请
            cashDao.insert(cash);
            //改变仓单以及制单申请，预约单申请状态
            JsonResult jsonResult = electronicWarehouseFeignService.doChangeEleWareStateById(electronicWarehouseId, 2, cash.getId());
            if(jsonResult == null){
                throw new ServiceException("后台异常！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        return JsonResult.ok();
    }

    /**
     * 针对于仓储企的分页查询
     * @param consigneeName
     * @param currentPage
     * @param pageSize
     * @param repoId
     * @return
     */
    @Override
    public JsonResult doGetCashPage(String consigneeName, Integer currentPage, Integer pageSize,Long repoId) {
        //查询记录总条数
        Integer total = 0;
        QueryWrapper<Cash> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("repo_id", repoId);
//        queryWrapper.eq("state", 2);
        if(StringUtils.isEmpty(consigneeName))

            try {
                total = cashDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
        else {
            queryWrapper.like("consignee_name", consigneeName);
            try {
                total = cashDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
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
        List<Cash> cashList = null;
        try {
            cashList =  cashDao.doGetCashPage(consigneeName,startIndex,pageSize,repoId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setCashList(cashList)
                .setTotal(total).setPageTotal(pageTotal);
        return JsonResult.ok(pageVo);
    }

    /**
     * 针对或住房的分页查询
     * 需要根据当前用户登陆的Id来查询当前用户所拥有的Id
     * @param consigneeName
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public JsonResult doGetCashPageToShipper(String consigneeName, Integer currentPage, Integer pageSize) {
        //获取当前用户的Id信息
        Long userId = UserThreadLocalUtil.getUser().getId();
        //查询记录总条数
        Integer total = 0;
        QueryWrapper<Cash> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
//        queryWrapper.eq("state", 2);
        if(StringUtils.isEmpty(consigneeName))

            try {
                total = cashDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
        else {
            queryWrapper.like("consignee_name", consigneeName);
            try {
                total = cashDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
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
        List<Cash> cashList = null;
        try {
            cashList =  cashDao.doGetCashPageToShipper(consigneeName,startIndex,pageSize,userId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setCashList(cashList)
                .setTotal(total).setPageTotal(pageTotal);
        return JsonResult.ok(pageVo);
    }

    /**
     *改变对付申请的状态，其中需要有审核备注
     * 同时需要改变其他申请单的状态
     */
    @Transactional
    @Override
    public JsonResult doAcceptOrRefuse(Cash entity) {
        if(entity.getState() ==5){

        }
        cashDao.updateById(entity);
        JsonResult jsonResult = electronicWarehouseFeignService.doChangeEleStateByCashId(entity.getId(), entity.getState()+1);
        if(jsonResult.getCode()!=200)
            throw new ServiceException("服务器异常请稍后尝试");
        return JsonResult.ok();
    }
}
