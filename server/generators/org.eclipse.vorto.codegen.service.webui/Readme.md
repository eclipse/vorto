# Web UI Code Generator

This generator generates a complete AngularJS web application for various IoT Platforms, e.g. Bosch IoT Things.

The Generator outputs:

 - UI Dashboard via users are able to find device instances
 - Location Service for devices that are described via the Location Function Block (TODO: referrence which model here)
 - Device specific UI widgets for known function blocks (Todo: List of known function blocks and their UI representation)


# Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

**That's it**. When the generator starts up, it automatically registers itself at the Vorto Repository and is ready to be used.


----------
List of other available [Code Generators](../Readme.md).