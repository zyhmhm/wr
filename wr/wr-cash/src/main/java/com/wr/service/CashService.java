package com.wr.service;

import com.wr.pojo.Cash;
import com.wr.util.JsonResult;

public interface CashService {
    JsonResult doSaveCash(Cash cash,Long electronicWarehouseId);

    JsonResult doGetCashPage(String consigneeName, Integer currentPage, Integer pageSize,Long repoId);

    JsonResult doGetCashPageToShipper(String consigneeName, Integer currentPage, Integer pageSize);

    JsonResult doAcceptOrRefuse(Cash entity);
}
