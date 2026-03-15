Needs an OpenAI API key set to environmental variable OPEN_API_KEY. 

OpenAPI reference here:
http://localhost:8080/swagger-ui/index.html

TODO: Create frontend to consume API. 

mvn clean package
docker build -t resumegpt .
# $OPEN_API_KEY is an OpenAI API key environmental variable.
docker run -p 8080:8080 -e OPEN_API_KEY=$OPEN_API_KEY resumegpt