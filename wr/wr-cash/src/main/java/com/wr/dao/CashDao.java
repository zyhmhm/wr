package com.wr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.pojo.Cash;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashDao extends BaseMapper<Cash> {
    List<Cash> doGetCashPage(@Param("consigneeName") String consigneeName,
                             @Param("startIndex") Integer startIndex
            , @Param("pageSize") Integer pageSize,@Param("repoId") Long repoId);

    List<Cash> doGetCashPageToShipper(@Param("consigneeName") String consigneeName,
                                      @Param("startIndex") Integer startIndex
            , @Param("pageSize") Integer pageSize,@Param("userId") Long userId);

    Long getRepoId(@Param("electronicWarehouseId") Long electronicWarehouseId);
}
