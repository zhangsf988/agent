package com.zsf.agent.entity;

import lombok.Data;

@Data
public class SimpleChatRequest {
    private String message;
    private String memoryId;
    private String functionType;
}
