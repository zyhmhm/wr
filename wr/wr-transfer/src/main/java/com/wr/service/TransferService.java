package com.wr.service;

import com.wr.pojo.Transfer;
import com.wr.util.JsonResult;

public interface TransferService {
    JsonResult getTransferPage(Integer currentPage, Integer pageSize, Integer state);

    JsonResult doSaveTransfer(Transfer entity);

    JsonResult doChangeState(Transfer entity);

    JsonResult getTransferPageToRepo(Integer currentPage, Integer pageSize, Integer state);
}
