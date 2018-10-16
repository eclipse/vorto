# Bosch IoT Suite Generator

### Overview

This generator generates source code that sends telemetry data to the [Bosch IoT Hub](https://www.bosch-iot-suite.com/hub/) and [Bosch IoT Things Service](https://www.bosch-iot-suite.com/things/). 

It generates code for the following platforms:

- Arduino for ESP8266
- Python
- Java

For devices that need to be connected via the Bosch IoT Gateway Software, this generator generates an OSGi bundle.

## Example Usage

The following curl commands show, how you can invoke the Bosch IoT Suite Generator for different device platforms:

Generate for Arduino platform:

	curl -GET http://vorto.eclipse.org/api/v1/generators/boschiotsuite/models/com.ipso.smartobjects.Load_Control:1.1.0?language=arduino

Generate for Python platform:

	curl -GET http://vorto.eclipse.org/api/v1/generators/boschiotsuite/models/com.ipso.smartobjects.Load_Control:1.1.0?language=python

Generate for Java platform:

	curl -GET http://vorto.eclipse.org/api/v1/generators/boschiotsuite/models/com.ipso.smartobjects.Load_Control:1.1.0?language=java

Generate for Gateway Software Platform:

	curl -GET http://vorto.eclipse.org/api/v1/generators/boschiotsuite/models/com.ipso.smartobjects.Load_Control:1.1.0?gateway=true

	
