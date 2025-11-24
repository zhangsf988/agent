package com.zsf.agent.entity;

import com.zsf.agent.mapper.SpringAiChatMemoryMapper;
import com.zsf.agent.repository.SpringAiChatMemoryRepository;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Primary
public class AgentChatMemory implements ChatMemory { // 实现 1.0.0 版本 ChatMemory 接口

    @Autowired
    private SpringAiChatMemoryRepository springAiChatMemoryRepository;
    @Autowired
    SpringAiChatMemoryMapper springAiChatMemoryMapper;
    // ------------------------------ 核心方法：严格匹配 ChatMemory 接口签名（1.0.0 版本） ------------------------------
    /**
     * 1. 添加消息（接口要求：void add(Message message)）
     */
    @Override
    public void add(String conversationId,List<Message> messages) {
        // 基于传入的消息列表，创建数据库实体并保存
        List<SpringAiChatMemoryEntity> entities = messages.stream()
                .map(message -> {
                    SpringAiChatMemoryEntity entity = new SpringAiChatMemoryEntity();
                    entity.setConversationId(conversationId);
                    entity.setType(mapMessageTypeToString(message.getMessageType()));
                    entity.setContent(message.getText());
                    entity.setTimestamp(LocalDateTime.now());
                    return entity;
                })
                .collect(Collectors.toList());
        
        springAiChatMemoryRepository.saveAll(entities);
    }


    @Override
    public List<Message> get(String conversationId) {
        SpringAiChatMemoryEntity springAiChatMemoryEntity = new SpringAiChatMemoryEntity();
        springAiChatMemoryEntity.setConversationId(conversationId);
        // 从数据库查询指定会话的所有消息，并转换为 Message 对象列表
        return springAiChatMemoryRepository.findAll(Example.of(springAiChatMemoryEntity))
                .stream()
                .map(this::mapEntityToMessage)
                .collect(Collectors.toList());
    }

    @Override
    public void clear(String conversationId) {
        // 删除指定会话的所有消息记录
        springAiChatMemoryRepository.deleteByConversationId(conversationId);
    }

    // ------------------------------ 辅助方法（无变更） ------------------------------
    // 生成 36 位 UUID（匹配表中 conversation_id 字段）
    private String generateConversationId() {
        return UUID.randomUUID().toString();
    }

    // MessageType → 表中 type 字段（USER/ASSISTANT 等，长度≤10）
    private String mapMessageTypeToString(MessageType messageType) {
        return switch (messageType) {
            case USER -> "USER";
            case ASSISTANT -> "ASSISTANT";
            case SYSTEM -> "SYSTEM";
            case TOOL -> "TOOL";
//            case IMAGE -> "IMAGE";
//            case VOICE -> "VOICE";
//            default -> "UNKNOWN";
        };
    }

    // 表中 type 字段 → MessageType
    private MessageType mapStringToMessageType(String typeStr) {
        return switch (typeStr) {
            case "USER" -> MessageType.USER;
            case "ASSISTANT" -> MessageType.ASSISTANT;
            case "SYSTEM" -> MessageType.SYSTEM;
            case "TOOL" -> MessageType.TOOL;
//            case "IMAGE" -> MessageType.IMAGE;
//            case "VOICE" -> MessageType.VOICE;
            default -> throw new IllegalStateException("Unexpected value: " + typeStr);
        };
    }

    // 数据库实体 → Spring AI Message
    private Message mapEntityToMessage(SpringAiChatMemoryEntity entity) {
        MessageType messageType = mapStringToMessageType(entity.getType());
        String content = entity.getContent();
        // 根据消息类型返回框架支持的具体Message实现
        return switch (messageType) {
            case USER -> new UserMessage(content);
            case ASSISTANT -> new AssistantMessage(content);
            case SYSTEM -> new SystemMessage(content);
//            case TOOL -> new ToolMessage(content); // 若需要支持工具消息
            default -> throw new IllegalStateException("不支持的消息类型: " + messageType);
        };
    }
}