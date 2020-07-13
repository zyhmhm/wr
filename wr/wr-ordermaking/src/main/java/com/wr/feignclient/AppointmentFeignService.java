package com.wr.feignclient;

import com.wr.feignclient.fb.AppointmentFeignServiceFB;
import com.wr.pojo.Appointment;
import com.wr.util.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "appointment-service",fallback = AppointmentFeignServiceFB.class)
public interface AppointmentFeignService {
    @GetMapping("/appointment/getInfoById")
//    @ResponseBody
    Appointment getInfoById(@RequestParam Long appointmentId);

//    @PostMapping(value = "appointment/doChangeStateById",consumes = "application/json")
    @PostMapping(value = "appointment/doChangeStateById")

    JsonResult doChangeStateById(@RequestBody Appointment entity);

    // 获取预约单详细信息，包括明细单
    @GetMapping("/appointment/doFindAppoById")
    public JsonResult doFindAppById(@RequestParam Long id);
}
