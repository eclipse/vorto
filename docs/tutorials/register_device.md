# Registering a Device in the Bosch IoT Suite from a Vorto Information Model

This tutorial explains how to register a device in the Bosch IoT Suite that was described as a Vorto Information Model.

As an example, the [Octopus board](http://vorto.eclipse.org/#/details/com.bosch.iot.suite:OctopusSuiteEdition:1.0.0) based on ESP 8266 is used. 


## Prerequisites

* Bosch ID User Account

* You have booked an asset communication package of the Bosch IoT Suite (refer to [Getting Started Guide](https://www.bosch-iot-suite.com/tutorials/getting-started-asset-communication/)).

* You have created a Vorto Information Model for the device (refer to [Describing a device](tisensor.md)).


## Proceed as follows


1. Register the device in the Bosch IoT Hub.

	- Add the device ID `<technical device ID>`: 

			curl -X POST -i -d '{"device-id":"<technical device ID>"}' -H 'Content-Type: application/json' https://device-registry.bosch-iot-hub.com/registration/<tenantId>

	- Add the device authentication credentials `<tenantId>` and `<authID>`:

			PWD_HASH=$(echo -n "secret" | openssl dgst -binary -sha512 | base64 | tr -d '\n')
			curl -X POST -i -d '{
				"device-id": "<technical device ID>",
				"auth-id": "<authID>",
				"type": "hashed-password",
				"secrets": [{
					"hash-function": "sha-512",
					"pwd-hash": "'$PWD_HASH'"
				}]
			}' -H "Content-Type: application/json" https://device-registry.bosch-iot-hub.com/credentials/<tenantId>

2. Create the thing in the Bosch IoT Hub.

	Example: create a thing with a reference to the octopus thing type:

		curl -X POST -i -d '
		{
			"thingId":"<tenantId>:<technical device ID>",
			"policyId":"<tenantId>:<technical device ID>"
		}' -H "Content-Type: application/json" -H "x-cr-api-token: <apitoken>" https://things.apps.bosch-iot-cloud.com/api/2/things


