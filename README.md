# SimianChecker

## Summary
SimianChecker is an API 

## Setup

**Docker and docker-compose** available in the host. Run:
> docker-compose -f docker-compose.yaml

## Endpoint
Once the command below finishes, the application will be available at "http:/host-ip:8082/".
The port can be changed by modifying the property **server.port=8082** and in the docker-compose.yaml in the application.properties.

## Swagger

host-ip:8082/swagger-ui/index.html#/

## Project components

- spring boot
- mongodb
- reactive mongodb
- reactor
- spring webflux
- swagger
- junit5