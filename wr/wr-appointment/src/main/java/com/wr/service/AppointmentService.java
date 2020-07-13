package com.wr.service;

import com.wr.pojo.Appointment;
import com.wr.pojo.Detailed;
import com.wr.util.JsonResult;
import com.wr.vo.PageVo;

import java.util.List;

public interface AppointmentService
{
    PageVo getPageObject(String contactsName,Integer currentPage, Integer pageSize,Integer state);

    JsonResult doSaveAppointment(Appointment appointment, List<Detailed> detailedList);

    JsonResult doSubmitAppById(Long id);

    JsonResult doFindAppoById(Long id);

    PageVo getPageObjectByRepoId(String contactsName, Integer currentPage, Integer pageSize, Long repoId);

    JsonResult doAcceptOrRefuse(Long id, Integer state,String auditNotes);

    PageVo getNeedMadeAppo(String contactsName, Integer currentPage, Integer pageSize,Integer state);

    /**
     * 制单过程所需要的方法，获取基本信息
     * @param appointmentId
     * @return
     */
    Appointment getInfoById(Long appointmentId);

    JsonResult doChangeStateById(Appointment entity);

    JsonResult deleteAppoById(Long appointmentId);

    JsonResult doInRepoForRepo(Appointment appointment, String detaileds);

//    JsonResult getNotesById(Long appointmentId);
}
