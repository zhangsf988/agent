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
                .defaultSystem("你是一个长亮公司的人事、行政助理，根据知识库中的员工手册文档，回答员工的问题，在回答的过程中，只在知识库进行检索，不进行联网查询，避免出现信息不正确的问题")
                .defaultTools(new ToolConfig())
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(agentChatMemory).build(), // chat-memory advisor
                        QuestionAnswerAdvisor.builder(vectorStore).build() // RAG advisor
                )
                .build();
    }
}