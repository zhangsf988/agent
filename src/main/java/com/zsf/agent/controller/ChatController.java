package com.zsf.agent.controller;

import com.zsf.agent.entity.SimpleChatRequest;
import com.zsf.agent.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    ChatService chatService;

    @PostMapping(value = "/simpleChat", produces =  "text/stream;charset=UTF-8")
    public Flux<String> simpleChat(@RequestBody SimpleChatRequest simpleChatRequest){
        Flux<String> stringFlux = chatService.simpleChat(simpleChatRequest);
        return stringFlux;
    }

}
