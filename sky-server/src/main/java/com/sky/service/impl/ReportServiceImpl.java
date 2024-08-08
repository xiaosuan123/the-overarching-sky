package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 根据指定日期范围获取营业额统计数据
     * 该方法将计算每个日期的营业额总和，并返回一个包含日期和对应营业额的报告对象
     *
     * @param begin 起始日期
     * @param end   结束日期
     * @return 返回一个TurnoverReportVO对象，包含日期列表和对应营业额列表
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 初始化日期列表，用于存储起始日期到结束日期之间的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        // 循环遍历，直到起始日期等于结束日期，构建日期列表
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 初始化营业额列表，用于存储每个日期的营业额
        List<Double> turnoverList = new ArrayList<>();
        // 遍历日期列表，计算每个日期的营业额
        for (LocalDate date : dateList) {
            // 设置开始时间为当前日期的凌晨
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            // 设置结束时间为当前日期的午夜
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // 创建映射对象，用于查询订单数据
            Map map = new HashMap();
            // 设置查询开始时间
            map.put("begin", beginTime);
            // 设置查询结束时间
            map.put("end", endTime);
            // 设置查询状态为完成状态
            map.put("status", Orders.COMPLETED);
            // 通过映射查询当天的营业额总和
            Double turnover = orderMapper.sumByMap(map);
            // 如果营业额为空，设置为0.0，避免空指针异常
            turnover = turnover == null ? 0.0 : turnover;
            // 将计算得到的营业额添加到营业额列表中
            turnoverList.add(turnover);
        }
        // 使用构建者模式创建TurnoverReportVO对象，并设置日期列表和营业额列表
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }


    /**
     * 根据指定日期范围获取用户统计数据
     * 此方法主要用于生成用户报告，包括新用户数和总用户数，按天统计
     *
     * @param begin 统计开始日期
     * @param end   统计结束日期
     * @return 返回用户报告数据对象，包含日期列表、每天的新用户数和总用户数
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 初始化日期列表，用于存储开始日期到结束日期之间的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        // 循环遍历，直到开始日期等于结束日期，确保日期范围覆盖完整
        while (!begin.equals(end)) {
            begin = begin.plusDays(1); // 将开始日期增加一天
            dateList.add(begin); // 将新的日期添加到列表中
        }

        // 初始化新用户数列表，用于存储每天的新用户数
        List<Integer> newUserList = new ArrayList<>();
        // 初始化总用户数列表，用于存储每天的总用户数
        List<Integer> totalUserList = new ArrayList<>();
        // 遍历日期列表，为每一天统计用户数据
        for (LocalDate date : dateList) {
            // 获取当天的开始时间和结束时间，用于查询当天的用户数据
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            // 准备查询参数，首先查询当天的新用户数
            Map map = new HashMap();
            map.put("end", endTime);
            // 执行查询，获取当天的新用户数
            Integer newUser = userMapper.countByMap(map);

            // 更新查询参数，添加开始时间，用于查询当天的总用户数
            map.put("begin", beginTime);
            // 执行查询，获取当天的总用户数
            Integer totalUser = userMapper.countByMap(map);
            // 将总用户数和新用户数分别添加到对应的列表中
            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        // 构建并返回用户报告数据对象，包含日期列表、每天的总用户数和新用户数
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }


    /**
     * 根据指定日期范围统计订单数据
     * 该方法旨在提供日期范围内每日的订单数量、有效订单数量、总订单数量、有效订单数量占比等统计信息
     *
     * @param begin 统计开始日期
     * @param end   统计结束日期
     * @return 返回订单统计信息的OrderReportVO对象
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 初始化日期列表，用于存储开始日期到结束日期之间的所有日期
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        // 循环添加日期直到达到结束日期
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        // 初始化列表以存储每日的订单数量和有效订单数量
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> vailidOrderCountList = new ArrayList<>();
        // 遍历日期列表，查询每日的订单数量和有效订单数量
        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            // 获取当日所有订单数量
            Integer orderCount = getOrderCount(beginTime, endTime, null);

            // 获取当日有效订单数量
            Integer validOrderCount = getOrderCount(beginTime, endTime, Orders.COMPLETED);

            // 将订单数量和有效订单数量分别添加到列表中
            orderCountList.add(orderCount);
            vailidOrderCountList.add(validOrderCount);
        }
        // 计算总订单数量和有效订单数量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = vailidOrderCountList.stream().reduce(Integer::sum).get();

        // 计算订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) {
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }
        // 构建并返回订单统计信息
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(orderCountList, ","))
                .validOrderCountList(StringUtils.join(vailidOrderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }


    /**
     * 根据给定的时间段和订单状态查询订单数量
     * 此方法用于统计在指定时间段内，特定状态的订单数量
     *
     * @param beginTime 订单开始时间，用于限定统计范围
     * @param endTime   订单结束时间，用于限定统计范围
     * @param status    订单状态，用于筛选特定状态的订单
     * @return 返回符合条件的订单数量
     */
    private Integer getOrderCount(LocalDateTime beginTime, LocalDateTime endTime, Integer status) {
        // 创建参数映射，以便存储查询条件
        Map map = new HashMap();
        // 将开始时间加入参数映射
        map.put("begin", beginTime);
        // 将结束时间加入参数映射
        map.put("end", endTime);
        // 将订单状态加入参数映射
        map.put("status", status);
        // 调用映射查询方法，返回符合条件的订单数量
        return orderMapper.countByMap(map);
    }


    /**
     * 根据指定日期范围获取销售前十的报告
     * 该方法通过统计指定日期范围内的商品销售数据，生成一个包含销售量和商品名称的报告
     * 主要解决了如何从订单数据中提取出指定时间段内销售量排名前十的商品的名称和数量的问题
     *
     * @param begin 开始日期，用于限定销售数据的时间范围
     * @param end   结束日期，用于限定销售数据的时间范围
     * @return SalesTop10ReportVO 返回一个包含销售量排名前十的商品名称和数量的报告对象
     */
    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        // 将开始日期和结束日期转换为LocalDateTime，以便精确匹配订单表中的时间范围
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // 通过orderMapper查询指定时间范围内的销售量排名前十的商品数据
        List<GoodsSalesDTO> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        // 提取商品名称，并将其转换为逗号分隔的字符串
        List<String> names = salesTop10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names, ",");

        // 提取商品销售数量，并将其转换为逗号分隔的字符串
        List<Integer> numbers = salesTop10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers, ",");

        // 构建并返回SalesTop10ReportVO对象，包含商品名称和销售数量的列表
        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }


    /**
     * 导出业务数据
     * 本方法用于生成一份包含最近30天业务数据的Excel报表，并通过HttpServletResponse返回给用户
     * @param response 用于返回报表文件的HttpServletResponse对象
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        //1. 查询数据库，获取营业数据---查询最近30天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        //查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        //2. 通过POI将数据写入到Excel文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            //基于模板文件创建一个新的Excel文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            //获取表格文件的Sheet页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            //填充数据--时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

            //获得第4行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            //获得第5行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            //填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                //查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                //获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            //3. 通过输出流将Excel文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            //关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
