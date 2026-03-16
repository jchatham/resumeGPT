Needs an OpenAI API key set to environmental variable OPEN_API_KEY. 

OpenAPI reference here:
http://localhost:8080/swagger-ui/index.html

TODO: Create frontend to consume API. 

mvn clean package
docker build -t resumegpt .
# $OPEN_API_KEY is an OpenAI API key.
docker run -p 8080:8080 -e OPEN_API_KEY=$OPEN_API_KEY resumegpt
# Prod version 
docker build -t jeffchatham/resumegpt:latest .
docker run -p 8080:8080 -e OPEN_API_KEY=$OPEN_API_KEY jeffchatham/resumegpt:latest
docker push jchatham/resumegpt:latest