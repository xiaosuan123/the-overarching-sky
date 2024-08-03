package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * 商品销量数据传输对象，用于封装商品销量相关的数据。
 * 该对象通常用于统计和传输商品销售情况的相关信息。
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GoodsSalesDTO implements Serializable {

    /**
     * 商品的名称，用于标识具体的商品。
     */
    private String name;

    /**
     * 商品的销量，表示在一定时间内该商品的销售数量。
     */
    private Integer number;

}