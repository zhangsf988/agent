package com.zsf.agent.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "SPRING_AI_CHAT_MEMORY")// 指定 schema（agent）和表名
public class SpringAiChatMemoryEntity  {

    // 主键 id：自增（对应表中 AUTO_INCREMENT）
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 依赖数据库自增
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    // 会话编号（36位 UUID，非空）
    @Column(name = "conversation_id", nullable = false, length = 36)
    private String conversationId;

    // 会话内容（非空 TEXT）
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // 消息类型（非空，长度≤10，匹配表约束）
    @Column(name = "type", nullable = false, length = 10)
    private String type;

    // 时间戳（非空，自动填充）
    @Column(name = "timestamp", nullable = false)
    @CreationTimestamp // 插入时自动填充当前时间，无需手动设置
    private LocalDateTime timestamp;

    // 功能类型（非空，匹配表约束）
    @Column(name = "function_type", nullable = false, length = 30)
    private String functionType;
}
