package com.chatham.resume.controller;

import com.chatham.resume.service.RagService;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/chat")
public class ChatController {
    private final RagService ragService;

    public ChatController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping()
    public String ask(@RequestParam String query) {
        return ragService.ask(query);
    }

    @GetMapping(value = "streamingAsk", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamingAsk(@RequestParam String query) {
        return ragService.streamingAsk(query)
                .map(token ->
                        ServerSentEvent.builder(token)
                                .event("token")
                                .build()
                );
    }

    @GetMapping("/debug")
    public Object debug(@RequestParam String query) {
        return (ragService.debug(query));
    }
}