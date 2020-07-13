package com.wr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.pojo.Escort;
import com.wr.pojo.Transfer;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscortDao extends BaseMapper<Escort> {
    List<Escort> findEscortPage(@Param("startIndex") Integer startIndex, @Param("pageSize") Integer pageSize, @Param("userId") Long userId,@Param("state") Integer state);

    Integer selectRows(Long repoId);

    List<Transfer> getTransferPageToRepo(Integer startIndex, Integer pageSize, Long repoId);
}
