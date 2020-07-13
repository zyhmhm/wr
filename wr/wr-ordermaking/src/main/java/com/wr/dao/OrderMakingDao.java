package com.wr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wr.pojo.OrderMaking;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.nimbus.State;
import java.util.List;

@Repository
public interface OrderMakingDao extends BaseMapper<OrderMaking> {
    List<OrderMaking> doGetOrderMakingToRepo(@Param("shipper") String shipper, @Param("startIndex") Integer startIndex,
                                             @Param("pageSize") Integer pageSize, @Param("repoId") Long repoId);

    List<OrderMaking> doGetOrderMakingToShipper(@Param("shipper") String shipper, @Param("startIndex") Integer startIndex,
                                                @Param("pageSize") Integer pageSize, @Param("repoId") Long repoId,
                                                @Param("state")Integer state);
}
