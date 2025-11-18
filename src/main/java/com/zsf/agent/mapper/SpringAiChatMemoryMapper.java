package com.zsf.agent.mapper;

import com.zsf.agent.entity.ChatMemoryEntry;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface  SpringAiChatMemoryMapper {
    /**
     * 新增聊天记录
     * @param memory 聊天记录实体
     * @return 影响行数
     */
    int insert(ChatMemoryEntry memory);

    /**
     * 根据会话ID查询聊天记录（按时间升序）
     * @param conversationId 会话ID
     * @return 该会话的所有聊天记录
     */
    List<ChatMemoryEntry> selectByConversationId(@Param("conversationId") String conversationId);

    /**
     * 根据会话ID删除聊天记录
     * @param conversationId 会话ID
     * @return 影响行数
     */
    int deleteByConversationId(@Param("conversationId") String conversationId);
}
