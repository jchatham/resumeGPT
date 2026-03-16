# ResumeGPT

## [Live Demo](https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/)
Live Demo URL could change and Azure might put it to sleep without regular usage. It starts up fast though. 

Needs an OpenAI API key set to environmental variable OPEN_API_KEY. 

OpenAPI reference here:
http://localhost:8080/swagger-ui/index.html
https://resumegpt-fudfcxbjbxcegvbk.canadacentral-01.azurewebsites.net/swagger-ui/index.html

TODO: Create frontend to consume API. 

mvn clean package
docker build -t resumegpt .
# $OPEN_API_KEY is an OpenAI API key.
docker run -p 8080:8080 -e OPEN_API_KEY=$OPEN_API_KEY resumegpt
# Build for dockerhub:
docker build -t jeffchatham/resumegpt:latest .
docker tag resumegpt:latest jeffchatham/resumegpt:latest
docker push jeffchatham/resumegpt:latest

# Run for dockerhub version
docker run -p 8080:8080 -e OPEN_API_KEY=$OPEN_API_KEY jeffchatham/resumegpt:latest