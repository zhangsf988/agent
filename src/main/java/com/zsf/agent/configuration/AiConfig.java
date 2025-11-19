package com.zsf.agent.configuration;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiEmbeddingModel;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Value("${spring.ai.openai.api-key}")
    String apiKey;
    
    @Value("${spring.ai.openai.base-url}")
    String baseUrl;

    @Value("${spring.ai.openai.embedding.options.model")
    String embeddingModel;
    @Bean
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(jdbcChatMemoryRepository)
                .maxMessages(100)
                .build();
    }

//    @Bean
//    public EmbeddingModel embeddingModel() {
//        OpenAiApi openAiApi = new OpenAiApi.Builder()
//                .baseUrl(baseUrl)
//                .apiKey(apiKey)
//                .build();
//        OpenAiEmbeddingOptions openAiEmbeddingOptions = new OpenAiEmbeddingOptions();
//        openAiEmbeddingOptions.setModel(embeddingModel);
//        return new OpenAiEmbeddingModel(openAiApi, openAiEmbeddingOptions);
//    }
}