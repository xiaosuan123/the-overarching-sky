package com.sky.task;

import com.sky.websocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/**
 * 负责通过 WebSocket 与客户端通信的任务类。
 */
@Component
public class WebSocketTask {
    /**
     * 用于发送消息到所有 WebSocket 客户端的 WebSocket 服务器实例。
     */
    @Autowired
    private WebSocketServer webSocketServer;
    /**
     * 发送当前时间的消息到所有连接的 WebSocket 客户端。
     * 消息格式为 "这是来自服务端的消息：HH:mm:ss"，其中时间是服务器当前的时间。
     */
    public void sendMessageToClient() {
        webSocketServer.sendToAllClient("这是来自服务端的消息："+ DateTimeFormatter
                .ofPattern("HH:mm:ss")
                .format(LocalDateTime.now()));
    }
}
