# Vehicle service application
For the backend, we use Spring technology and the NAE libraries that we have defined
in Maven dependencies.
The backend application runs on version 6.0.3 of the Netgrif Application Engine (NAE).

## About
NAE Backend is the server part of Netgrif application engine that provides Restful API for Nae frontend. Engine consumes XML petrinet files and produces executable processes and tasks. NAE is Spring boot application (based on Java), uses Rule Engine based on Drools for event triggering.  MongoDB, redis and MySQL for database and Elasticsearch for faster search experiences and greater analysis. XML petrinet files also contains Groovy executable actions.

For more informations visit https://netgrif.com/products/#nae
## Installation

This project can be used as a base to your NAE application. Before you start coding please consider doing following
steps:

1. Clone the repository:
    ```sh
    git clone https://github.com/Retoocs/vehicle-service-backend.git
    ```
2. Install Maven dependencies ``clean``, ``compile``, ``package``, ``install`` in Lifecycle.
3. Change SDK version to 11.
4. Run command in directory where docker-compose.yml is located:
   ```sh
    docker-compose up
    ```
5. In ``src/main/resources/application.properties`` change URLs and ports for yours elastic, redis and mongo databases.

