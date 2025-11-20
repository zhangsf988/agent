package com.zsf.agent.service;

import com.zsf.agent.entity.SimpleChatRequest;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    public Flux<String> simpleChat(SimpleChatRequest simpleChatRequest){
        System.out.println("Message: " + simpleChatRequest.getMessage());
        System.out.println("Memory ID: " + simpleChatRequest.getMemoryId());
//        float[] embed = embeddingModel.embed(userMessage);
        Flux<String> call = simpleChatClient.prompt()
                .system("你是一个智能助手,帮助用户解答问题")
                .user(simpleChatRequest.getMessage())
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, simpleChatRequest.getMemoryId()) )
                .stream()
                .content();
        return call;
    }
}
