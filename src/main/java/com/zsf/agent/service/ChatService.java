package com.zsf.agent.service;

import com.zsf.agent.entity.AgentChatMemory;
import com.zsf.agent.entity.SimpleChatRequest;
import com.zsf.agent.entity.SpringAiChatMemoryEntity;
import com.zsf.agent.repository.SpringAiChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
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
    AgentChatMemory agentChatMemory;


    public Flux<String> simpleChat(SimpleChatRequest simpleChatRequest){
        System.out.println("Message: " + simpleChatRequest.getMessage());
        System.out.println("Memory ID: " + simpleChatRequest.getMemoryId());
        List<Message> messages = agentChatMemory.get(simpleChatRequest.getMemoryId());
        Flux<String> call = simpleChatClient.prompt()
                .system("你是一个智能助手,帮助用户解答问题")
                .messages(messages)
                .user(simpleChatRequest.getMessage())
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, simpleChatRequest.getMemoryId()) )
                .stream()
                .content();
        return call;
    }
    
    public List<Message> getHistory(String conversationId) {
        return agentChatMemory.get(conversationId);
    }
}
