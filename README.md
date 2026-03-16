# ResumeGPT
[Live Demo](https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/)
Live Demo URL could change and Azure might put it to sleep without regular usage. It starts up fast though. 

Needs an OpenAI API key set to environmental variable OPEN_API_KEY. 

Open API reference here:
http://localhost:8080/swagger-ui/index.html
https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/swagger-ui/index.html

    mvn clean package spring-boot:run

Environmental variable $OPEN_API_KEY is an OpenAI API key.


# Docker build for dockerhub:
    docker build -t jeffchatham/resumegpt:latest .
    docker tag resumegpt:latest jeffchatham/resumegpt:latest
    docker push jeffchatham/resumegpt:latest

# Run for dockerhub version
    docker run -p 8080:8080 -e OPEN_API_KEY=$OPEN_API_KEY jeffchatham/resumegpt:latest