package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 数据统计相关接口
 */
@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;

    /**
     * 获取营业额统计信息
     * <p>
     * 本方法通过GET请求方式提供营业额统计信息它接收开始日期和结束日期作为参数，
     * 并返回在这段时间内的营业额统计结果使用@DateTimeFormat注解来确保日期参数的正确格式化
     *
     * @param begin 开始日期，用于指定统计周期的开始时间
     * @param end   结束日期，用于指定统计周期的结束时间
     * @return 返回一个Result对象，包含营业额统计信息（TurnoverReportVO）
     * <p>
     * 使用者可以是需要了解营业额统计信息的管理人员或相关利益者
     * 方法内部通过调用reportService的getTurnoverStatistics方法来获取统计结果，
     * 并将结果包装在Result对象中返回
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("营业额统计：{},{}", begin, end);
        return Result.success(reportService.getTurnoverStatistics(begin, end));
    }

    /**
     * 查询用户统计信息
     *
     * 通过指定的开始日期和结束日期，从报告服务中获取用户统计信息
     * 该方法使用了@GetMapping注解，指定请求的URL为"/userStatistics"，并使用@ApiOperation注解描述了方法的功能
     *
     * @param begin 开始日期，使用@DateTimeFormat注解指定日期格式为"yyyy-MM-dd"
     * @param end 结束日期，同样使用@DateTimeFormat注解指定日期格式为"yyyy-MM-dd"
     * @return 返回一个Result对象，其中包含用户统计信息（UserReportVO类型）
     *
     * 该方法首先记录了一条信息日志，内容为"用户统计：{},{}"，其中的{},{}会被实际的开始日期和结束日期替换
     * 然后，方法调用reportService的getUserStatistics方法，传入开始日期和结束日期，获取用户统计信息
     * 最后，将获取到的用户统计信息封装到Result对象中，并设置成功状态，返回给调用者
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("用户统计：{},{}", begin, end);
        return Result.success(reportService.getUserStatistics(begin, end));
    }
    /**
     * 查询订单统计信息
     *
     * 通过指定的开始日期和结束日期，从报告服务中获取订单统计信息
     * 该方法使用了@GetMapping注解，指定请求的URL为"/ordersStatistics"，并使用@ApiOperation注解描述了方法的功能
     *
     * @param begin 开始日期，使用@DateTimeFormat注解指定日期格式为"yyyy-MM-dd"
     * @param end 结束日期，同样使用@DateTimeFormat注解指定日期格式为"yyyy-MM-dd"
     * @return 返回一个Result对象，其中包含订单统计信息（OrderReportVO类型）
     *
     * 该方法首先记录了一条信息日志，内容为"订单统计：{},{}"，其中的{},{}会被实际的开始日期和结束日期替换
     * 然后，方法调用reportService的getOrderStatistics方法，传入开始日期和结束日期，获取订单统计信息
     * 最后，将获取到的订单统计信息封装到Result对象中，并设置成功状态，返回给调用者
     */

    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> ordersStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("订单统计：{},{}", begin, end);
        return Result.success(reportService.getOrderStatistics(begin, end));
    }
    /**
     * 查询销量排名前10的商品
     *
     * 通过GET请求，获取指定日期范围内销量排名前10的商品报告
     *
     * @param begin 开始日期，格式为yyyy-MM-dd
     * @param end 结束日期，格式为yyyy-MM-dd
     * @return 返回销量排名前10的商品报告，封装在Result对象中
     */
    @GetMapping("/top10")
    @ApiOperation("销量排名top10")
    public Result<SalesTop10ReportVO> top10(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("销量排名top10：{},{}", begin, end);
        return Result.success(reportService.getSalesTop10(begin, end));
    }
    /**
     * 导出运营数据报表
     * 通过HTTP GET请求导出运营数据报表
     * 使用HttpServletResponse直接将报表数据返回给客户端
     * 此方法不接受任何参数，报表的具体数据和格式由报告服务（reportService）决定
     *
     * @param response HttpServletResponse对象，用于将报表数据作为响应返回给客户端
     */
    @GetMapping("/export")
    @ApiOperation("导出运营数据报表")
    public void export(HttpServletResponse response){
        reportService.exportBusinessData(response);
    }
}
