package com.zsf.agent.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class ChatService {
    @Autowired
    ChatClient simpleChatClient;
    @Autowired
    ChatMemory chatMemory;
    @Autowired
    EmbeddingModel embeddingModel;

    public Flux<String> simpleChat(String userMessage){
        UserMessage message = new UserMessage(userMessage);
        Flux<String> call = simpleChatClient.prompt()
                .messages(message)
                .stream()
                .content();
        return call;
    }
}
