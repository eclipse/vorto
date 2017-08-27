# Register a device in the Bosch IoT Suite


## Prerequisite

- An information model published to the Vorto Repository. [Read more](tutorial-create_and_publish_with_web_editor.md)
- Evaluation account for the Bosch IoT Suite. [Request account here](https://bosch-si.secure.force.com/content/FormDisplayPage?f=2abiE).

## Steps

- Open the <a href="https://console.bosch-iot-suite.com">Bosch IoT Developer Console</a>
- Log in to the console with your evaluation tenant, username and password.
- Open the *thing browser** under Thing Console in the left navigation bar
- Click **Connect thing**

	<img src="./images/connect_xdk_kura/step2_connect.png" width="50%" height="50%"/>

- Search the Vorto Repository for the device type you would like to register, e.g. _XDK_. Select the device type and confirm with _Next_

 <img src="./images/connect_xdk_kura/step2_connect_vorto.png" width="50%"/>

- Choose the protocol connector that the device connects to the Suite, e.g. **HTTP**. Confirm your selection with _Next_
- Give your device a unique name. For example
	- Namespace: com.mycompany
	- Technical ID: For example the mac address of the device, like _FCD6BD100B88_. Confirm your settings with _Complete_
- Now you have successfully registered your device with the Suite for a given Vorto information model and you should be able to see it in the thing browser:
	
	<img src="./images/connect_xdk_kura/step2_7.png" width="50%"/>

- Send test data to registered thing and verify the incoming data in the Developer Console:

```
curl -X PUT 
https://things.apps.bosch-iot-cloud.com/api/1/things/ADD_THING_ID_HERE/features/temperature
-H 'authorization: Basic  ADD_CREDENTIALS_HERE' \
-H "Accept: application/json" \
-H 'x-cr-api-token: ADD_THINGS_API_TOKEN_HERE' \
-d '{"properties": {"sensor_value":22.2}}'
```

## What's next ? 

- [Connect a device via Bluetooth to the Bosch IoT Suite](tutorial_connect_xdk_ble.md)
- [Build a web application for a device](tutorial_create_webapp_dashboard.md)
- [Build an Amazon Alexa Skillset for a device](tutorial_voicecontrol_alexa.md)
