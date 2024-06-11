Weather App
=================================================
- - -

This project is Weather REST API using Spring Boot API.


System Requirements
-------------------
- - -

To use this project, you will need the following to be already installed on your machine:

- JDK 20 or above
- Git (Optional)
- Lombok plugin (Optional - Can be installed based on IDE used, either Intellij or Eclipse or any other IDE)

Quick Start
-----------

1. Replace the OpenWeatherMap API key placeholder in application.yml file.

2. Run the following `maven` command from the command line in the root directory of your project:

```
mvn clean package
java -jar target\weather-1.0.0.jar

```

Once started, go to [http://localhost:8085](http://localhost:8085/engine-rest/incident/count) :

Weather REST API has the following functionality.
1. Retrieve city weather.
2. Limit of max 5 request per hour per API Key.
3. API Keys are assumed to be constant as part of this project purpose.


The project structure is as follows:

```
pom.xml                             -- Default POM dependencies required for Springboot application
src/
  main/
    java/                           -- Application Java source code goes here
    resources/                      -- Application properties, etc. go here
        application.yml             -- Use to specify configurations 
  test/
    java/                           -- Unit and application integration test Java code goes here
