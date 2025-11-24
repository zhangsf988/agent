package com.zsf.agent;

import com.zsf.agent.entity.AgentChatMemory;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AgentApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    OpenAiChatModel openAiChatModel;
    @Autowired
    AgentChatMemory agentChatMemory;
    @Autowired
    VectorStore vectorStore;
    @Test
    void testRag() {
        System.out.println("RAG 测试开始");

        ChatClient chatClient = ChatClient.builder(openAiChatModel)
                .defaultSystem("你是一个长亮公司的人事、行政助理，根据知识库中的员工手册文档，回答员工的问题，在回答的过程中，只在知识库进行检索，不进行联网查询，避免出现信息不正确的问题")
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(agentChatMemory).build(), // chat-memory advisor
                        QuestionAnswerAdvisor.builder(vectorStore).build() // RAG advisor
                )
                .build();
        String content = chatClient.prompt().user("告诉我职位职级职等划分").call().content();
        System.out.println(content);

    }

}
