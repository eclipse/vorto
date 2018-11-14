# Vorto Code Generators

Vorto Code Generators convert Information Models into source code that runs either on a the gateway or device.

The following generators are available as part of the Vorto project: 

 - [Bosch IoT Suite Generator](../generator-boschiotsuite/Readme.md)
 - [Eclipse Ditto](../generator-eclipseditto/Readme.md)
 - [Eclipse Hono](../generator-eclipsehono/Readme.md)

Here is a list of more [Generators](https://www.github.com/eclipse/vorto-examples), provided by the Eclipse Vorto Community.

## Quickstart
Eager to try out the example generators? Head over to the [repository quickstart section](https://github.com/eclipse/vorto/tree/development/repository/repository-web/#Quickstart)
There is a short tutorial on spinning up Docker conatiners that run the whole package.

## Docker

This Docker image expects a bind mount of the docker folder into `/gen/config/` to read the configuration.
The configuration is provided using the [`config.json`](../../repository/repository-server/docker/config.json) file, it contains the configuration for the repoisitory, [the generators](../Readme.md) and [the 3rd party generators](https://github.com/eclipse/vorto-examples).
The proxy settings are read and then the complete file is passed to [Spring Boot](https://spring.io/projects/spring-boot) using the `SPRING_APPLICATION_JSON` env var.
