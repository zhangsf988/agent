package com.zsf.agent.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {
    @Autowired
    ChatClient simpleChatClient;

    public String simpleChat(String userMessage){
        UserMessage message = new UserMessage(userMessage);
        String call = simpleChatClient.prompt().messages(message).call().content();
        return call;
    }
}
