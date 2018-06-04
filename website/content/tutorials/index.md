---
date: 2016-03-09T20:08:11+01:00
title: Tutorials
weight: 30
---
## Connecting a GrovePi to Eclipse Hono
This tutorial explains how to generate a simple Python application for you GrovePi that sends sensor data to Eclipse Hono using MQTT.

### Prerequisites

*   [Python 3.x](https://www.python.org/)
    
*   IDEs - for Python: [Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python)
    
*   [Paho Python Client](https://eclipse.org/paho/clients/python/)
    
*   [GrovePi Sensor Kit](https://www.dexterindustries.com/grovepi/)
    


### 1.  Setup your device.
In this example Raspberry Pi is used but you can use any device which can run python.

- [Install raspbian on the Raspberry Pi](https://www.raspberrypi.org/learning/software-guide/).
- [Connect the pi to wifi](https://www.raspberrypi.org/learning/software-guide/wifi/).
- [Enable ssh connection on your pi](https://www.raspberrypi.org/documentation/remote-access/ssh/) .
- [Install python and required modules]({{< ref "installation/grovepi.md" >}}).

        
### 2. Setup your development environment (on your development machine).

- [Install Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python).
- [Install python and required modules]({{< ref "installation/grovepi.md" >}}).
        
### 3.  Generate application code using the Python generator.
    
- Go to the Information Model of your device in the Vorto Repository, e.g. [distance sensor](http://vorto.eclipse.org/#/details/demo.iot.device/DistanceSensor/1.0.1?s=distancesensor).
- On the right side, select the **Eclipse Hono Generator** and choose **Python**
- Click **Generate**
- Store the ZIP file and extract the source code.
- [Get the certificate](https://letsencrypt.org/certs/lets-encrypt-x3-cross-signed.pem.txt) and copy and paste the content in a .crt file where you stored your source code

### 4. Registering to Eclipse Hono
Eclipse Hono provides a sandbox environment, which we can use in this tutorial to integrate the GrovePi with. So before we can send any data, we must register the device in Eclipse Hono. We use ```vortodemo``` as a **Hono tenant** and ```1234``` as a **device ID**:

**Registering our Device**

```sh
curl -X POST http://hono.eclipse.org:28080/registration/vortodemo
-i -H 'Content-Type: application/json'
-d '{"device-id": "1234"}'
```

**Adding device credentials**

```sh
PWD_HASH=$(echo -n "S3cr3t" | openssl dgst -binary -sha512 | base64 | tr -d '\n') 
curl -X POST http://hono.eclipse.org:28080/credentials/vortodemo
-i -H 'Content-Type: application/json'
-d 'JSON request payload' 
```

JSON request payload:
```sh
{
  "device-id": "1234",
  "auth-id": "1234",
  "type": "hashed-password",
  "secrets": [
    {
      "hash-function": "sha-512",
      "pwd-hash": "'$PWD_HASH'"
    }
  ]
}
```

        
### 5.  Install GrovePi Python dependencies on the Raspberry Pi.
    
- Install necessary dependencies for GrovePi on the Raspberry Pi. To do so, just execute the commands

```sh
sudo curl https://raw.githubusercontent.com/DexterInd/Raspbian_For_Robots/master
upd_script/fetch_grovepi.sh | bash
       
sudo reboot
```

- You can find full instructions [here](https://www.dexterindustries.com/GrovePi/get-started-with-the-grovepi/setting-software/).
    
### 6.  Connect the sensor to the Raspberry Pi.
    
- Select a sensor from the kit.
- Connect the selected sensor to the Raspberry Pi. In this tutorial, ultrasonic sensor is used, which measures a non-contact distance. The ultrasonic sensor is connected to **pin “3”** on the GrovePi.
        
### 7.  Update the application code.
    
The ZIP file contains the necessary libraries for your project and a sample Python main program. The main program is named after the thing type and stored in the main directory of the project. Open this file in your editor of choice to make the necessary adjustments.
    
Code sections which can be customized for your needs are marked with
    
```sh
        ### BEGIN SAMPLE CODE
```         
and

```sh
    
        ### END SAMPLE CODE
```
    
    
- Import the GrovePi dependencies:

```sh      
      from grovepi import *
```     
        
- Update the Device Configuration values like:
        
```sh
      hono_tenant = "vortodemo"
      hono_password = "S3cr3t"
      hono_endpoint = "ssl://hono.eclipse.org:8883"
      hono_deviceId = "1234"
      hono_clientId = hono_deviceId
      hono_authId = hono_deviceId + "@" + hono_tenant
      hono_certificatePath = "PATH TO YOUR CERTIFICATE"
      ditto_namespace = "org.eclipse.vorto"
```
            
- Define a constant and assign the pin number of the pin to which the sensor is physically connected:

```sh
      # constants
      ultrasonic_ranger = 3
```            
              
- The function `periodicAction` is called periodically every `timePeriod` seconds. It is meant to periodically read your sensor’s values and transmit them using the thing type as a data model layer and the Paho MQTT Client.
        
- Read the data and assign it to the functional block properties:

```sh     
      infomodel.distance.sensor_value = ultrasonicRead(ultrasonic_ranger)
      infomodel.distance.sensor_units = "cm"
```       
        
- The variable `timePeriod` is set further down in the file. In this example, the variable `timePeriod` is set to 10.
        
### 8.  Run the application on the device.
- Copy the code to the device ([How to copy files to Raspberry Pi?](https://www.raspberrypi.org/documentation/remote-access/ssh/scp.md)).
- Open the Terminal and navigate to the source code folder.
- Run the application by typing the following command:

```sh  
      python GENERATED_MAIN_PYTHON_FILE.py
```      
`example output:`

![grovePi Screenshot](/images/tutorials/grovepi/output_screenhot_grovepi.png)

- Follow [Consuming Messages from Java for Hono] (https://www.eclipse.org/hono/dev-guide/java_client_consumer/) to receive the device data in Eclipse Hono.


-------------

## Connecting a ESP8266 (Arduino) to Eclipse Hono
This tutorial explains how to generate an Arduino sketch for a given Information Model and send the device data to Eclipse Hono via MQTT.

### Prerequisites
* [Arduino IDE](https://www.arduino.cc/en/Main/Software)
    * ESP8266 Package
    * [PubSubClient](https://pubsubclient.knolleary.net/)
* OpenSSL
* Curl

### 1. Setup your development environment (on your development machine).
 *   Download and install the [Arduino IDE](https://www.arduino.cc/en/Main/Software).
        
    *   Install the ESP8266 Board Package.
        
        1.  Select **File -> Preferences -> Additional Board Manager URLs** and add `http://arduino.esp8266.com/stable/package_esp8266com_index.json`.
            
        2.  Select **Tools -> Board -> Boards Manager…**, look for the esp8266 package and install the latest version.
            
        3.  Select **Tools -> Board**.
            
            You should now find all the supported ESP8266 boards listed, chose the one you are working with.
            
        4.  Plug-in the NodeMCU board and check in the Device Manager, whether you have the necessary USB serial driver installed. In case it is missing and Windows Update can’t find the driver, get the latest version of the driver from the [NodeMCU github repository](https://github.com/nodemcu/nodemcu-devkit/blob/master/Drivers/).
            
    *   Select **Sketch -> Include Library -> Manage Libraries**, look for the PubSubClient and install the latest version.
        
        > Note: You can use the search field to search for the PubSubClient.
        
    
    The PubSubClient will get installed in the Arduino/libraries directory, i.e. in `{HOME}/Arduino/libraries`. As you might need to adjust the buffer size for the MQTT payload, it is a good thing to verify the location of the library at this point.



### 2. Generating an arduino sketch using the Arduino Generator (Arduino project).
    
- Go to the Information Model of the [distance sensor] (http://vorto.eclipse.org/#/details/demo.iot.device/DistanceSensor/1.0.1?s=distancesensor) in the Vorto Repository
- On the right side, click on the Arduino generator
- Store the ZIP file and extract the source code.
- Open the INO file in your Arduino IDE.

### 3. Registering to Eclipse Hono

Eclipse Hono provides a sandbox environment, which we can use in this tutorial to integrate the GrovePi with. So before we can send any data, we must register the device in Eclipse Hono. We use ```vortodemo``` as a **Hono tenant** and ```1234``` as a **device ID**:

**Registering our Device**

```sh
curl -X POST http://hono.eclipse.org:28080/registration/vortodemo
-i -H 'Content-Type: application/json'
-d '{"device-id": "1234"}'
```

**Adding device credentials**
```sh
PWD_HASH=$(echo -n "S3cr3t" | openssl dgst -binary -sha512 | base64 | tr -d '\n') 
curl -X POST http://hono.eclipse.org:28080/credentials/vortodemo
-i -H 'Content-Type: application/json'
-d 'JSON payload' 
```

Example JSON payload:
```sh
{
  "device-id": "1234",
  "auth-id": "1234",
  "type": "hashed-password",
  "secrets": [
    {
      "hash-function": "sha-512",
      "pwd-hash": "'$PWD_HASH'"
    }
  ]
}
```

### 4. Adjust the Arduino Project according to your needs.
The following important changes have to be made:

```sh
/* Your tenant in Eclipse Hono / Bosch IoT Hub */
#define hono_tenant "vortodemo"

/* Device Configuration */
String hono_deviceId = "1234";
String ditto_namespace = "org.eclipse.vorto";

/* MQTT broker endpoint */
const char* hono_endpoint = "ssl://hono.eclipse.org:8883";
const char* hono_password = "S3cr3t";
String hono_authId;

#if (USE_SECURE_CONNECTION == 1)
    /* SHA-1 fingerprint of the server certificate of the MQTT broker, UPPERCASE and spacing */
    const char* mqttServerFingerprint = "E6 A3 B4 5B 06 2D 50 9B 33 82 28 2D 19 6E FE 97 D5 95 6C CB";
#endif

/* WiFi Configuration */
const char* ssid = "<ENTER YOUR WIFI SSID>";
const char* password = "<ENTER YOUR WIFI PASSWORD>"; 
```

* For a secure connection, you need a fingerprint of the server certificate. This finger print is an SHA-1 hash of the certificate of the MQTT broker.

* If you want to create the fingerprint yourself [get the certificate](https://letsencrypt.org/certs/lets-encrypt-x3-cross-signed.pem.txt) and copy and paste the content in a .crt file. The fingerprint for the certificate can be extracted by invoking:

```sh
openssl x509 -noout -fingerprint -sha1 -inform pem -in [certificate-file.crt]
```

* Connect your ESP8266 to your computer and select the virtual COM port to which your device is connected in the Arduino IDE under **Tools -> Port** and upload the sketch.

* Follow [Consuming Messages from Java for Hono] (https://www.eclipse.org/hono/dev-guide/java_client_consumer/) to receive the device data from Eclipse Hono.

-------------
