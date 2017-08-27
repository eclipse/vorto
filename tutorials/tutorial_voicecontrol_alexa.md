# Voice - Control the XDK with Amazon Alexa

## Prerequisite

- An information model published to the Vorto Repository. [Read more](tutorial-create_and_publish_with_web_editor.md) 
- Evaluation account for the Bosch IoT Suite. [Request account here](https://bosch-si.secure.force.com/content/FormDisplayPage?f=2abiE)
- You have successfully registered your XDK in the Bosch IoT Suite. [Read more](tutorial_register_device.md) 

## Steps

### 1. Generate an Amazon Alexa Skillset with Vorto

- Go to the <a href="http://vorto.eclipse.org/#/details/com.bosch.devices/XDK/1.0.0">XDK Information Model</a> and select 'AWS IoT' from the list of generators on the right hand-side.

	<img src="./images/connect_xdk_kura/step6_1.png">

- In the generator configuration page, select **Bosch IoT Suite** as the digital twin backend. Confirm your selection with **Generate**

	<img src="./images/connect_xdk_kura/step6_2.png" width="50%">

- Download and unzip the generated bundle. It contains 3 generated source files:
	- _alexa-skillset-lambda.js_ - AWS Lambda function containing the skillset behaviour
	- _IntendSchema.json_ - Skillset grammar
	- _xdkUtterances.txt_ - A list of speech commands for each grammar intend

## 2. Write Skillset Lambda function

- Open the <a href="https://console.aws.amazon.com/lambda/home">AWS Lambda console</a> and create a new function
- Select **Alexa Skills Kit** as the trigger triggering the new function and confirm with **Next**

 <img src="./images/connect_xdk_kura/step6_5.png" width="50%" height="50%">
 
- Give your function a name, leave the Runtime default configuration and Copy and Paste the generated alexa-skillset-lambda.js content into the online editor

	<img src="./images/connect_xdk_kura/step6_6.png" width="50%" height="50%">

- Modify the code and add your api token, thingID, username and password. Beware, that the username must be formatted like _tenant\username_

## 3. Build the Skillset 
 
- Open the <a href="https://developer.amazon.com/edw/home.html#/skills">AWS Alexa Skillset Builder</a> and select **Add a New Skill**
- Give the skill a name, e.g. XDKDemo, and an Invocation Name, e.g. XDK. Confirm with **Next**.
- Add the generated **intent schema** as well as the **utterances**, into the online editor. Feel free to modify the utterances to more human friendly commands. Confirm with **Next**.
- Select AWS Lambda ARN Endpoint and add the ARN of the skillset lambda function, that you created in step 2. 

## 4. Test the Skillset 

You can easily test your skillset with the Amazon Alexa online skillset builder.  

- Before we let Alexa output the value, let us update the temperature in Bosch IoT Things first:

```
curl -X PUT 
https://things.apps.bosch-iot-cloud.com/api/1/things/ADD_THING_ID_HERE/features/temperature
-H 'authorization: Basic  ADD_CREDENTIALS_HERE' \
-H "Accept: application/json" \
-H 'x-cr-api-token: ADD_THINGS_API_TOKEN_HERE' \
-d '{"properties": {"sensor_value":30.1}}'
```

-  Now, we can ask Alexa about the temperature. For this, enter the following utterance:

```
Get temperature sensor value
```
	
	Alexa outputs `30.1`

Now it's time to publish your skillset and test it with the Echo. Have fun!

