# Bosch-ProSyst FI Code Generator

The generator creates Functional Items for an Information Model, needed by the ProSyst mBS gateway.

It outputs:

 - Functional Item Abstraction Layer

# Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

**That's it**. When the generator starts up, it automatically registers itself at the Vorto Repository and is ready to be used.


----------

List of other available [Code Generators](../Readme.md).