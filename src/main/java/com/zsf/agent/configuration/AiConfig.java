package com.zsf.agent.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Autowired
    OpenAiChatModel openAiChatModel;
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }

    @Bean
    ChatClient SimpleChatClient(){
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("你是一个智能助手，帮助我解决各种问题")
                .build();
    }
}
