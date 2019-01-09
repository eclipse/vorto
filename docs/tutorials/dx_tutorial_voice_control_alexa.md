# Voice - Control the XDK with Amazon Alexa

This tutorial explains how to build an Amazon Alexa skillset that reads device data from Bosch IoT Suite.

## Prerequisites

* Bosch ID User Account

* You have booked an asset communication package of the Bosch IoT Suite (refer to [Getting Started Guide](https://www.bosch-iot-suite.com/tutorials/getting-started-asset-communication/)).

* You have created a Vorto Information Model for the device (refer to [Describing a device](tisensor.md)).

* You have registered the device using the Vorto Information Model in the Bosch IoT Suite (refer to [Registering a Device in the Bosch IoT Suite](../dx_register_device)).

## Proceed as follows

1. Generate an Amazon Alexa skillset with Vorto.

	- Log in to the [Thing Browser](https://console-bcx.bosch-iot-suite.com/#/thingbrowser).

	- Select your registered XDK device.

	- Select the **Source Code Templates** tab.

	- From the list of templates, select **Amazon Alexa skillset**.
	
		<img width="400" src="../img/connect_alexa/alexa-generator.png">
	
	- Download and unzip the generated bundle.

		It contains 3 generated source files:
	
		- `alexa-skillset-lambda.js` - the AWS Lambda function containing the skillset behavior
	
		- `IntendSchema.json` - the skillset grammar
	
		- `xdkUtterances.txt` - a list of speech commands for each grammar intend

2. Write skillset Lambda function.

	- Log in to the [AWS Lambda console](https://console.aws.amazon.com/lambda/home).

	- Click **Create a function**.
	
		The **Create function** page opens.

		<img width="60%" src="../img/connect_alexa/alexa-create-function.png">
		
		> Note: Ensure that your location is _N.Virginia_.
	
	- In the **Name** entry field, enter a name for your function.

	- From the **Runtime** drop-down list, select **Node.js.6.10** as runtime default configuration,

	- From the Role drop-down-list, select **Choose an existing role**.

	- From the **Existing role** drop-down list, select  **lambda_basic_execution** to this role (refer to [Create the Execution Role (IAM Role)](https://docs.aws.amazon.com/lambda/latest/dg/with-s3-example-create-iam-role.html)). 

	- Click **Create function**.
	
		<img width="60%" src="../img/connect_alexa/alexa-function-details.png">
	
	- Copy and Paste the generated `alexa-skillset-lambda.js` content into the online editor.
	
	- Modify the code and add your _api token_, _thingID_, _username_ and _password_. Beware, that the username must be formatted like _tenant\username_. The _username_ and _password_ are from [Bosch IoT Permissions](https://permissions.s-apps.de1.bosch-iot-cloud.com/fusion/).
	
		<img width="80%" src="../img/connect_alexa/alexa-add-lambdajs.png">
	
	- In the Execution code area, select **Choose an existing role** from the first drop-down list.

	- In the Execution code area, select an existing role from the first **Existing role** drop-down list.

		<img width="70%" src="../img/connect_alexa/alexa-add-lambdajs-role.png">

3. Set-up Your Alexa Skill in the Developer Portal.

	- Log in to the [AWS Alexa Skillset Builder](https://developer.amazon.com/edw/home.html#/skills).

	- On **Alexa Skills Kit**, click **Get Started**.
	
		<img width="30%" src="../img/connect_alexa/alexa-getStarted.png">

		A page opens where you can define and manage your skills.
	
	- Click **Add a New Skill**:
	
		<img width="50%" src="../img/connect_alexa/alexa-add-skill.png">
	
	- In the **Name** entry field, enter a name for your skill.

	- In the **Invocation Name** entry field, enter an invocation name for your skill.

		<img width="70%" src="../img/connect_alexa/alexa-skill-config.png">
	
	- Click **Next**.

	- Copy the Skill **ID** for the next step:
	
		<img width="50%" src="../img/connect_alexa/alexa-skillset-id.png">

4. Add Alexa skillset trigger.

	- Return to the [AWS Lambda console](https://console.aws.amazon.com/lambda/home).

	- In the **Designer** area, click **Alexa Skills Kit** from the list of triggers to add an Alexa skillset trigger to the new function:
	
		<img width="70%" src="../img/connect_alexa/alexa-add-skillsetTrigger.png">
	
	- In the **Configure triggers** area, paste the skill ID copied from the previous step into the **Skill ID** entry field.

		<img width="70%" src="../img/connect_alexa/alexa-add-triggerConfig.png">

	- Click **Add**.

	- Click **Save**.
	
		Your Alexa skills kit will appear as shown below:
	
		<img width="70%" src="../img/connect_alexa/alexa-add-SkillID.png">

5. Build the skillset.
 
	- Return to the [AWS Alexa Skillset Builder](https://developer.amazon.com/edw/home.html#/skills).
	
	- Select **Interaction Model**.

	- Add the generated **Intent Schema** as well as the **Sample Utterances**, into the online editor.

		You can modify the utterances to more human friendly commands:

		<img width="70%" src="../img/connect_alexa/alexa-add-intent.png">

		<img width="70%" src="../img/connect_alexa/alexa-add-utterences.png">

	- Confirm with **Next**
	
	- Select **Configuration**.

	- Add the **AWS Lambda ARN (Amazon Resource Name)** of the skillset lambda function that you created in step 2. 
		
		<img width="70%" src="../img/connect_alexa/alexa-add-configuration.png">

6. Test the skillset.

	You can easily test your skillset with the AWS Alexa Skillset Builder.  
	
	- First, update the temperature feature of your XDK thing from Bosch IoT Things (refer [Update the features of a thing from Things API](./dx_create_webapp_dashboard/#update_the_features_of_a_thing_from_things_api)).
	
	-  Enter the following utterance to ask Alexa about the temperature and submit:
	
			get temperature sensor value

		You should see the formatted JSON request from the Alexa Service and the response coming back.

	- Verify that you get a correct Lambda response and notice the temperature value.
	
		Alexa outputs `57.8`:
	
		<img width="50%" src="../img/connect_alexa/alexa-test.png">

7. Publish your skillset and test it with Echo.
