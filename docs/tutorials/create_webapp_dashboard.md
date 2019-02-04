# Creating a Device-specific Web Application

This tutorial explains how to build a small Spring-boot AngularJS Web application that is able to consume device values from the Bosch IoT Suite Cloud Services and display the data in a dashboard. In this example, a web application specific to the XDK functionality is created using the XDK Information Model (refer to [XDK Information Model](https://vorto.eclipse.org/#/details/com.bosch.bcds:XDK:1.0.0)).


## Prerequisites

* [Bosch ID User Account](https://accounts.bosch-iot-suite.com)

* Subscription to [Asset Communication for Bosch IoT Suite](https://www.bosch-iot-suite.com/asset-communication/) (Free plan, no credit card required)

* You have created a XDK thing in the Bosch IoT Suite (refer to [Creating a Thing in the Bosch IoT Suite](create_thing.md)).


## Proceed as follows

### Step 1: Generate the SpringBoot App from Vorto Model

- In the [Thing Browser](https://vorto.eclipse.org/console/#/thingbrowser), browse for the registered XDK information model.

- From the list of generators on the **Source Code Templates** tab, choose **AngularJS Spring-boot Application** and click **Download**.
	
	<img src="../images/tutorials/springbootApp/angularJS_gen.png"/>
	 
	This will generate a ZIP archive containing a maven project of the XDK dashboard spring-boot application.

- **Unzip** the archive and **Import** the project as a Maven Project into your Eclipse IDE.

### Step 2: Configure the App for Bosch IoT Suite

- Create a *certs* folder of the project under `/src/main/resources`.

- Generate a public/private keypair using the Java keytool and store the *jks* file in the src/main/resources/certs folder:
	
		keytool -genkeypair -noprompt -dname "CN=-, OU=-, O=-, L=-, S=-, C=-" -keyalg EC -alias things -sigalg SHA512withECDSA -validity 365 -keystore thing-client.jks

- Extract the public key information into a separate file:
	
		keytool -export -keystore thing-client.jks -alias things -rfc -file thingclient_publickey.cer

- Print the public key to the command prompt:
	  
		keytool -printcert -rfc -file Cthingclient_publickey.cer

-  Open the Things Administration Dashboard for your solution and submit your public key by pasting the key from the command prompt (refer to previous step).
	
- Open the file `src/main/resources/application.yml`.

	- Insert the Bosch IoT Things credentials:
	
			spring:
			  jackson:
			    serialization:
				   write-dates-as-timestamps: false
			bosch:
		     things:
			    alias: things
				 alias.password: [enter keystore password]
				 endpointUrl: https://things.eu-1.bosch-iot-suite.com
				 wsEndpointUrl: wss://things.eu-1.bosch-iot-suite.com
				 apiToken: [enter Bosch IoT Things API Token here ]
				 keystoreLocation: /secure/CRClient.jks
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

### Step 3: Build and Run the App

- You can build the application from the command-line using:
	
		$ mvn clean package
	
- You can easily run the Web application from the command-line using:
	
			$ mvn spring-boot:run

- Open your browser under [http://localhost:8080](http://localhost:8080).

- Log in with Google:
	
	<img src="../images/tutorials/springbootApp/login_page.png" width="50%" />
		
At this point, you should not see any devices yet. For you need to grant the logged-in (Google) user permission to view the thing. 

### Step 4: Grant permissions to user and application

#### Grant (Google user) access to XDK:

- In the [Thing Browser](https://vorto.eclipse.org/console/#/thingbrowser) of the Vorto Console, browse for your thing.

- Select the **Policy** tab and add a new policy, to share the thing with the Google user:

	- Enter a unique **Label** for your policy, e.g. google.

	- Choose **Google** as subject type.

	- Paste the **Subject** ID (Google Open ID) copied from the login screen into the **Subject ID** field:
	
- Refresh your Web application [http://localhost:8080](http://localhost:8080) to view the created thing:
	
	<img src="../images/tutorials/springbootApp/xdk_thing.png" width="40%"/>
	
#### Grant application access to XDK

- Again, select the **Policy** tab and add a new policy, to share the thing with the application as a technical user:

	- Enter a unique **Label** for your policy, e.g. mywebapplication.

	- Choose **Bosch IoT Things Client** as subject type.

	- As **Subject ID**, put in <solutionId>:mywebapp
	
	- Save the policy entry.

- Click on the device to see the details containing UI widgets for the individual function blocks:

	<img src="../images/tutorials/springbootApp/thing_details.png" width="50%"/>

### Step 5: Verify device data in web app

- Open [Bosch IoT Suite API](https://apidocs.bosch-iot-suite.com).

- Choose **Bosch IoT Things - API v2** as spec
	  
- Click **Authorize**.
	  
- Enter your thingsApiKey 

- Choose Bosch ID to log in with your Bosch ID User ID

- Click 'Close' for the Authorization dialog to return back to the Swagger Documentation
	
Now let us send some test temperature values to the Bosch IoT Suite, that get displayed in the dashboard:
	
- Copy **ThingID** from **Endpoint Configuration** of your thing in the Vorto Console and paste it into the **thingId** field.

- Enter `temperature` in the **featureId** field.

- Copy the raw JSON string (within **temperature{ }**) from the JSON tab or use the updated JSON as given below, and paste it in **featureObject** field and 
**Execute**.
	    
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
	   
	<img src="../images/tutorials/springbootApp/thing_features.png" width="40%"/>
	
	<img src="../images/tutorials/springbootApp/thing_put_param.png" width="40%"/>
	  
- Observe the updated temperature widget value in the dashboard of your web application.
	
	<img src="../images/tutorials/springbootApp/web_app.png" width="40%"/>


**Way to go!!** You have just created a simple Spring Boot App that consumes device telemetry data from Bosch IoT Suite and displays its data in a UI. It all works together nicely because the API between the web app and Bosch IoT Suite is defined with Vorto. Feel free to customize the app. 