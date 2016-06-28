# Web UI Code Generator

The generator creates a web application that visualizes the devices data in a simple web frontend.

It outputs:

 - AngularJS front-end displaying the configuration and status data of a device
 - Web Sockets that output device events in the UI without refreshing

# Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

**That's it**. When the generator starts up, it automatically registers itself at the Vorto Repository and is ready to be used.


----------

List of other available [Code Generators](../Readme.md).