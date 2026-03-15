package com.chatham.resume.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * SimpleVectorStore configuration. In-memory and skips load if vector store json exists.
 */
@Configuration
public class SimpleVectorStoreConfig {
    private static final Logger log = LoggerFactory.getLogger(SimpleVectorStoreConfig.class);

    @Value("${vector.store.name}")
    private String vectorStoreName;

    @Bean
    public SimpleVectorStore vectorStore(EmbeddingModel embeddingModel) {
        File storeFile = new File(vectorStoreName);
        //builder pattern to create store
        SimpleVectorStore simpleVectorStore = SimpleVectorStore
                .builder(embeddingModel)
                .build();


        //If file exists for given path load it - avoid OpenAI API embeddings fees
        if (storeFile.exists()) {
            simpleVectorStore.load(storeFile);
            log.info("Loaded vector store from disk");
        }

        log.info("Embedding model: {}", embeddingModel.getClass());

        return simpleVectorStore;
    }

}
