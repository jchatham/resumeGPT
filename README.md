# ResumeGPT
[Live Demo](https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/)

Azure might put the live demo to sleep without regular usage. It starts up fast though. 

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
    docker run -p 8080:8080 -e OPEN_API_KEY=$OPEN_API_KEY jeffchatham/resumegpt:latest
