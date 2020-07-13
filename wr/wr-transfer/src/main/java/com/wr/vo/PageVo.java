package com.wr.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageVo<T>{
    private Integer total;
    private Integer currentPage;
    private Integer pageSize;
    private Integer pageTotal;
    private List<T> data;
}
