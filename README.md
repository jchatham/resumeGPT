# ResumeGPT
[Live Demo](https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/)

Azure is free tier, and it might be over quota. Yes, it's on Central Canada region, but that one had empty slots for App Service Free Tier. 

Open API reference here:

http://localhost:8080/swagger-ui/index.html

https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/swagger-ui/index.html

    mvn clean package spring-boot:run

$OPENAI_API_KEY is an OpenAI API key.


# Docker build for dockerhub:
    sudo docker build -t jeffchatham/resumegpt:latest .
    sudo docker tag resumegpt:latest jeffchatham/resumegpt:latest
    sudo docker push jeffchatham/resumegpt:latest

# Run for dockerhub version
    sudo docker run -p 8080:8080 -e OPENAI_API_KEY=$OPENAI_API_KEY jeffchatham/resumegpt:latest