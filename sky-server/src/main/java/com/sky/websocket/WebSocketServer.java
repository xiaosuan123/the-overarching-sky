package com.sky.websocket;

import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket服务端点，用于处理WebSocket连接和消息。
 */
@Component
@ServerEndpoint("/ws/{sid}")
public class WebSocketServer {

    /**
     * 存储会话的映射，以sessionId作为键。
     */
    private static Map<String, Session> sessionMap = new HashMap<>();

    /**
     * 客户端连接打开时调用。
     *
     * @param session 会话对象
     * @param sid 客户端的sessionId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        System.out.println("客户端：" + sid + "建立连接");
        sessionMap.put(sid, session);
    }

    /**
     * 客户端发送消息时调用。
     *
     * @param message 客户端发送的消息
     * @param sid 客户端的sessionId
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        System.out.println("收到来自客户端：" + sid + "的信息:" + message);
    }

    /**
     * 客户端连接关闭时调用。
     *
     * @param sid 客户端的sessionId
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        System.out.println("连接断开:" + sid);
        sessionMap.remove(sid);
    }

    /**
     * 向所有客户端发送消息。
     *
     * @param message 要发送的消息
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}