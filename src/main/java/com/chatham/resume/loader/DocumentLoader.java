package com.chatham.resume.loader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Loads a document from classpath if the vector embeddings haven't been generated.
 * Reads resume.json structured by sections.
 */
@Service
public class DocumentLoader {
    private static final Logger log = LoggerFactory.getLogger(DocumentLoader.class);
    public static final int CHUNK_SIZE = 200;
    public static final int MIN_CHUNK_SIZE_CHARS = 350;

    private final SimpleVectorStore vectorStore;

    @Value("classpath:static/documents/resume.json")
    private Resource resumeJSON;

    @Value("${vector.store.name}")
    private String vectorStoreName;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public DocumentLoader(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() throws IOException {
        File storeFile = new File(vectorStoreName);

        // Vector store exists, skip embedding generation
        if (storeFile.exists()) {
            log.info("Vector store JSON already exists, skipping embedding call.");
            return;
        }

        // Parse JSON file into Map<String, Object>
        Map<String, Object> resumeMap = objectMapper.readValue(
                resumeJSON.getInputStream(),
                new TypeReference<>() {}
        );

        List<Document> finalDocs = getDocuments(resumeMap);

        vectorStore.add(finalDocs);
        vectorStore.save(storeFile);
        log.info("vector-store.json created and saved to disk with {} chunks.", finalDocs.size());
    }

    /**
     * Loads documents from JSON.
     * @param resumeMap JSON map.
     * @return list of org.springframework.ai.document.Document populated from JSON.
     * @throws JsonProcessingException on error.
     */
    private @NonNull List<Document> getDocuments(Map<String, Object> resumeMap) throws JsonProcessingException {
        List<Document> sectionDocs = new ArrayList<>();
        // Convert each section in JSON into a Document
        for (Map.Entry<String, Object> entry : resumeMap.entrySet()) {
            String sectionName = entry.getKey();
            Object sectionContent = entry.getValue();

            String textContent;

            // Flatten content depending on type
            if (sectionContent instanceof String) {
                textContent = (String) sectionContent;
            } else {
                // If it's a Map or List, convert to pretty JSON string
                textContent = objectMapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(sectionContent);
            }

            Map<String, Object> metadata = Map.of(
                    "source", "resume",
                    "section", sectionName
            );

            sectionDocs.add(new Document(textContent, metadata));
        }

        // Token splitter for large sections (skip INFO)
        TokenTextSplitter splitter = TokenTextSplitter.builder().build();
        List<Document> finalDocs = new ArrayList<>();

        sectionDocs.forEach(doc -> {
            String section = (String) doc.getMetadata().get("section");
            if ("INFO".equalsIgnoreCase(section)) {
                // Keep INFO together
                finalDocs.add(doc);
            } else {
                finalDocs.addAll(splitter.apply(List.of(doc)));
            }
        });
        return finalDocs;
    }
}