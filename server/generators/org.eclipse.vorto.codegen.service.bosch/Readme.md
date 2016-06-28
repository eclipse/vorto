# Bosch Code Generator

This generator creates Java OSGI bundles that allow an end-to-end communication between a device and the Bosch IoT server backend.

The Generator outputs:

 - Dummy Device Driver bundle that can simulate device state changes.
 - Device Service OSGI Bundle that accepts state changes from a device and send them to the server.
 - OSGI Model Bundle, needed on the gateway and backend to do input validation.

# Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

**That's it**. When the generator starts up, it automatically registers itself at the Vorto Repository and is ready to be used.


----------

List of other available [Code Generators](../Readme.md).