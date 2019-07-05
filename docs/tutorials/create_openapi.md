# Generate Digital Twin OpenAPI from an Information Model

This tutorial explains how to generate a fully functional OpenAPI spec from an existing Information Model described in the Vorto Repository.


### Prerequisite
* [BoschID](https://accounts.bosch-iot-suite.com/) Account or [GitHub](https://github.com/) 
* You are a collaborator/owner of a namespace
* Subscription to [Asset Communication for Bosch IoT Suite](https://www.bosch-iot-suite.com/asset-communication/) (Free plan, no credit card required)
* Created a Vorto Information Model refer to [Describing a device](./describe_device-in-5min.md))

<br />

## Steps to follow

**1**. [Provision device](./create_thing.md) in Bosch IoT Suite

**2.** Send telemetry data to Bosch IoT Suite using [Java](./connect_javadevice.md), [Python](./mqtt-python.md), or [Arduino](./connect_esp8266.md)

**3.** Create OpenAPI for Bosch IoT Things

**4.** Retrieve feature data from Things by running generated OpenAPI yml in Swagger tools (requires suite auth token)

## What's next?
- [Visualize device data](create_webapp_dashboard.md) in a Node.js web application.
