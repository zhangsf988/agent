package com.zsf.agent.controller;

import com.zsf.agent.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    @RequestMapping("/simpleChat")
    public Flux<String> simpleChat(String userMessage){
        Flux<String> stringFlux = chatService.simpleChat(userMessage);
        return stringFlux;
    }

}
