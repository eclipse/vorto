# Generate Digital Twin OpenAPI from an Information Model

This tutorial explains how to generate a fully functional OpenAPI spec from an existing Information Model described in the Vorto Repository.

In this example we'll be using one of the pre-created models. It has a `Geolocation` and `Temperature` Function Block.   
The rendered version of the generated OpenAPI spec for this models looks like this.

![openAPI example spec](../images/tutorials/create_openapi/openAPI_view.png)

<br />

### Prerequisite
* [BoschID](https://accounts.bosch-iot-suite.com/) Account or [GitHub](https://github.com/) 
* You are a collaborator/owner of a namespace
* Subscription to [Asset Communication for Bosch IoT Suite](https://www.bosch-iot-suite.com/asset-communication/) (Free plan, no credit card required)
* Created a Vorto Information Model refer to [Describing a device](./describe_device-in-5min.md))

<br />

## Steps to follow

**1**. [Provision device](./create_thing.md) in Bosch IoT Suite

**2.** Send telemetry data to Bosch IoT Suite using [Java](./connect_javadevice.md), [Python](./mqtt-python.md), or [Arduino](./connect_esp8266.md)

**3.** Create OpenAPI for Bosch IoT Things by choosing the Information Model of your choice and using the OpenAPI generator within the `Experimental Plugins` section of the details page.

![generate openAPI spec](../images/tutorials/create_openapi/openAPI_plugin.png)

Once you cick the plugin, a pop-up will let you choose which Digital Twin specification the OpenAPI spec should be created for.
> **Note**: Right now, only the Bosch-IoT Things specification is available

![run spec in swagger tools](../images/tutorials/create_openapi/generate_openAPI.png)

**4.** Once downloaded, use the `.yml` file and run it in Swagger tools.   
You're now all set up to retrieve feature data from Things.
> **Note**: This requires a [suite auth token](https://accounts.bosch-iot-suite.com/oauth2-clients)

![get device data through api](../images/tutorials/create_openapi/get_data_ui.png)

<br />

## What's next?
- [Visualize device data](create_webapp_dashboard.md) in a Node.js web application.
