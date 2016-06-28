# Javabean Code Generator

This generator converts an information model into a plain old java object model. This generator is used, e.g. by the [MQTT Generator](../org.eclipse.vorto.codegen.service.mqtt/Readme.md) and [Web UI Generator](../org.eclipse.vorto.codegen.service.webdevice/Readme.md)

# Build & Run

	mvn clean install spring-boot:run -Dspring.profiles.active=local

**That's it**. When the generator starts up, it automatically registers itself at the Vorto Repository and is ready to be used.


----------

List of other available [Code Generators](../Readme.md).