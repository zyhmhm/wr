package com.wr.service;

import com.wr.pojo.Pledge;
import com.wr.util.JsonResult;

public interface PledgeService {
    JsonResult getPledgePage(Integer currentPage, Integer pageSize, Integer state);

    JsonResult doSavePledge(Pledge entity);

    JsonResult doChangeStateByState(Pledge entity);

    JsonResult doPledgePageToRepo(Integer currentPage, Integer pageSize);
}
