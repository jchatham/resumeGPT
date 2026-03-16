# ResumeGPT
[Live Demo](https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/)

Azure is free tier and it might be over quota. Looking into why now. 

Open API reference here:

http://localhost:8080/swagger-ui/index.html

https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/swagger-ui/index.html

    mvn clean package spring-boot:run

$OPEN_API_KEY is an OpenAI API key.


# Docker build for dockerhub:
    docker build -t jeffchatham/resumegpt:latest .
    docker tag resumegpt:latest jeffchatham/resumegpt:latest
    docker push jeffchatham/resumegpt:latest

# Run for dockerhub version
    docker run -p 8080:8080 -e OPENAI_API_KEY=$OPENAI_API_KEY jeffchatham/resumegpt:latest