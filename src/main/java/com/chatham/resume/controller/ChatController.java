package com.chatham.resume.controller;

import com.chatham.resume.service.RagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private RagService ragService;

    @GetMapping
    public String ask(@RequestParam String query) {
        return ragService.ask(query);
    }

    @GetMapping("/debug")
    public Object debug(@RequestParam String query) {
        return (ragService.debug(query));
    }
}