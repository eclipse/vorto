# Eclipse Hono Generator

This generator generates source code that sends device telemetry data to [Eclipse Hono](https://www.eclipse.org/hono/) via MQTT.

It supports the generation for the following platforms:

- Arduino (ESP8266)
- Python
- Java 

## Related tutorials

- [Connect an ESP8266 to Eclipse Hono](https://www.eclipse.org/vorto/tutorials/arduino/)
- [Connect an ultrasonic sensor on Raspberry Pi to Eclipse Hono](https://www.eclipse.org/vorto/tutorials/grovepi/) 
- [Connect a Java-based device to Eclipse Hono](https://www.eclipse.org/vorto/gettingstarted/) 

## Example Usage

The following curl commands show, how you can invoke the Eclipse Hono Generator for different device platforms:

Generate for Arduino platform:

	curl -GET https://vorto.eclipse.org/api/v1/generators/eclipsehono/models/com.ipso.smartobjects.Load_Control:1.1.0?language=arduino

Generate for Python platform:

	curl -GET https://vorto.eclipse.org/api/v1/generators/eclipsehono/models/com.ipso.smartobjects.Load_Control:1.1.0?language=python

Generate for Java platform:

	curl -GET https://vorto.eclipse.org/api/v1/generators/eclipsehono/models/com.ipso.smartobjects.Load_Control:1.1.0?language=java
