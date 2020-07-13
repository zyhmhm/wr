package com.wr.controller;

import com.wr.pojo.Appointment;
import com.wr.pojo.Detailed;
import com.wr.service.AppointmentService;
import com.wr.util.JsonResult;
import com.wr.util.JsonUtil;
import com.wr.vo.PageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@CrossOrigin
@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    /**
     * 分页查询预约单可以根据联系人姓名
     * @param contactsName
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getAppointmentPage")
    public JsonResult<PageVo> getAppointmentPage(String contactsName, Integer currentPage,Integer pageSize,Integer state){
        PageVo appointmentPageVo =  appointmentService.getPageObject(contactsName,currentPage,pageSize,state);
        return JsonResult.ok(appointmentPageVo);
    }
    /**
     * 新增预约单
     */
    @PostMapping("postAppointment")
    public JsonResult postAppointment(Appointment appointment, String detailes){
        List<Detailed> detailedList = new ArrayList<>();
//        String jsonStr = JsonUtil.to(detailes);
        detailedList = JsonUtil.toObject(detailes,detailedList.getClass());
        //detailedList = JsonUtil.getList(deta)
        return appointmentService.doSaveAppointment(appointment,detailedList);
    }

    /**
     * 将状态为已保存未提交的预约单提交根据Id
     */
    @GetMapping("doSubmitApp")
    public JsonResult doSubmitApp(Long id){
        return appointmentService.doSubmitAppById(id);
    }
    /**
     * 根据Id查询预约单信息
     */
    @GetMapping("doFindAppoById")
    public JsonResult doFindAppById(Long id){
        return appointmentService.doFindAppoById(id);
    }

    /**
     * 删除业务，根据Id来删除预约单
     */
    @GetMapping("deleteAppoById")
    public JsonResult deleteAppoById(Long appointmentId){
        return appointmentService.deleteAppoById(appointmentId);
    }
    /**
     * 根据仓库Id查询未审核的预约单
     * @param contactsName
     * @param currentPage
     * @param pageSize
     * @return
     */
    @GetMapping("/getAppointmentPageToRepo")
    public JsonResult<PageVo> getAppointmentPageToRepo(String contactsName, Integer currentPage,Integer pageSize){
        Long repoId = 1l;
        PageVo appointmentPageVo =  appointmentService.getPageObjectByRepoId(contactsName,currentPage,pageSize,repoId);
        return JsonResult.ok(appointmentPageVo);
    }

    /**
     * 审核通过或者没有通过的处理
     * @param id
     * @param state
     * @param auditNotes
     * @return
     */
    @PostMapping("doAcceptOrRefuse")
    public JsonResult doAcceptOrRefuse(Long id,Integer state,String auditNotes){
        return appointmentService.doAcceptOrRefuse(id,state,auditNotes);
    }
    /**
     *查询可以申请制单的预约单
     */
    @GetMapping("getNeedMadeAppo")
    public JsonResult getNeedMadeAppo(String contactsName, Integer currentPage,Integer pageSize,Integer state){
        PageVo appointmentPageVo =  appointmentService.getNeedMadeAppo(contactsName,currentPage,pageSize,state);
        return JsonResult.ok(appointmentPageVo);
    }
//    /**
//     * 查询预约单的审核备注
//     */
//    @GetMapping("getNotesById")
//    public JsonResult getNotesById(Long appointmentId){
//        return appointmentService.getNotesById(appointmentId);
//    }
    /**
     * 以上为预约业务
     * 下面是其他业务需要的
     */
    @GetMapping("getInfoById")
    public Appointment getInfoById(Long appointmentId){
        return appointmentService.getInfoById(appointmentId);
    }

    @PostMapping("doChangeStateById")
    public JsonResult doChangeStateById(@RequestBody Appointment entity){
        return appointmentService.doChangeStateById(entity);
    }

    /**
     * 仓库方确认入库同时将详细单的状态改变
     */
    @PostMapping("doInRepoForRepo")
    public JsonResult doInRepoForRepo(Appointment appointment,String detailes){
        return appointmentService.doInRepoForRepo(appointment,detailes);
    }

//    @GetMapping("getAppoDetaileInfoById")
//    public JsonResult getAppoDetaileInfoById(Long appointmentId){
//        return appointmentService.getAppoDetaileInfoById(appointmentId);
//    }


}
