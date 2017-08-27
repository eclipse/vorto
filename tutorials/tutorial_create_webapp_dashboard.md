# Create a device-specific web application with Vorto

In this tutorial, we want to build a small Spring-boot based IoT webapp that is able to consume device values, e.g. XDK, from a IoT Cloud backend and display the data in a dashboard. In this example, we will use the <a href="http://vorto.eclipse.org/#/details/com.bosch.devices/XDK/1.0.0"> XDK Information Model</a> and create a web application specific to the XDK functionality.

## Prerequisite

- An information model published to the Vorto Repository. [Read more](tutorial-create_and_publish_with_web_editor.md) 
- Evaluation account for the Bosch IoT Suite. [Request account here](https://bosch-si.secure.force.com/content/FormDisplayPage?f=2abiE) 
- You have successfully registered your XDK in the Bosch IoT Suite. [Read more](tutorial_register_device.md) 

## Steps

### 1. Generate a web application with Vorto

- Go to the <a href="http://vorto.eclipse.org/#/details/com.bosch.devices/XDK/1.0.0">XDK Information Model</a> and select 'Web-based Device Dashboard' from the list of generators on the right hand-side.

 <img src="./images/create_webapp_dashboard/step3_1.png"/>
 
- In the generator configuration page, select the <strong>Bosch IoT Suite</strong> as a cloud platform connector. Feel free to select other add-ons as needed. Confirm your selection by clicking **Generate**. This will generate a ZIP achieve containing a maven project of the XDK dashboard spring-boot application.

 <img src="./images/create_webapp_dashboard/step3_2.png" width="50%"/>
 
- **Unzip** and **import** the project as a Maven Project into your Eclipse IDE.

### 2. Configure application for Bosch IoT Suite

- Download <a href="https://github.com/bsinno/iot-things-examples/blob/master/cr-integration-api-examples/common/src/main/resources/bosch-iot-cloud.jks">bosch-iot-cloud.jks</a> and store it in a "secure" folder of the project
- Create a public and private key pair for your solution. Store the CRClient in the "secure" folder as well
```
keytool -genkeypair -noprompt 
-dname "CN=-, OU=-, O=-, L=-, S=-, C=-" 
-keyalg EC -alias CR -sigalg SHA512withECDSA 
-validity 365 -keystore CRClient.jks
```
- Extract the public key information into a separate file
```
keytool -export -keystore CRClient.jks -alias CR -rfc -file CRClient_key.cer
```
- Print the public key to the command prompt
```
keytool -printcert -rfc -file CRClient_key.cer
```
- Open the Things Adminstration Dashboard for your solution (sent to you via Email during evaluation account registration) and submit your public key by copy&pasting the key from the command prompt

	<img src="./images/connect_xdk_kura/step3_5_publickey.png" width="50%"/>

- Open the src/main/resources/application.yml and insert the Bosch IoT Permissions and Bosch IoT Things credentials that you have received via Email during the evaluation account registration:

	```
spring:
  jackson:
    serialization:
      write-dates-as-timestamps: false
bosch:
  permissions:
    endpointUrl: https://permissions-api.apps.bosch-iot-cloud.com
    clientId: [enter Bosch IoT Permissions client id here]
    clientSecret: [enter Bosch IoT Permissions secret here]
  things:
    alias: CR
    alias.password: [enter keystore password]
    endpointUrl : https://things.apps.bosch-iot-cloud.com
    wsEndpointUrl : wss://events.apps.bosch-iot-cloud.com
    apiToken: [enter Bosch IoT Things API Token here ]
    keystoreLocation : /secure/CRClient.jks
    trustStoreLocation : /secure/bosch-iot-cloud.jks
    trustStorePassword : jks
    solutionid: [enter Bosch IoT Things solution ID here ]
    keystore:
      password: [enter keystore password]
	```

- If you are behind a proxy, make these couple of changes:
	- Add proxy information in the application.yml
	- Open java class _com.example.iot.xdk.config.LocalConfiguration_ and uncomment the proxy authentication configuration.

- Open the _com.example.iot.xdk.config.LocalConfiguration_ and use the **clientID**: _solutionID:xdkapp._ 
Make sure the  **clientID** in your configuration matches the clientID in the ACL that you had created via the Developer Console earlier!

### 3. Test the application

- Open your browser under <a href="http://localhost:8080">http://localhost:8080</a>
- Log in with your Bosch IoT Permissions credentials

	<img src="./images/connect_xdk_kura/step3_8.png" width=50%"/>

- After successful authentication, you can see all your claimed devices, that you had registered (see [Register a device in Bosch IoT Suite](tutorial_register_device.md) 

	<img src="./images/connect_xdk_kura/step3_9.png" width=50%"/>

- Click on the device to see the details containing UI widgets for the individual function blocks

	<img src="./images/create_webapp_dashboard/step3_10.png" width=50%"/>

- Let's send some test temperature value to the Bosch IoT Suite, that gets displayed in the dashboard:

	```
curl -X PUT 
https://things.apps.bosch-iot-cloud.com/api/1/things/ADD_THING_ID_HERE/features/temperature
-H 'authorization: Basic  ADD_CREDENTIALS_HERE' \
-H "Accept: application/json" \
-H 'x-cr-api-token: ADD_THINGS_API_TOKEN_HERE' \
-d '{"properties": {"sensor_value":22.2}}'
	```

## What's next ?

- [Simulate the device and send test data](tutorial_simulate_device.md)