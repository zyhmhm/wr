package com.wr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.pojo.Position;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionDao extends BaseMapper<Position> {
    List<Position> findPositionPage(@Param("startIndex") Integer startIndex,
                                    @Param("pageSize") Integer pageSize,
                                    @Param("state") Integer state,
                                    @Param("repoId") Long repoId);
}
