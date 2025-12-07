package com.zsf.agent.configuration;

import com.zsf.agent.entity.AgentChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.tools.Tool;

@Component
public class ClientConfig {
    @Autowired
    OpenAiChatModel openAiChatModel;
    @Autowired
    AgentChatMemory agentChatMemory;
    @Autowired
    VectorStore vectorStore;
    @Autowired
    ToolConfig toolConfig;



    @Bean
    ChatClient simpleChatClient() {
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("你是一个长亮公司的人事、行政助理，如果用户要询问员工手册相关内容，则根据知识库中的员工手册文档，回答员工的问题，在回答的过程中，" +
                        "只在知识库进行检索，不进行联网查询，避免出现信息不正确的问题。" +
                        "如果用户是有其他申请，则配合用户处理业务")
                .defaultTools(toolConfig)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(agentChatMemory).build(), // chat-memory advisor
                        QuestionAnswerAdvisor.builder(vectorStore).searchRequest(SearchRequest
                                        .builder()
                                        .topK(5)
                                        .similarityThreshold(0.5)
                                        .build())
                                .build(), // RAG advisor
                        SimpleLoggerAdvisor.builder().build()
                )
                .build();
    }

    @Bean
    ChatClient pdfChatClient() {
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("根据提供的文档作为知识库及回答问题的参考来回答用户提出的问题，如果用户问的内容在文档中没有对应回答，就直接对用户进行告知，不要进行无关的回答。")
                .defaultTools(new ToolConfig())
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(agentChatMemory).build(), // chat-memory advisor
                        SimpleLoggerAdvisor.builder().build()
                )
                .build();
    }
}