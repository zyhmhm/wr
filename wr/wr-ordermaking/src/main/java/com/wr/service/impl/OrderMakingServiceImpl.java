package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.ElectronicWarehouseDao;
import com.wr.dao.OrderMakingDao;
import com.wr.dao.StorageMarkDao;
import com.wr.exe.ServiceException;
import com.wr.feignclient.AppointmentFeignService;
import com.wr.feignclient.RepoFeignService;
import com.wr.pojo.*;
import com.wr.service.OrderMakingService;
import com.wr.util.JsonResult;
import com.wr.util.UserThreadLocalUtil;
import com.wr.vo.EleWareVo;
import com.wr.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class OrderMakingServiceImpl implements OrderMakingService {
    @Autowired
    private OrderMakingDao orderMakingDao;
    @Autowired
    private ElectronicWarehouseDao electronicWarehouseDao;
    @Autowired
    private StorageMarkDao storageMarkDao;

    @Autowired
    private AppointmentFeignService appointmentFeignService;
    @Autowired
    private RepoFeignService repoFeignService;

    /**
     * 新增制单申请、
     * 同时需要改变预约单的状态
     * @param orderMaking
     * @return
     */
    @Override
    @Transactional
    public JsonResult doSave(OrderMaking orderMaking) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        Appointment appointment = new Appointment();
        appointment.setId(orderMaking.getAppointmentId())
                .setState(9);

        orderMaking.setState(1).setUserId(userId);
        try {
            orderMakingDao.insert(orderMaking);
            appointmentFeignService.doChangeStateById(appointment);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        return JsonResult.ok();
    }

    /**
     * 货主申请制单中所需要的基本信息
     * 不能修改
     * @param appointmentId
     * @return
     */
    @Transactional
    @Override
    public JsonResult getInfoById(Long appointmentId) {
        /*
         * 获取预约单上面所需要的基本信息
         */
        //调用预约业务的方法
        //可能会出现错误，就是如果预约业务服务器宕机了返回的
        // 数据不能强转为appointment类型(已经修改，在那边直接抛出异常)
//        JsonResult appoJsonResult = appointmentFeignService.getInfoById(appointmentId);
        Appointment appointment = null;
        try {
//            appointment = (Appointment)appoJsonResult.getData();
            appointment = appointmentFeignService.getInfoById(appointmentId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("预约服务异常");
        }
        OrderMaking orderMaking = new OrderMaking();
        orderMaking.setAppointmentId(appointmentId).setAppointmentNumber(appointment.getAppointmentNumber());
        orderMaking.setGoodsName(appointment.getGoodsName());
        orderMaking.setShipper(appointment.getShipper());
        orderMaking.setShipperTell(appointment.getShipperTell());
        //获取仓库id,用以查询仓库方的那一部分信息
        Long depositoryId = appointment.getDepositoryId();
        //调用仓库服务的方法以获取所需要的仓库的基本信息
        Repo repo = null;
        try {
            repo = (Repo) repoFeignService.getRepoById(depositoryId).getData();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException(e.getMessage());
        }
        orderMaking.setCompany(repo.getCompany())
                .setDepositoryId(repo.getId())
                .setDepositoryName(repo.getDepositoryName())
                .setDepositoryContacts(repo.getDepositoryContacts())
                .setDepositoryTell(repo.getDepositoryTell());
        return JsonResult.ok(orderMaking);
    }

    /**
     * 查询档当前仓库所拥有的制单申请
     * @param shipper
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Transactional
    @Override
    public JsonResult doGetOrderMakingToRepo(String shipper, Integer currentPage, Integer pageSize) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        Long repoId = repoFeignService.doGetOrderMakingToRepo(userId);
        //查询记录总条数
        Integer total = 0;
        QueryWrapper<OrderMaking> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("depository_id", repoId);
//        queryWrapper.eq("state", 1);
        if(StringUtils.isEmpty(shipper))

            try {
                total = orderMakingDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
        else {
            queryWrapper.like("shipper", shipper);
            try {
                total = orderMakingDao.selectCount(queryWrapper);
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
        List<OrderMaking> orderMakingList;
        try {
            orderMakingList =  orderMakingDao.doGetOrderMakingToRepo(shipper,startIndex,pageSize,repoId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo();
        pageVo.setPageSize(pageSize).setOrderMakings(orderMakingList)
                .setTotal(total).setPageTotal(pageTotal);
        return JsonResult.ok(pageVo);
    }

    /**
     * 根据Id查询制单申请详情
     * @param id
     * @return
     */
    @Override
    public JsonResult doGetOrderMakingById(Long id) {
        OrderMaking orderMaking = null;
        try {
            orderMaking = orderMakingDao.selectById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库异常");
        }
        return JsonResult.ok(orderMaking);
    }

    /**
     * 拒绝制单申请或者通过制单申请
     * 同时需要改变的有预约单的状态
     * @param id
     * @param state
     * @param notes
     * @return
     */
    @Transactional
    @Override
    public JsonResult doAcceptOrRefuse(Long id, Integer state, String notes) {
        OrderMaking orderMaking = new OrderMaking();
        orderMaking.setId(id).setState(state).setNotes(notes);
        Appointment appointment = new Appointment();
        int i = 0;
        try {
            Long appointmentId = orderMakingDao.selectById(id).getAppointmentId();
            appointment.setId(appointmentId).setState(state+8);
            appointmentFeignService.doChangeStateById(appointment);
            i = orderMakingDao.updateById(orderMaking);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        if(i == 0){
            throw new ServiceException("当前记录可能已经不存在！");
        }
        return JsonResult.ok();
    }

    /**
     * 保存制作的仓单
     * @param electronicWarehouse
     * @param storageMark
     * @return
     */
    @Transactional
    @Override
    public JsonResult doSaveEleWare(ElectronicWarehouse electronicWarehouse, StorageMark storageMark) {
        OrderMaking orderMaking = new OrderMaking();
        orderMaking.setId(electronicWarehouse.getOrdermakingId()).setState(4);
        Appointment appointment = new Appointment();
        appointment.setId(electronicWarehouse.getAppointmentId()).setState(12);
        electronicWarehouse.setState(1);
        try {
            orderMakingDao.updateById(orderMaking);
            //制单之后预约单恩德状态也需要改变
            appointmentFeignService.doChangeStateById(appointment);
            storageMarkDao.insert(storageMark);
            electronicWarehouse.setWarehouseMarkId(storageMark.getId()).setUserId(UserThreadLocalUtil.getUser().getId());
            electronicWarehouseDao.insert(electronicWarehouse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库异常");
        }
        return JsonResult.ok();
    }

    /**
     * 根据Id改变制单申请状态
     * @param orderMakingId
     * @param state
     * @return
     */
    @Override
    public JsonResult doChangeStateById(Long orderMakingId, Integer state) {
        OrderMaking orderMaking = new OrderMaking().setId(orderMakingId).setState(state);
        try {
            orderMakingDao.updateById(orderMaking);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常");
        }
        return JsonResult.ok();
    }

    @Override
    public JsonResult doSaveEleWareVo(EleWareVo[] eleWareVoList) {
        for(int i=0;i<eleWareVoList.length;i++){
            EleWareVo eleWareVo = eleWareVoList[i];
            ElectronicWarehouse electronicWarehouse = (ElectronicWarehouse) eleWareVo;
            StorageMark storageMark = new StorageMark().setBrand(eleWareVo.getBrand()).setBatchNumber(eleWareVo.getBatchNumber())
                    .setModel(eleWareVo.getModel()).setProductionAdd(eleWareVo.getProductionAdd())
                    .setSpecifications(eleWareVo.getSpecifications());
            electronicWarehouse.setState(1);
            try {
                storageMarkDao.insert(storageMark);
                electronicWarehouse.setWarehouseMarkId(storageMark.getId());
                electronicWarehouseDao.insert(electronicWarehouse);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("数据库异常！");
            }
        }
        OrderMaking orderMaking = new OrderMaking();
        orderMaking.setId( eleWareVoList[0].getOrdermakingId()).setState(4);
        Appointment appointment = new Appointment();
        appointment.setId( eleWareVoList[0].getAppointmentId()).setState(12);
        orderMakingDao.updateById(orderMaking);
        //制单之后预约单恩德状态也需要改变
        appointmentFeignService.doChangeStateById(appointment);
        return JsonResult.ok();
    }

    /**
     * 货主的分页条件查询
     * @param shipper
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @Override
    public JsonResult doGetOrderMakingToShipper(String shipper, Integer currentPage, Integer pageSize, Integer state) throws IllegalAccessException {
        Long userId = UserThreadLocalUtil.getUser().getId();
        Long repoId = repoFeignService.doGetOrderMakingToRepo(userId);
        //查询记录总条数
        Integer total = 0;
        QueryWrapper<OrderMaking> queryWrapper = new QueryWrapper<>();

//        queryWrapper.eq("state", 1);
        if(state != null){
            if(state>0 && state <5){
                queryWrapper.eq("state", state);
            }else
                throw new IllegalAccessException("参数不合法！");
        }
        if(StringUtils.isEmpty(shipper))

            try {
                total = orderMakingDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
        else {
            queryWrapper.like("shipper", shipper);
            try {
                total = orderMakingDao.selectCount(queryWrapper);
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
        List<OrderMaking> orderMakingList;
        try {
            orderMakingList =  orderMakingDao.doGetOrderMakingToShipper(shipper,startIndex,pageSize,repoId,state);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo();
        pageVo.setPageSize(pageSize).setOrderMakings(orderMakingList)
                .setTotal(total).setPageTotal(pageTotal);
        return JsonResult.ok(pageVo);
    }
}
