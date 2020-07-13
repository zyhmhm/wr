package com.wr.service;

import com.wr.pojo.ElectronicWarehouse;
import com.wr.util.JsonResult;

public interface ElectronicWarehouseService {
    JsonResult getEleWareInfoById(Long id);

    JsonResult getStateById(Long electronicWarehouseId);

    JsonResult doChangeEleWareStateById(Long electronicWarehouseId, Integer state,Long cashId);

    JsonResult doChangeEleStateByCashId(Long cashId, Integer state);

    JsonResult getEleWareInfoByTypeId(String type, Long id);

    JsonResult doChangeByEntity(ElectronicWarehouse entity);

    JsonResult getEleWarePage(String inventor,Integer currentPage, Integer pageSize, Integer state);

    JsonResult doGetEleWareInfoToMake(Long appointmentId, Long repoId, Long orderMakingId);

}
