package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
/**
 * ReportService接口定义了业务数据查询和导出的相关操作
 * 主要用于统计和获取系统中的营业额、用户、订单等数据的报表
 */
public interface ReportService {
    /**
     * 获取指定时间段内的营业额统计报表
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 返回营业额统计信息的TurnoverReportVO对象
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取指定时间段内的用户统计报表
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 返回用户统计信息的UserReportVO对象
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取指定时间段内的订单统计报表
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 返回订单统计信息的OrderReportVO对象
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取指定时间段内销售量排名前10的商品报表
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 返回销售量排名前10的商品信息的SalesTop10ReportVO对象
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 导出系统中的业务数据
     * 该方法主要用于响应HTTP请求，以适当的数据格式（如CSV、Excel等）导出业务数据
     * @param response 用于设置HTTP响应头和写入数据的HttpServletResponse对象
     */
    void exportBusinessData(HttpServletResponse response);
}
