# ResumeGPT

ResumeGPT is an AI-powered resume assistant that lets you query, summarize, and interact with your resume using natural language. It leverages section-level vector embeddings to provide precise answers to questions about your experience, education, skills, and projects.

### Features

#### Section-level embeddings – Each resume section is treated as a separate chunk for accurate, context-aware queries.

#### Semantic search – Ask natural language questions and get answers directly from your resume content.

#### Docker & Azure Ready – Run locally or deploy easily on Azure App Service.

#### Open-source & Extensible – Built with Spring Boot, Java, and AI libraries for easy customization.

### Usage

### Clone the repository:

    git clone https://github.com/jchatham/resumeGPT.git
    cd resumeGPT

## Build and run with Docker:

    docker build -t resumegpt:latest .
    docker run -p 8080:8080 -e OPENAI_API_KEY=$OPENAI_API_KEY resumegpt:latest

## Query your resume:
Send prompts to the API and get responses using only your resume content.

## Live Demo
[Live Demo](https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/)

Azure is free tier, and it might be over quota. Yes, it's on Central Canada region, but that one had empty slots for App Service Free Tier.

Open API reference here:

http://localhost:8080/swagger-ui/index.html

https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/swagger-ui/index.html

    mvn clean package spring-boot:run

$OPENAI_API_KEY is an OpenAI API key.


### Docker build for dockerhub:
    sudo docker build -t jeffchatham/resumegpt:v1 .
    sudo docker tag jeffchatham/resumegpt:v1 jeffchatham/resumegpt:v1
    sudo docker push jeffchatham/resumegpt:v1

### Run for dockerhub version
    sudo docker run -p 8080:8080 -e OPENAI_API_KEY=$OPENAI_API_KEY jeffchatham/resumegpt:v1
