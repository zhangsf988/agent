package com.zsf.agent.entity;

import lombok.*;
import org.springframework.ai.chat.messages.Message;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMemoryEntry  {
    // 会话唯一标识
    private  String conversationId;

    // 消息内容
    private  String content;

    // 消息类型（USER/ASSISTANT 等）
    private  String type;

    // 消息时间戳
    private Timestamp timestamp;
}
