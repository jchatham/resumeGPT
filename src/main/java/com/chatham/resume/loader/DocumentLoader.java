package com.chatham.resume.loader;

import jakarta.annotation.PostConstruct;
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
import java.util.List;
import java.util.Map;

/**
 * Loads a document from classpath if the vector embeddings haven't been generated.
 */
@Service
public class DocumentLoader {
    private static final Logger log = LoggerFactory.getLogger(DocumentLoader.class);
    private final SimpleVectorStore vectorStore;

    @Value("classpath:static/documents/Chatham-resume.txt")
    private Resource resumeText;
    @Value("classpath:static/documents/Chatham-resume.pdf")
    private Resource resumePDF;
    @Value("${vector.store.name}")
    private String vectorStoreName;

    public DocumentLoader(SimpleVectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void init() throws IOException {
        File storeFile = new File(vectorStoreName);

        //Vector store exists, skip load
        if (storeFile.exists()) {
            log.info("Vector store JSON already exists, skipping embedding call.");
        } else {
            //Reads file into String
            String resumeStr = resumeText.getContentAsString(Charset.defaultCharset());

            //Splits text into chunks for better search
            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(400)
                    .build();

            //Create with metadata resume to improve results
            Map<String,Object> metadata = Map.of("source","resume");
            List<Document> docs = splitter.apply(
                    List.of(new Document(resumeStr, metadata))
            );

            vectorStore.add(docs);

            vectorStore.save(storeFile);
        }
    }
}