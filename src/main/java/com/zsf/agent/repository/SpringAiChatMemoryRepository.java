package com.zsf.agent.repository;

import com.zsf.agent.entity.SpringAiChatMemoryEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

// 泛型：实体类 + 主键类型（Integer，对应表中 id 的 INT 类型）
@Repository
public interface SpringAiChatMemoryRepository extends JpaRepository<SpringAiChatMemoryEntity, String> {

    /**
     * 核心方法：按会话编号查询历史消息（按时间戳升序，保证上下文顺序）
     */
    List<SpringAiChatMemoryEntity> findByConversationIdOrderByTimestampAsc(String conversationId);


    /**
     * 清空某会话的所有记忆（按 conversation_id 批量删除）
     */
    @Modifying // 标识修改操作（DELETE/UPDATE）
    @Query("DELETE FROM SpringAiChatMemoryEntity m WHERE m.conversationId = :conversationId")
    void deleteByConversationId(@Param("conversationId") String conversationId);

    List<SpringAiChatMemoryEntity> queryAllByConversationId(String conversationId);
}
