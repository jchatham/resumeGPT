package com.chatham.resume.service;

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class RagService {
    private static final Logger log = LoggerFactory.getLogger(RagService.class);
    public static final int TOP_K = 6;
    private final SimpleVectorStore vectorStore;
    private final ChatClient chatClient;

    public RagService(SimpleVectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    public String ask(String question) {

        String prompt = getPrompt(question);

        var response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        log.info("Received response from OpenAI: {}", response);

        return response;
    }

    public Flux<String> streamingAsk(String question) {
        final AtomicReference<Character> lastCharRef = new AtomicReference<>(' ');

        return chatClient.prompt()
                .user(getPrompt(question))
                .stream()
                .content()
                .map(token -> {
                    if (token.isEmpty()) return token;

                    char lastChar = lastCharRef.get();
                    char firstChar = token.charAt(0);

                    // insert a space if previous char and first char are both letters/digits
                    String result = (Character.isLetterOrDigit(lastChar) && Character.isLetterOrDigit(firstChar))
                            ? " " + token
                            : token;

                    // update last char
                    lastCharRef.set(token.charAt(token.length() - 1));
                    return result;
                });
    }

    /**
     * Used to build prompt, uses vector store to avoid API calls.
     * @param question user submitted question.
     * @return the prompt.
     */
    private @NonNull String getPrompt(String question) {
        log.info("Vector search for question: {}", question);
        var docs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(TOP_K)
                        .build()
        );

        log.info("Retrieved {} context documents", docs.size());
        docs.forEach(d -> log.debug("Context chunk: {}", d.getText()));
        String context = docs.stream()
                .map(Document::getText)
                .reduce("", (a, b) -> a + "\n" + b);


        //What's sent out to the API
        String prompt = """
                Please answer using only the content from the resume.
                Do not include any header lines like "=== EXPERIENCE ===" or "=== EDUCATION ===".
                
                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        log.info("Prompt {}", prompt);
        log.info("Sending prompt to OpenAI...");
        return prompt;
    }


    public Object debug( String query) {
        //Only this way for easier breakpoint placement
        var docs = vectorStore.similaritySearch(
                SearchRequest.builder().query(query).topK(3).build()
        );

        return docs;
    }
}