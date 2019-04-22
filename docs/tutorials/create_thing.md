# Creating a thing in the Bosch IoT Suite from a Vorto Information Model

This tutorial explains how to create a thing in the Bosch IoT Suite that was described as a Vorto Information Model.   
As an example, we will use our [RaspberryPiTutorial Information Model](https://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorials:RaspberryPiTutorial:1.0.0). 

<img src="../images/tutorials/create_thing/raspbi_IM.png" />

## Prerequisites

* [Postman is installed](https://www.getpostman.com/downloads/)

* Created a Vorto Information Model for the device (refer to [Describing a device](describe_tisensor.md)).

* [Bosch ID User Account](https://accounts.bosch-iot-suite.com)

* Subscription to [Asset Communication for Bosch IoT Suite](https://www.bosch-iot-suite.com/asset-communication/) (Free plan, no credit card required)

* Created a [Bosch IoT Suite OAuth2 Client](https://accounts.bosch-iot-suite.com/oauth2-clients/)
> Make sure to have both scopes checked on creation! (Hub and Things)
<img src="../images/tutorials/create_thing/oauth2_client.png" />

<br />

## Proceed as follows

**1.** Click the `Source Code` button to download the generated Postman script.
<img src="../images/tutorials/create_thing/provision_device_dl.PNG" height="500"/>

**2.** Unzip the downloaded file and open Postman   

**3.** Import the file present in the extracted folder into Postman.   
<img src="../images/tutorials/create_thing/import_jspm.png" />

<br />

**4.** Click on the just imported `Collections` and switch to the `Pre-request Script` tab.

**5.** In here you will see 3 entires. 
<img src="../images/tutorials/create_thing/pre_requeset_script.png" />

- **service-instance-id**: Service Instance ID of your asset communication subscription. (Can be found in the *Show Credentials* on the far bottom)
<img src="../images/tutorials/create_thing/service_isntance_id.png" />

- **device-id**: Some Unique ID to give the device in the format of `<namespace>:<uniqueId>`. (Namespace has to defined and can be found in the namespace tab of the asset communication subscription).

- **device-password**: Password to be used when creating the integration for the device and sending data to it.

<br />

**6.** Once you've entered the information into the tab, switch to the `Headers` tab.
<img src="../images/tutorials/create_thing/bearer_token.png" />

**7.** Head over to your Bosch IoT Suite account and open the [OAuth2 Client page](https://accounts.bosch-iot-suite.com/oauth2-clients). Click the `Use` button to get a temporary token (valid for 5min).

**8.** Insert the temporary token into the second row that says `Authorization`. **Don't** remove the `Bearer` keyword.

**9.** After entering the token, press the blue `Send` button in the upper right corner to create your Thing.

<br />

**10.** Once your request was successful, you'll see a long response in the bottom section of Postman and the status `201 Created`. Now your thing has been created and you can start with the integration. 

## What's next?

- [Generate Arduino sketch](connect_esp8266.md) that connects an ESP8266 based device to the Bosch IoT Suite.
- [Visualize your data with the Vorto Dashboard](./vorto_dashboard.md)
