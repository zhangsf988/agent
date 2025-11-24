package com.zsf.agent.service;

import com.alibaba.fastjson2.JSONObject;
import com.zsf.agent.entity.AgentChatMemory;
import com.zsf.agent.entity.SimpleChatRequest;
import com.zsf.agent.entity.SpringAiChatMemoryEntity;
import com.zsf.agent.repository.SpringAiChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
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
    @Autowired
    VectorStore vectorStore;
    @Autowired
    EmbeddingModel embeddingModel;


    public Flux<String> simpleChat(SimpleChatRequest simpleChatRequest){
        System.out.println("Message: " + simpleChatRequest.getMessage());
        System.out.println("Memory ID: " + simpleChatRequest.getMemoryId());
        float[] embed = embeddingModel.embed(simpleChatRequest.getMessage());
        System.out.println("embed:"+ JSONObject.toJSONString(embed));
        List<Document> documents = vectorStore.similaritySearch(simpleChatRequest.getMessage());
        System.out.println("documents:"+ JSONObject.toJSONString(documents));
        Flux<String> call = simpleChatClient.prompt()
                .user(simpleChatRequest.getMessage())
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, simpleChatRequest.getMemoryId()))
                .stream()
                .content();
        return call;
    }
    
    public List<Message> getHistory(String conversationId) {
        return agentChatMemory.get(conversationId);
    }
}
