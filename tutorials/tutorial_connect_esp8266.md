# Connect an ESP8266-Based Device with Arduino to Bosch IoT Suite

In this tutorial, you are going to learn how to use Vorto to generate an Arduino sketch for a given Information Model and send the device data to the Bosch IoT Suite via MQTT. 

## Pre-requisites

* You have successfully booked the following services:
	- Bosch IoT Things Service
	- Bosch IoT Permissions Service 
	- Bosch IoT Hub
* [Publish](tutorial-create_and_publish_with_web_editor.md) an information model for the device to the Vorto Repository.  
* [Register](tutorial_register_device.md) the device in the Bosch IoT Suite.

## Tools

* [Arduino IDE](https://www.arduino.cc/en/Main/Software)
 * ESP8266 Package
 * [PubSubClient](https://pubsubclient.knolleary.net/)
* OpenSSL
* Curl

## Steps


### Step 1. Setup Development Environment

If you haven't already setup your development environment for the ESP8266, you first want to install the Arduino IDE, the board-package for the ESP8266 and the PubSubClient.

1. Download and install the [Arduino IDE](https://www.arduino.cc/en/Main/Software)
2. Install the ESP822 Board Package
 1. Under File -> Preferences -> Additional Board Manager URLs add "http://arduino.esp8266.com/stable/package_esp8266com_index.json"
 2. Under Tools -> Board -> Boards Manager... look for the esp8266 package and install the latest version. NOTE: in the case download fails from within the Arduino IDE with a PKIX verification exception, you need to add the Bosch certificates to the certificate store which is included in the Java installation in the Arduino IDE installation directory (<Arduino install path>\java\lib\security\cacerts).
 3. Under Tools -> Board you should now find all the supported ESP8266 boards listed, chose the one you are working with
 4. Plug-in the NodeMCU board and check in the Device Manager, whether you have the necessary USB serial driver installed. In case it is missing and Windows Update can't find the driver, get the latest version of the driver from the [NodeMCU github repository](https://github.com/nodemcu/nodemcu-devkit/blob/master/Drivers/)
3. Under Sketch -> Include Library -> Manage Libraries look for the PubSubClient and install the latest version. Easiest is to use the search field to search for the PubSubClient.

The PubSubClient will get installed in the Arduino/libraries directory, i.e. in C:\users\<username>\Documents\Arduino\libraries. As you might need to adjust the buffer size for the MQTT payload, it is a good thing to verify the location of the library at this point.

### Step 2: Generating an Arduino Project

In this step you are going to generate the arduino sketch using the Arduino Generator. Assuming you have published an infromation model already to the vorto repository prior to this step (*see the pre-prequisite section*):

* **Invoke the generator from the Repository**

	1. Open the [Vorto Repository](http://vorto.eclipse.org/) in a browser
	2. Navigate to your model using the search functionality of the repository and look for 	   the **Arduino Generator** in the panel on the right hand side of the screen. Click on the **generate button** to generate code. 
	3. Store the ZIP file which will be downloaded and extract the source code.
	4. Open the INO file in your Arduino IDE


### Step 3: Adjusting the Arduino Project
Finally you have to modify the Arduino sketch to your needs. The sketch is built to be used with Eclipse Hono / Bosch IoT Hub as a backend. Hence the necessary adaptations to the generated file may be different in case you use a different MQTT broker.

A few important changes that need to be made:

* Tenant: set the define TENANT to the name of your tenant in Elipse Hono/Bosch IoT Hub
* MQTT Broker address: set the mqttServer constant to the corresponding endpoint, i.e. mqtt.bosch-iot-hub.com for the Bosch IoT Hub
* WLAN configuration
 * SSID: change the constant ssid to your WiFi's SSID
 * Password: change the constant password to you WiFi password
* Device ID: the device ID we are using by default consists of a prefix, which is stored in the String deviceId and the MAC address of the WiFi interface.
* In order to verify the server, you need to add a fingerprint of the server certificate to the code. This finger print is a SHA-1 hash of the certificate of the MQTT broker.
 * The certificate of the Bosch IoT Hub can downloaded from [the website](http://docs.bosch-iot-hub.com/documentation/cert/iothub.crt).
 * For other MQTT brokers you might need to use openssl to extract the server certificate by invoking `openssl s_client -showcerts -connect www.example.com:443 </dev/null`. The first certificate, starting with -----BEGIN CERTIFICATE----- and ending with -----END CERTIFICATE----- is the part you want to store in a .crt file.
 * The fingerprint for the certificate above can be extracted by invoking `openssl x509 -noout -fingerprint -sha1 -inform pem -in [certificate-file.crt]`
 * Add the fingerprint to the Arduino sketch by setting the constant `mqttServerFingerprint`, replacing the columns with spaces.
* Finally you want to adapt the code in the loop function to read the sensors of your device and filling in the corresponding values to the Information Model API.
* Compile the code to verify everything with your code is ok.
* Connect your ESP8266 to your computer and select the virtual COM port to which your device is connected in the Arduino IDE under Tools -> Port.
* Upload your code to your device by selecting Sketch -> Upload. 

### Step 4. Verify incoming sensor data

To check if the sensor data was sent successfully to the cloud, just open the <a href="https://console.bosch-iot-suite.com">Bosch IoT Developer Console</a> and navigate to the thing in the thing browser.

**Congratulations**, you have successfully connected your ESP8266 device to the Bosch IoT Hub.

## What Could Go Wrong
So you have followed all the steps above, compiled and uploaded the code to your device, registered the same with Bosch IoT Hub but the Hub does not receive any data? Here are a few things you might want to check:

* PubSubClient has an MQTT buffer size of 128 bytes per default. This means that if your the length of your topic and payload plus 5 bytes overhead are longer than those 128 bytes the library will not transmit your data. However there is a solution: you can increase the buffer size by setting the variable `MQTT_MAX_PACKET_SIZE` in PubSubClient.h to a larger value, e.g. 256 or 384.

## Next Steps

- [Create a web application consuming the device telemetry data](tutorial_create_webapp_dashboard.md)
- [Build an Amazon Alexa Skillset to voice-control the device](tutorial_voicecontrol_alexa.md)
