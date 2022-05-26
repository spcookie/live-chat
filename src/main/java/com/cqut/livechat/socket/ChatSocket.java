package com.cqut.livechat.socket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqut.livechat.constant.MessageType;
import com.cqut.livechat.dto.message.AuxiliaryMessageDto;
import com.cqut.livechat.entity.message.ChatImageMessage;
import com.cqut.livechat.entity.message.CommonMessage;
import com.cqut.livechat.service.message.AuxiliaryMessageCache;
import com.cqut.livechat.service.message.CommonMessageService;
import com.cqut.livechat.service.message.MessageHandlerAdapter;
import com.cqut.livechat.utils.SocketUserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

/**
 * @author Augenstern
 * @date 2022/5/21
 */
@Slf4j
@Component
public class ChatSocket extends AbstractWebSocketHandler {

    @Autowired
    private MessageHandlerAdapter adapter;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        long id = SocketUserUtil.getLoginUserId(session);
        log.info("Socket连接成功 -> " + "id:" + id + " address:" + session.getRemoteAddress());
        // 保存连接
        ChatSocketCache.add(id, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        InetSocketAddress remoteAddress = session.getRemoteAddress();
        String payload = message.getPayload();
        log.info("收到消息 -> " + remoteAddress + ":" + payload);
        // 文本消息转换json对象
        JSONObject jsonObject = JSON.parseObject(payload);
        // 获取消息类型
        MessageType messageType = getMessageType(jsonObject);
        // 将消息封装成对象
        CommonMessage commonMessage = getMessage(jsonObject, messageType);
        // 获取消息处理器
        CommonMessageService handler = adapter.adaptation(messageType);
        // 处理消息
        handler.handler(session, commonMessage);
    }

    private MessageType getMessageType(JSONObject jsonObject) {
        return jsonObject.getObject("type", MessageType.class);
    }

    private CommonMessage getMessage(JSONObject jsonObject, MessageType messageType) {
        return jsonObject.getObject("message", messageType.getType());
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        // 获取二进制消息
        ByteBuffer payload = message.getPayload();
        byte[] bytes = payload.array();
        log.info("收到二进制消息 -> " + payload);
        // 将二进制数据封装到对象
        ChatImageMessage chatImageMessage = new ChatImageMessage();
        chatImageMessage.setImage(bytes);
        // 获取缓存中要传输的信息类型
        AuxiliaryMessageDto auxiliaryMessageDto = AuxiliaryMessageCache.get(session);
        MessageType transmissionType = auxiliaryMessageDto.getTransmissionType();
        // 从缓存中获取消息的接收者
        Long target = auxiliaryMessageDto.getTarget();
        chatImageMessage.setTarget(target);
        // 获取消息处理器
        CommonMessageService handler = adapter.adaptation(transmissionType);
        // 处理消息
        handler.handler(session, chatImageMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        long id = SocketUserUtil.getLoginUserId(session);
        // 连接断开后将套接字清除
        ChatSocketCache.del(id);
        log.info("Socket连接关闭 -> " + "id:" + id);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("Socket错误 -> " + exception);
    }
}
