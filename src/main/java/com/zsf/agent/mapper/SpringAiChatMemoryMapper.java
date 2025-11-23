package com.zsf.agent.mapper;


import com.zsf.agent.entity.SpringAiChatMemoryEntity;
import com.zsf.agent.repository.SpringAiChatMemoryRepository;
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
    int insert(SpringAiChatMemoryEntity memory);

    /**
     * 根据会话ID查询聊天记录（按时间升序）
     * @param conversationId 会话ID
     * @return 该会话的所有聊天记录
     */
    List<SpringAiChatMemoryEntity> selectByConversationId(@Param("conversationId") String conversationId,@Param("functionType") String functionType);

    /**
     * 根据会话ID删除聊天记录
     * @param conversationId 会话ID
     * @return 影响行数
     */
    int deleteByConversationId(@Param("conversationId") String conversationId);

    List<String> selectChatListByFunctionType(@Param("functionType") String functionType);
}
