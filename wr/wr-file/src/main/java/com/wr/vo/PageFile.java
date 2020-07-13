package com.wr.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PageFile {
    private Integer error = 0;//判断是否有错，0代表没有错
    private String url;       //图片存储的地址
    private Integer width;     //图片宽度
    private Integer height;      //图片高度
    private String type;       //类型，是收据，合同还是什么

    //为了简化操作，可以提供静态方法
    public static PageFile fail(){
        return new PageFile(1,null,null,null,null);
    }
}
