package com.wr.service;

import com.wr.pojo.ElectronicWarehouse;
import com.wr.pojo.OrderMaking;
import com.wr.pojo.StorageMark;
import com.wr.util.JsonResult;
import com.wr.vo.EleWareVo;

public interface OrderMakingService {
    JsonResult doSave(OrderMaking orderMaking);

    JsonResult getInfoById(Long id);

    JsonResult doGetOrderMakingToRepo(String shipper, Integer currentPage, Integer pageSize);

    JsonResult doGetOrderMakingById(Long id);

    JsonResult doAcceptOrRefuse(Long id, Integer state, String notes);

    JsonResult doSaveEleWare(ElectronicWarehouse electronicWarehouse, StorageMark storageMark);

    JsonResult doChangeStateById(Long orderMakingId, Integer state);

    JsonResult doSaveEleWareVo(EleWareVo[] eleWareVoList);

    JsonResult doGetOrderMakingToShipper(String shipper, Integer currentPage, Integer pageSize, Integer state) throws IllegalAccessException;
}
