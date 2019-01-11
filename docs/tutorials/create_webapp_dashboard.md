+++
tags = []
categories = []
date = "2017-12-18T12:00:00+08:00"
title = "Creating a Device-specific Web Application"
parent = "Resources >"
parentlink = "/resources/"
sibling = "Tutorials >"
siblinglink = "/tutorials/"
child = " Creating a Device-specific Web Application" 
+++

# Creating a Device-specific Web Application

This tutorial explains how to build a small Spring-boot AngularJS Web application that is able to consume device values from the Bosch IoT Suite Cloud Services and display the data in a dashboard. In this example, a web application specific to the XDK functionality is created using the XDK Information Model (refer to [XDK Information Model](http://vorto.eclipse.org/#/details/com.bosch.devices/XDK/1.0.0)).


## Prerequisites

* You have successfully booked the Bosch IoT Hub Service (refer to [https://www.bosch-iot-suite.com/hub/](https://www.bosch-iot-suite.com/hub/)).

* You have successfully booked the Bosch IoT Things Service (refer to [https://www.bosch-iot-suite.com/things/](https://www.bosch-iot-suite.com/things/)).

* You have successfully booked the Bosch IoT Permissions Service (refer to [https://www.bosch-iot-suite.com/things/](https://www.bosch-iot-suite.com/permissions/)).

* You have created a thing type for the device (refer to [Creating a New Thing Type](../dx_create_thingtype)).

* You have registered the device with the thing type (refer to [Registering a Device in the Bosch IoT Suite](../dx_register_device)).


## Proceed as follows

1. Generate a Web application.

	- In the [Thing Browser](https://console.bosch-iot-suite.com/#/thingbrowser), browse for the registered XDK information model.

	- From the list of generators on the **Source Code Templates** tab, choose **AngularJS Spring-boot Application** and click **Download**.
	
		<img src="../img/springbootApp/angularJS_gen.png"/>
	 
		This will generate a ZIP archive containing a maven project of the XDK dashboard spring-boot application.

	- **Unzip** the archive and **Import** the project as a Maven Project into your Eclipse IDE.

2. Configure the application for Bosch IoT Suite.

	- Download [bosch-iot-cloud.jks](../sample_code/bosch-iot-cloud.jks) and store it in a `secure` folder of the project under `/src/main/resources`.

	- From the secure folder, open a command prompt to create a public and private key pair for your solution. Store the CRClient in the *secure* folder as well:
	
			keytool -genkeypair -noprompt -dname "CN=-, OU=-, O=-, L=-, S=-, C=-" -keyalg EC -alias CR -sigalg SHA512withECDSA -validity 365 -keystore CRClient.jks


	- Extract the public key information into a separate file:
	
			keytool -export -keystore CRClient.jks -alias CR -rfc -file CRClient_key.cer

	- Print the public key to the command prompt:
	  
			keytool -printcert -rfc -file CRClient_key.cer

	-  Open the Things Administration Dashboard for your solution [https://things.s-apps.de1.bosch-iot-cloud.com/?solutionId=\{Your_Solution_ID\}&solutionSecret=\{Your_Solution_Secret\}](https://things.s-apps.de1.bosch-iot-cloud.com/?solutionId=\{Your_Solution_ID\}&solutionSecret=\{Your_Solution_Secret\}) and submit your public key by pasting the key from the command prompt (refer to previous step).

		<img src="../img/springbootApp/step3_5_publickey.png" width="50%" />
	
	- Open the file `src/main/resources/application.yml`.

		- Insert the Bosch IoT Permissions and Bosch IoT Things credentials:
	
				spring:
				  jackson:
				    serialization:
				      write-dates-as-timestamps: false
				bosch:
				  things:
				    alias: CR
				    alias.password: [enter keystore password]
				    endpointUrl: https://things.apps.bosch-iot-cloud.com
				    wsEndpointUrl: wss://events.apps.bosch-iot-cloud.com
				    apiToken: [enter Bosch IoT Things API Token here ]
				    keystoreLocation: /secure/CRClient.jks
				    trustStoreLocation: /secure/bosch-iot-cloud.jks
				    trustStorePassword: jks
				    solutionid: [enter Bosch IoT Things solution ID here ]
				    keystore:
				    password: [enter keystore password]
	
		- If you are behind a proxy, add proxy information:
	  
				  http:
				    proxyUser: [enter proxy user]
				    proxyPassword: [enter proxy password]
				    proxyHost: [enter proxy host]
				    proxyPort: 8080
	
		- Include google OAuth2 client details:

				google:
				  oauth2:
				    client:
				      clientId: [enter google client ID]
				      clientSecret: [enter google client secret]

			Refer to [Guide to create google Client ID for web applications](https://developers.google.com/identity/sign-in/web/devconsole-project).

	- Open the Java class `com.example.iot.xdk.config.LocalConfiguration` and uncomment the proxy authentication configuration.

3. Build and run the application.

	- You can build the application from the command-line using:
	
			$ mvn clean package
	
	- You can easily run the Web application from the command-line using:
	
			$ mvn spring-boot:run

4. Test the application.

	- Open your browser under [http://localhost:8080](http://localhost:8080).

	- Log in with Google:
	
		<img src="../img/springbootApp/login_page.png" width="50%" />
	
	- Copy the **Subject** ID from the left menu (highlighted in red).
	
		<img src="../img/springbootApp/copy_SubjectId.png" width="20%" />

5. Update policy of the created thing.

	- In the [Thing Browser](https://console.bosch-iot-suite.com/#/thingbrowser) of the Developer Console, browse for your thing.

	- Select the **Policy** tab and add a new policy, to share the thing with the Google user:

	  - Enter a unique **Label** for your policy.

	  - Choose **Google JWT Token** as subject type.

	  - Paste the **Subject** ID copied from the login screen into the **Subject ID** field:
	
			<img src="../img/springbootApp/create_policy.png" width="50%" />
	
	- Refresh your Web application [http://localhost:8080](http://localhost:8080) to view the created thing:
	
		<img src="../img/springbootApp/xdk_thing.png" width="40%"/>
	
	- Click on the device to see the details containing UI widgets for the individual function blocks:

		<img src="../img/springbootApp/thing_details.png" width="50%"/>

6. Update the features of a thing from Things API.

	- Open [Bosch IoT Things HTTP API](https://things.s-apps.de1.bosch-iot-cloud.com/documentation/rest/).
	  
		- Copy the bosch:things:**apiToken:** value from the file `/src/main/resources/application.yml` and paste it in the **Solution API Token** field.

		- Click **Authorize**.
	  
			<img src="../img/springbootApp/thing_rest_api.png" width="40%"/>
	  
	  - Select **openID** and **Authorize**.
	  
			<img src="../img/springbootApp/authorize_bosch_id.png" width="40%"/>
	
	- Send some test temperature values to the Bosch IoT Suite, that get displayed in the dashboard.
	
	  - Copy **ThingID** from **Endpoint Configuration** of your thing in the Developer Console and paste it into the **thingId** field.

	  - Enter `temperature` in the **featureId** field.

	  - Copy the raw JSON string (within **temperature{ }**) from the JSON tab or use the updated JSON as given below, and paste it in **featureObject** field and **Try It**.
	    
				{
				  "properties": {
				    "status": {
				      "max_range_value": 100,
				      "min_range_value": -100,
				      "sensor_units": "F",
				      "sensor_value": 57.80,
				      "min_measured_value": 10,
				      "max_measured_value": 90
				    }
				  }
				}
	   
			<img src="../img/springbootApp/thing_features.png" width="40%"/>
	
			<img src="../img/springbootApp/thing_put_param.png" width="40%"/>
	  
	- Observe the updated temperature widget value in the dashboard of your web application.
	
	      <img src="../img/springbootApp/web_app.png" width="40%"/>

7. Automatic update of Thing's Features.

	- Open the **LocalConfiguration.java** class and update the `clientId` with an approprieate suffix for this application. In this case, we want to name it as **xdkapp**.
	
		```
			PublicKeyAuthenticationConfiguration.newBuilder()
			  .clientId(thingsSolutionId+":"+"xdkapp")
			  .keyStoreLocation(LocalConfiguration.class.getResource(keystoreLocation))
			  .keyStorePassword(keystorePassword)
			  .alias(alias)
			  .aliasPassword(aliasPassword)
			  .build();
		```
	
	- Please refer to the below picture to configure the new policy for the technical user defined in the above code snippet. 
	
		<img src="../img/springbootApp/create_policy_for_tech_user.png" width="50%"/>
		
	- Provide a **label** in the label textfield, select `Bosch Iot Things Client Id` in the **type** drop-down list, enter the `clientId` (what you have given in the Java code) as the **subject ID** and grant only `read` permission on the **things** resource, as this user only needs to read the things feature values.
	
		<img src="../img/springbootApp/view_policy_for_tech_user.png" width="50%"/>
		
	- Now you will be able to see your web page updates the things features without you refreshing the browser. 
	

	