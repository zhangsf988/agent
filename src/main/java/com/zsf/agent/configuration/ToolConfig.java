package com.zsf.agent.configuration;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolConfig {

    @Tool(name = "当前时间",description = "获取当前时间")
    public String getCurrentTime() {
        return "The current time is " + new java.util.Date();
    }

}
