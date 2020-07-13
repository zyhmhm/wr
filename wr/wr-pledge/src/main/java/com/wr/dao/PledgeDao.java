package com.wr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.pojo.Pledge;
import com.wr.pojo.Transfer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PledgeDao extends BaseMapper<Pledge> {
    List<Transfer> findPledgePage(@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize,@Param("userId") Long userId,@Param("state") Integer state);

    Integer selectRows(Long repoId);

    List<Transfer> getTransferPageToRepo(@Param("startIndex") Integer startIndex,@Param("pageSize") Integer pageSize,@Param("repoId") Long repoId);
}
