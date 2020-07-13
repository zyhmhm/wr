package com.wr.service;

import com.wr.pojo.Escort;
import com.wr.util.JsonResult;

public interface EscortService {
    JsonResult getEscortPage(Integer currentPage, Integer pageSize, Integer state);

    JsonResult doSaveEscort(Escort entity);

    JsonResult doChangeStateByState(Escort entity);

    JsonResult doEscortPageToRepo(Integer currentPage, Integer pageSize);
}
