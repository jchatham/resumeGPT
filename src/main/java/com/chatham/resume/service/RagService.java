package com.chatham.resume.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;

@Service
public class RagService {
    private static final Logger log = LoggerFactory.getLogger(RagService.class);
    private final SimpleVectorStore vectorStore;
    private final ChatClient chatClient;

    public RagService(SimpleVectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public String ask(String question) {

        log.info("Vector search for question: {}", question);
        var docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(3)
                        .build()
        );

        log.info("Retrieved {} context documents", docs.size());
        docs.forEach(d -> log.debug("Context chunk: {}", d.getText()));
        String context = docs.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n" + b);


        //What's sent out to the API
        String prompt = """
                Answer the question using the context below.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        log.info("Sending prompt to OpenAI...");

        var response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        log.info("Received response from OpenAI: {}", response);

        return response;
    }


    public Object debug( String query) {
        //Only this way for easier breakpoint placement
        var docs = vectorStore.similaritySearch(
                SearchRequest.builder().query(query).topK(3).build()
        );

        return docs;
    }
}