package com.wr.service;

import com.wr.pojo.Position;
import com.wr.util.JsonResult;

public interface PositionService {
    JsonResult doUpdatePosition(Position entity);

    JsonResult doSavePosition(Position entity);

    JsonResult doGetPositionPage(Integer currentPage, Integer pageSize, Integer state);

    JsonResult getNoUsedPosition();

    JsonResult getPositionById(Long positionId);

    JsonResult doChangeByEntity(Position entity);
}
