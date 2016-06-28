# LWM2M Code Generator

This generator supports the OMA LWM2M specification by mapping an information model to LWM2M objects and resources.

The Generator outputs:

 - LWM2M XML according to the OMA LWM2M Schema definition.
 - Eclipse Leshan Client that sends and receives LWM2M messages to and from the LWM2M server.

This generator **requires a Vorto Mapping** definition mapping from a function block element to a LWM2M model element.


# Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

**That's it**. When the generator starts up, it automatically registers itself at the Vorto Repository and is ready to be used.


----------

List of other available [Code Generators](../Readme.md).