package com.zsf.agent.configuration;

import com.zsf.agent.entity.AgentChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ClientConfig {
    @Autowired
    OpenAiChatModel openAiChatModel;
    @Autowired
    AgentChatMemory agentChatMemory;
    @Autowired
    VectorStore vectorStore;
    
    @Bean
    ChatClient simpleChatClient(){
        return ChatClient.builder(openAiChatModel)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(agentChatMemory).build(), // chat-memory advisor
                        QuestionAnswerAdvisor.builder(vectorStore).build() // RAG advisor
                )
                .build();
    }
}