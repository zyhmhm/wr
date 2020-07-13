package com.wr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wr.dao.AppointmentDao;
import com.wr.dao.DetailedDao;
import com.wr.exe.ServiceException;
import com.wr.feignclient.RepoFeignService;
import com.wr.pojo.Appointment;
import com.wr.pojo.Detailed;
import com.wr.pojo.Position;
import com.wr.service.AppointmentService;
import com.wr.util.JsonResult;
import com.wr.util.JsonUtil;
import com.wr.util.UserThreadLocalUtil;
import com.wr.vo.AppointmentVo;
import com.wr.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class AppointmentImpl implements AppointmentService {
    @Autowired
    private AppointmentDao appointmentDao;
    @Autowired
    private DetailedDao detailedDao;

    @Autowired
    private RepoFeignService repoFeignService;

    /**
     * 针对与货主的查询
     * 查询当前用户所创建的预约单
     * 根据联系人姓名、当前用户id来查询
     */
    @Override
    public PageVo getPageObject(String contacts,Integer currentPage, Integer pageSize,Integer state) {
        Long userId = UserThreadLocalUtil.getUser().getId();
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<Appointment>();
        queryWrapper.eq("user_id", userId);
        //查询记录总条数
        Integer total = 0;
        if(state != null){
            queryWrapper.eq("state", state);
        }
        if(StringUtils.isEmpty(contacts))

            try {
                total = appointmentDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
        else {
            queryWrapper.like("contacts", contacts);
            try {
                total = appointmentDao.selectCount(queryWrapper);
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
        List<Appointment> appointmentList;
        try {
            appointmentList =  appointmentDao.findAppointmentPage(contacts,startIndex,pageSize,userId,state);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setAppointment(appointmentList)
                .setTotal(total).setPageTotal(pageTotal);
        return pageVo;
    }

    /**
     * 添加预约单
     * @param appointment
     * @param detailedList
     * @return
     */
    //当前的方法必须启动新事务，并在它自己的事务内运行，如果有事务正在运行，应该将它挂起
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public JsonResult doSaveAppointment(Appointment appointment, List<Detailed> detailedList) {
        if(StringUtils.isEmpty(appointment)){
            throw new ServiceException("预约单的基本信息不能为空！");
        }
        if(detailedList == null || detailedList.size()==0){
            throw new ServiceException("请至少有一个商品明细单");
        }
        //动态获取当前用户
        Long userId = UserThreadLocalUtil.getUser().getId();

        //生成预约单单号防止重复
        String appoNumber = "APP";
        Calendar date = Calendar.getInstance();
        int day = (date.get(Calendar.MONDAY)+1);
        String day2 = "";
        if(day<10)
            day2 = "0" + day;
        else
            day2 = "" + day;
        appoNumber = appoNumber + date.get(Calendar.YEAR) + day2 +
                date.get(Calendar.DAY_OF_MONTH) + date.get(Calendar.HOUR_OF_DAY) +
                date.get(Calendar.MINUTE) + date.get(Calendar.SECOND) +
                date.getTimeInMillis() + userId;

        System.out.println(appointment.getStorageTime());
        appointment.setCreateTime(new Date()).setUpdateTime(appointment.getCreateTime())
                .setState(1).setUserId(userId).setAppointmentNumber(appoNumber);
        try {
            appointmentDao.insert(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库服务器繁忙。请稍后再试");
        }
        // 转换计量单位为具体的
        HashMap<String,String> map = new HashMap<>();
        map.put("0", "重量");
        map.put("1", "数量");
        map.put("2", "体积");
        map.put("3", "长度");
        map.put("4", "面积");
        for(int i=0;i<detailedList.size();i++){
            String str = JsonUtil.to(detailedList.get(i));
            Detailed detailed = JsonUtil.from(str, Detailed.class);
            Double unitPrice = detailed.getUnitPrice();
            Double number = null;
            String unit = detailed.getUnit();
            if("0".equals(unit)){
                number = detailed.getWeight();
            }else if("1".equals(unit)){
                number = (double) detailed.getNumber();
            }else if("2".equals(unit)){
                number = detailed.getVolume();
            }else if("3".equals(unit)){
                number = detailed.getLength();

            }else if("4".equals(unit)){
                number = detailed.getArea();
            }
            detailed.setUnit(map.get(unit));
            double v = unitPrice * number;
            detailed.setTotalPrice(v);
            try {
                detailed.setAppointmentId(appointment.getId());
                detailedDao.insert(detailed);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("数据库服务器繁忙。请稍后再试");
            }
        }
        return JsonResult.ok("添加成功!");
    }

    /**
     * 将状态变为提交待审核
     * */
    @Override
    public JsonResult doSubmitAppById(Long id) {
        Integer state = 2;
        Appointment appointment  = new Appointment();
        appointment.setId(id);
        appointment.setState(state);
        try {
            appointmentDao.updateById(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常，请稍后再试！");
        }
        return JsonResult.ok("提交成功，等待仓库方审核");
    }

    /**
     * 查询预约单详细信息根据Id
     * @param id
     */
    @Override
    @Transactional(readOnly = true)
    public JsonResult doFindAppoById(Long id) {
        QueryWrapper<Detailed> queryWrapper = null;
        Appointment appointment = null;
        List<Detailed> detailedList = new ArrayList<>();
        try {
            appointment = appointmentDao.selectById(id);
            queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("appointment_id", id);
            detailedList = detailedDao.selectList(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            throw  new ServiceException("数据库异常请稍后重试！");
        }
        AppointmentVo appointmentVo = new AppointmentVo();
        appointmentVo.setAppointment(appointment).setDetailedList(detailedList);
        return JsonResult.ok(appointmentVo);
    }

    /**
     * 仓库方--只能查询状态为已提交待审核的预约单，以及自己仓库的预约单
     * @param contacts
     * @param currentPage
     * @param pageSize
     * @param repoId
     * @return
     */
    @Override
    public PageVo getPageObjectByRepoId(String contacts, Integer currentPage, Integer pageSize, Long repoId) {
        //查询记录总条数
        Integer total = 0;
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("depository_id", repoId);
        queryWrapper.gt("state", 1);
        if(StringUtils.isEmpty(contacts))

            try {
                total = appointmentDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
        else {
            queryWrapper.like("contacts", contacts);
            try {
                total = appointmentDao.selectCount(queryWrapper);
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
        List<Appointment> appointmentList;
        try {
            appointmentList =  appointmentDao.findAppointmentPageByRepoId(contacts,startIndex,pageSize,repoId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setAppointment(appointmentList)
                .setTotal(total).setPageTotal(pageTotal);
        return pageVo;
    }

    /**
     * 审核通过或者不通过
     * @param id
     * @param state
     * @param auditNotes
     * @return
     */
    @Override
    public JsonResult doAcceptOrRefuse(Long id, Integer state,String auditNotes) {
        Appointment appointment = new Appointment();
        appointment.setState(state).setId(id).setAuditNotes(auditNotes);
        try {
            int row = appointmentDao.updateById(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库服务器异常");
        }
        return JsonResult.ok();
    }

    //对于货主查询可以制单的预约单————state=8
    @Override
    public PageVo getNeedMadeAppo(String contactsName, Integer currentPage, Integer pageSize,Integer state) {
        //查询记录总条数
        Integer total = 0;
        QueryWrapper<Appointment> queryWrapper = new QueryWrapper<>();
        if(state != null)
            queryWrapper.eq("state", state);
        else {
            List<Integer> states = new ArrayList<>();
            states.add(8);states.add(9);states.add(10);states.add(11);states.add(12);
            queryWrapper.in("state", states);
        }
        if(StringUtils.isEmpty(contactsName)) {

            try {
                total = appointmentDao.selectCount(queryWrapper);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("后台数据库正在维护！");
            }
        }else {
            queryWrapper.like("contacts", contactsName);
            try {
                total = appointmentDao.selectCount(queryWrapper);
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
        List<Appointment> appointmentList;
        //到时候动态获取
        Long userId = UserThreadLocalUtil.getUser().getId();
        try {
            appointmentList =  appointmentDao.findNeedMadeAppoByContactsAndUserId(contactsName,startIndex,pageSize,userId,state);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("后台数据库正在维护!请稍候再试!");
        }
        PageVo pageVo = new PageVo().setCurrentPage(currentPage)
                .setPageSize(pageSize).setAppointment(appointmentList)
                .setTotal(total).setPageTotal(pageTotal);
        return pageVo;
    }

    /**
     * 上面是预约业务所需要的业务方法下面是其他业务需要的该方法是制单业务需要获得的基本信息
     * @param appointmentId
     * @return
     */
    @Override
    public Appointment getInfoById(Long appointmentId) {
        Appointment appointment = null;
        try {
            appointment = appointmentDao.selectById(appointmentId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库服务异常");
        }
        return appointment;
    }

    @Override
    public JsonResult doChangeStateById(Appointment entity) {
        int i = 0;
        try {
            appointmentDao.updateById(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常");
        }
        return JsonResult.ok();
    }

    @Override
    public JsonResult deleteAppoById(Long appointmentId) {
        int row = 0;
        try {
            row = appointmentDao.deleteById(appointmentId);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("s护具看异常，正在抢修！");
        }
        if(row !=0)
            return JsonResult.ok();
        return JsonResult.err();
    }

    @Override
    public JsonResult doInRepoForRepo(Appointment appointment, String detailes) {
        List<Detailed> detailedList = new ArrayList<>();
        detailedList = JsonUtil.toObject(detailes,detailedList.getClass());
        for(int i=0;i<detailedList.size();i++) {
            String str = JsonUtil.to(detailedList.get(i));
            Detailed detailed = JsonUtil.from(str, Detailed.class);
            Position position = new Position();
            position.setId(detailed.getPosition()).setIsUsed(1);
            try {
                detailedDao.updateById(detailed);
                JsonResult jsonResult = repoFeignService.doChangeByEntity(position);
                if(jsonResult == null || jsonResult.getCode()!=200)
                    throw new ServiceException("仓库服务异常！");
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException("数据库异常！");
            }
        }
        try {
            appointmentDao.updateById(appointment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServiceException("数据库异常！");
        }
        return JsonResult.ok();
    }

//    /**
//     * 获取审核备注信息
//     * @param appointmentId
//     * @return
//     */
//    @Override
//    public JsonResult getNotesById(Long appointmentId) {
//
//        return null;
//    }
}
