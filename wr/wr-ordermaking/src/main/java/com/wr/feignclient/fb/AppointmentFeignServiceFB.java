package com.wr.feignclient.fb;

import com.wr.exe.ServiceException;
import com.wr.feignclient.AppointmentFeignService;
import com.wr.pojo.Appointment;
import com.wr.util.JsonResult;
import org.springframework.stereotype.Component;

@Component
public class AppointmentFeignServiceFB implements AppointmentFeignService {

    @Override
    public Appointment getInfoById(Long appointmentId) {
        throw new ServiceException("预约服务异常！");
    }

    @Override
    public JsonResult doChangeStateById(Appointment entity) {
        throw new ServiceException("预约服务异常！");
    }

    @Override
    public JsonResult doFindAppById(Long id) {
        return null;
    }
}
