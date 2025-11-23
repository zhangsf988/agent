package com.zsf.agent.service;


import com.zsf.agent.entity.SpringAiChatMemoryEntity;
import com.zsf.agent.mapper.SpringAiChatMemoryMapper;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMemoryService {
    @Autowired
    private ChatMemory chatMemory;
    @Autowired
    private SpringAiChatMemoryMapper springAiChatMemoryMapper;

    /**
     * 获取指定会话的历史消息
     * @param conversationId 会话ID
     * @return 历史消息列表
     */
    public List<SpringAiChatMemoryEntity> getHistory(String conversationId, String functionType) {
        return springAiChatMemoryMapper.selectByConversationId(conversationId,functionType);
    }

    /**
     * 清除指定会话的记忆
     * @param conversationId 会话ID
     */
    public void clearMemory(String conversationId) {
        chatMemory.clear(conversationId);
    }

    /**
     * 获取会话中的消息数量
     * @param conversationId 会话ID
     * @return 消息数量
     */
    public int getMessageCount(String conversationId) {
        return chatMemory.get(conversationId).size();
    }
}
