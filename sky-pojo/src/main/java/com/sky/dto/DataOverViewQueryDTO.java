package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 数据概览查询数据传输对象，用于封装数据概览查询的时间范围参数。
 * 该对象通常用于接收查询指定时间范围内数据统计的请求参数。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataOverViewQueryDTO implements Serializable {

    /**
     * 查询的开始时间（包含），用于指定数据概览的起始时间点。
     */
    private LocalDateTime begin;

    /**
     * 查询的结束时间（包含），用于指定数据概览的结束时间点。
     */
    private LocalDateTime end;

}