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

