package com.wr.vo;

import com.wr.pojo.Appointment;
import com.wr.pojo.Detailed;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
public class AppointmentVo implements Serializable {
    private Appointment appointment;
    private List<Detailed> detailedList;
}
