package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.ElectronicWarehouseDao;
import com.wr.dao.OrderMakingDao;
import com.wr.dao.StorageMarkDao;
import com.wr.exe.ServiceException;
import com.wr.feignclient.AppointmentFeignService;
import com.wr.feignclient.RepoFeignService;
import com.wr.pojo.*;
import com.wr.service.ElectronicWarehouseService;
import com.wr.util.JsonResult;
import com.wr.util.JsonUtil;
import com.wr.util.UserThreadLocalUtil;
import com.wr.vo.EleWareVo;
import com.wr.vo.ElectronicWarehouseVo;
import com.wr.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ElectronicWarehouseServiceImpl implements ElectronicWarehouseService {
    @Autowired
    private ElectronicWarehouseDao electronicWarehouseDao;
    @Autowired
    private StorageMarkDao storageMarkDao;
    @Autowired
    private OrderMakingDao orderMakingDao;
    @Autowired
    private AppointmentFeignService appointmentFeignService;
    @Autowired
    private RepoFeignService repoFeignService;

    /**
     * 根据电子仓单Id查询
     * @param id
     * @return
     */
    @Override
    public JsonResult getEleWareInfoById(Long id) {
        ElectronicWarehouse electronicWarehouse = null;
        StorageMark storageMark = null;
        try {
            electronicWarehouse = electronicWarehouseDao.selectById(id);
            Long warehouseMarkId = electronicWarehouse.getWarehouseMarkId();
            storageMark = storageMarkDao.selectById(warehouseMarkId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        ElectronicWarehouseVo electronicWarehouseVo = new ElectronicWarehouseVo();
        electronicWarehouseVo.setElectronicWarehouse(electronicWarehouse);
        electronicWarehouseVo.setStorageMark(storageMark);
        return JsonResult.ok(electronicWarehouseVo);
    }

    /**
     * 根据仓单得到Id查询仓单当前状态
     * @param electronicWarehouseId
     * @return
     */
    @Override
    public JsonResult getStateById(Long electronicWarehouseId) {
        Integer state = null;
        try {
            state = electronicWarehouseDao.getStateById(electronicWarehouseId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常，正在抢修");
        }
        return JsonResult.ok(state);
    }

    /**
     * 根据仓单Id改变状态
     * @param electronicWarehouseId
     * @param state
     * @return
     */
    @Override
    public JsonResult doChangeEleWareStateById(Long electronicWarehouseId, Integer state,Long cashId) {
        ElectronicWarehouse electronicWarehouse = new ElectronicWarehouse().setId(electronicWarehouseId).setCashId(cashId);
        return doChange(state, electronicWarehouse);
    }

    private JsonResult doChange(Integer state, ElectronicWarehouse electronicWarehouse) {
        electronicWarehouse.setState(state);
        Long orderMakingId = null;
        Long appoId = null;
        try {
            orderMakingId = electronicWarehouseDao.getOrderMakingId(electronicWarehouse.getId());
            appoId = electronicWarehouseDao.getAppoId(electronicWarehouse.getId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库异常正在抢修");
        }
        OrderMaking orderMaking = new OrderMaking().setId(orderMakingId).setState(state +3);
        Appointment appointment = new Appointment().setId(appoId).setState(state +11);
        try {
//            appointmentFeignService.doChangeStateById(appointment);
//            orderMakingDao.updateById(orderMaking);
            electronicWarehouseDao.updateById(electronicWarehouse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库异常正在抢修");
        }
        return JsonResult.ok();
    }

    /**
     * 根据对付申请Id改变状态
     * @param cashId
     * @param state
     * @return
     */
    @Override
    public JsonResult doChangeEleStateByCashId(Long cashId, Integer state) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("cash_id", cashId);
        ElectronicWarehouse electronicWarehouse = electronicWarehouseDao.selectOne(queryWrapper);
        if(state == 6){
            Position position = new Position().setId(electronicWarehouse.getPosition()).setIsUsed(0);
            repoFeignService.doChangeByEntity(position);
        }
        return doChange(state, electronicWarehouse);
    }

    /**
     * 根据不同的类型Id查找对应的仓单信息
     * @param type
     * @param id
     * @return
     */
    @Override
    public JsonResult getEleWareInfoByTypeId(String type, Long id) {
        QueryWrapper<ElectronicWarehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(type, id);
        List<ElectronicWarehouse> electronicWarehouseList = electronicWarehouseDao.selectList(queryWrapper);
        if(electronicWarehouseList==null)
            throw new ServiceException("没有数据");
        List<ElectronicWarehouseVo> voList = new ArrayList<>();
        for(int i=0;i<electronicWarehouseList.size();i++){
            ElectronicWarehouse electronicWarehouse = electronicWarehouseList.get(i);
            StorageMark storageMark = storageMarkDao.selectById(electronicWarehouse.getWarehouseMarkId());
            ElectronicWarehouseVo vo = new ElectronicWarehouseVo().setElectronicWarehouse(electronicWarehouse)
                    .setStorageMark(storageMark);
            voList.add(vo);
        }
        return JsonResult.ok(voList);
    }

    /**
     * 改变状态根据Entity,同时也可由改变其他的，如所有人ID
     * @param entity
     * @return
     */
    @Override
    public JsonResult doChangeByEntity(ElectronicWarehouse entity) {
        int row = 0;
        try {
            row = electronicWarehouseDao.updateById(entity);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("数据库异常，正在抢修！");
        }
        if (row!=0){
            return JsonResult.ok();
        }
        return JsonResult.err();
    }

    /**
     * 针对于货主的仓单查询
     * @param currentPage
     * @param pageSize
     * @param state
     * @return
     */
    @Override
    public JsonResult getEleWarePage(String inventor,Integer currentPage, Integer pageSize, Integer state) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        QueryWrapper<ElectronicWarehouse> queryWrapper = new QueryWrapper<ElectronicWarehouse>();
        Long repoId = UserThreadLocalUtil.getUser().getRepoId();
        queryWrapper.and(wrapper -> wrapper.eq("user_id", userId).or().eq("repo_id", repoId));
        //查询记录总条数
        Integer total = 0;
        if(state != null){
            queryWrapper.eq("state", state);
        }
        if(!StringUtils.isEmpty(inventor))
            queryWrapper.like("inventor", inventor);
        try {
            total = electronicWarehouseDao.selectCount(queryWrapper);
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
        List<ElectronicWarehouse> electronicWarehouseList;
        try {
            electronicWarehouseList =  electronicWarehouseDao.findEleWarePage(inventor,startIndex,pageSize,userId,state,repoId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setOrderMakings(electronicWarehouseList)
                .setTotal(total).setPageTotal(pageTotal);
        return JsonResult.ok(pageVo);
    }

    /**
     * 仓库方制作仓单的时候自动填写信息
     * @param appointmentId
     * @param repoId
     * @param orderMakingId
     * @return
     */
    @Override
    public JsonResult doGetEleWareInfoToMake(Long appointmentId, Long repoId, Long orderMakingId) {
        // 获取仓单对应的预约单信息
        JsonResult appointmentInfo = appointmentFeignService.doFindAppById(appointmentId);
        Appointment appointment = new Appointment();
        List<Detailed> detailedList = new ArrayList<Detailed>();
        if(appointmentInfo!=null){
            HashMap data = (HashMap)appointmentInfo.getData();
            Object appointment1 = data.get("appointment");
            String appointmentJson = JsonUtil.to(appointment1);
            Object detailesList = data.get("detailedList");
            String detailedListJson = JsonUtil.to(detailesList);
            appointment = JsonUtil.from(appointmentJson, appointment.getClass());
            detailedList = JsonUtil.from(detailedListJson, detailedList.getClass());
        }else{
            throw new ServiceException("预约服务器异常");
        }
        // 获取制单申请信息
        OrderMaking orderMakingInfo = orderMakingDao.selectById(orderMakingId);
        // 获取仓库的基本信息
        JsonResult repoInfo = repoFeignService.getRepoById(repoId);
        Repo repo = null;
        if(repoInfo != null){
            Object data = repoInfo.getData();
            String repoJson = JsonUtil.to(data);
            repo = JsonUtil.from(repoJson, Repo.class);
        }else{
            throw new ServiceException("仓库服务器异常");
        }
        // 封装仓单信息
        List<EleWareVo> eleWareList = new ArrayList<>();
        for(int i=0;i<detailedList.size();i++){
            Object detailedObj = detailedList.get(i);
            String detaileJson = JsonUtil.to(detailedObj);
            Detailed detailed = JsonUtil.from(detaileJson,Detailed.class);
            JsonResult positionInfo = repoFeignService.getPosition(detailed.getPosition());
            Position position = null;
            if(positionInfo !=null && positionInfo.getCode() == 200){
                positionInfo.getData();
                Object data = positionInfo.getData();
                String positionJson = JsonUtil.to(data);
                position = JsonUtil.from(positionJson, Position.class);
            }else{
                throw new ServiceException("仓库服务器异常");
            }
            EleWareVo eleWare = new EleWareVo();
            eleWare.setBrand(detailed.getBrand()).setSpecifications(detailed.getSpecifications())
                    .setModel(detailed.getModel()).setProductionAdd(detailed.getProductionAdd()).setBatchNumber(detailed.getBatchNumber())
                    .setInventor(appointment.getShipper()).setCustodian(orderMakingInfo.getCompany())
                    .setCAddr(repo.getDepositoryAdd()).setGoodsCategory(appointment.getGoodsName())
                    .setIsClean(detailed.getIsClear()).setGoodsName(detailed.getGoodsName()).setManufacturer(detailed.getManufacturer())
                    .setLossStandard("0").setGoodsEncoding(detailed.getEncoding()).setPacking(detailed.getPackinng()).setNumber((long)detailed.getNumber())
                    .setWeight(detailed.getWeight()).setStorehouse(position.getPosition()).setReceiptValues(detailed.getTotalPrice())
                    .setEffectiveDates(orderMakingInfo.getValidityPeriod()).setInsurance("全险")
                    .setUnit(detailed.getUnit()).setIAddr(appointment.getShipperTell()).setRate("按时计费")
                    .setAppointmentId(appointmentId).setOrdermakingId(orderMakingId).setUserId(orderMakingInfo.getUserId())
                    .setRepoId(repoId).setPosition(detailed.getPosition());
            eleWareList.add(eleWare);
        }
        return JsonResult.ok(eleWareList);
    }

}
