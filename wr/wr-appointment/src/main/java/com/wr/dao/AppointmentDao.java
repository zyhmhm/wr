package com.wr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.pojo.Appointment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentDao extends BaseMapper<Appointment> {
    List<Appointment> findAppointmentPage(@Param("contacts") String contacts, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,@Param("userId") Long userId,@Param("state") Integer state);

    List<Appointment> findAppointmentPageByRepoId(@Param("contacts") String contacts, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,@Param("repoId") Long repoId);

    List<Appointment> findNeedMadeAppoByContactsAndUserId(@Param("contacts") String contacts, @Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize,@Param("userId") Long userId,@Param("state") Integer state);
}
