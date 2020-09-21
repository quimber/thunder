# SimianChecker

## Summary
SimianChecker is a REST API to analyze if a requested DNA sequence is from a simian or a human. 

## Setup

### To run the application in server.
**Docker and docker-compose** must be available in the host. Run:
> docker-compose -f docker-compose.yaml up -d --build

Once the command above finishes, the application will be available at "http:/host-ip:8082/".
The port can be changed by modifying the property **server.port=8082** and in the **docker-compose.yaml** in the application.properties.

### To run the application in locally (eclipse).
> mvn eclipse:eclipse
> import the project
> start mongodb 
> run application in the elcipse
Change application.properties accordingly with the mongodb host and port

## Endpoints

The application exposes these two endpoints
> http://host-ip:8082/simian - Method post

The API returns a JSON result with "is_simian": boolean. If the DNA sequence *matches, is_simian will be true, otherwise
will be false, example:

> HTTP 200 {"is_simian": true};
> HTTP 422, when the input is not correct;
> HTTP 500, when an error occured;

http://host-ip:8082/stats

The API will returns a JSON with a counting of simian DNAs and Human DNAs created, and the ratio between simian and human.

> HTTP 200 {"count_simian_dna": 40, "count_human_dna": 100: "ratio": 0.4}

The folder 'postman-tests' contains a collection of tests to executed.
TIP: Create an environment in the model of 'AWS Environment.postman_environment.json'

**Rule to match:** 
POST → /simian
{
"dna": ["CTGAGA", "CTATGC", "TATTGT", "AGAGGG", "CCCCTA", "TCACTG"]
}
* A DNA is simian when two or more sequence of four of same letters
  in any direction, horizontal, vertical and diagonals.
* The letter can only be: (A, T, C, G)

## Swagger

After the setup (and the application be up & running), the swagger will be available at the URL below:

http://host-ip:8082/swagger-ui/index.html#/

## Project components

- spring boot
- mongodb
- reactive mongodb
- reactor
- spring webflux
- swagger
- junit5

## Future enhancements

To enhance the metrics and log monitoring, the application could use an APM service (like datadog and new relic)

## Demo

http://18.228.108.235:8082/