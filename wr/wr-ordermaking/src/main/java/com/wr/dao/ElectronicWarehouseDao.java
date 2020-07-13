package com.wr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.pojo.ElectronicWarehouse;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ElectronicWarehouseDao extends BaseMapper<ElectronicWarehouse> {
    @Select("select state from t_electronic_warehouse where id = #{electronicWarehouseId}")
    Integer getStateById(@Param("electronicWarehouseId") Long electronicWarehouseId);

    @Select("select appointment_id from t_electronic_warehouse where id = #{electronicWarehouseId}")
    Long getOrderMakingId(Long electronicWarehouseId);
    @Select("select ordermaking_id from t_electronic_warehouse where id = #{electronicWarehouseId}")
    Long getAppoId(Long electronicWarehouseId);

    List<ElectronicWarehouse> findEleWarePage(@Param("inventor") String inventor,@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize,@Param("userId") Long userId,@Param("state") Integer state,@Param("repoId") Long repoId);
}
