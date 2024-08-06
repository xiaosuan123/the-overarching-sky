package com.sky.controller.notify;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.properties.WeChatProperties;
import com.sky.service.OrderService;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 微信支付回调通知控制器。
 */
@RestController
@RequestMapping("/notify")
@Slf4j
public class PayNotifyController {

    // 通过Spring的自动注入获取订单服务
    @Autowired
    private OrderService orderService;

    // 通过Spring的自动注入获取微信支付属性配置
    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 处理微信支付成功回调。
     * 当微信支付平台调用此接口时，会触发支付成功后续的业务逻辑。
     *
     * @param request  HttpServletRequest对象，包含请求信息
     * @param response HttpServletResponse对象，用于构造响应
     * @throws Exception 可能抛出的异常
     */
    @RequestMapping("/paySuccess")
    public void paySuccessNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 读取请求数据
        String body = readData(request);
        // 日志记录原始请求数据
        log.info("支付成功回调：{}", body);

        // 解密请求数据
        String plainText = decryptData(body);
        // 日志记录解密后的请求数据
        log.info("解密后的文本：{}", plainText);

        // 解析JSON数据
        JSONObject jsonObject = JSON.parseObject(plainText);
        // 提取商户平台订单号和微信支付交易号
        String outTradeNo = jsonObject.getString("out_trade_no");
        String transactionId = jsonObject.getString("transaction_id");

        // 日志记录订单号和交易号
        log.info("商户平台订单号：{}", outTradeNo);
        log.info("微信支付交易号：{}", transactionId);

        // 调用订单服务完成支付成功的业务逻辑
        orderService.paySuccess(outTradeNo);

        // 响应微信支付平台
        responseToWeixin(request, response);
    }

    /**
     * 从请求中读取数据。
     *
     * @param request HttpServletRequest对象
     * @return 从请求中读取的字符串数据
     * @throws IOException 如果读取请求数据时发生I/O错误
     */
    private String readData(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (result.length() > 0) {
                result.append("\n");
            }
            result.append(line);
        }
        return result.toString();
    }

    /**
     * 解密微信支付回调数据。
     *
     * @param body 加密的数据
     * @return 解密后的字符串数据
     * @throws Exception 如果解密过程中发生错误
     */
    private String decryptData(String body) throws Exception {
        JSONObject resultObject = JSON.parseObject(body);
        JSONObject resource = resultObject.getJSONObject("resource");
        String ciphertext = resource.getString("ciphertext");
        String nonce = resource.getString("nonce");
        String associatedData = resource.getString("associated_data");

        // 使用AES工具类进行解密
        AesUtil aesUtil = new AesUtil(weChatProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        String plaintext = aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);
        return plaintext;
    }

    /**
     * 构造响应给微信支付平台的数据。
     *
     * @param request  HttpServletRequest对象
     * @param response HttpServletResponse对象
     * @throws Exception 如果响应数据写入时发生I/O错误
     */
    private void responseToWeixin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setStatus(200);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code", "SUCCESS");
        map.put("message", "SUCCESS");
        response.setHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
        response.getOutputStream().write(JSONUtils.toJSONString(map).getBytes(StandardCharsets.UTF_8));
        response.flushBuffer();
    }
}