---
menu:
  main:
    parent: 'Tutorials'
date: 2018-03-09T20:08:11+01:00
title: Connecting a GrovePi to Eclipse Hono
weight: 32
---

This tutorial explains how to generate a simple Python application for you GrovePi that sends sensor data to Eclipse Hono using MQTT.

<!--more-->

## Prerequisites

*   [Python 3.x](https://www.python.org/)
    
*   IDEs - for Python: [Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python)
    
*   [Paho Python Client](https://eclipse.org/paho/clients/python/)
    
*   [GrovePi Sensor Kit](https://www.dexterindustries.com/grovepi/)
    


### 1.  Setup your device.
In this example Raspberry Pi is used but you can use any device which can run python.

- [Install raspbian on the Raspberry Pi](https://www.raspberrypi.org/learning/software-guide/).
- [Connect the pi to wifi](https://www.raspberrypi.org/learning/software-guide/wifi/).
- [Enable ssh connection on your pi](https://www.raspberrypi.org/documentation/remote-access/ssh/) .
- [Install python and required modules]({{< relref "#installation-of-grovepi-required-modules" >}}).

        
### 2. Setup your development environment (on your development machine).

- [Install Visual Studio Code with the Python extension](https://code.visualstudio.com/docs/languages/python).
- [Install python and required modules]({{< relref "#installation-of-grovepi-required-modules" >}}).
        
### 3.  Generate application code using the Python generator.
    
- Open the [Distance Sensor](http://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorial/DistanceSensor/1.0.0) in the Vorto Repository.
- Click **Eclipse Hono Generator** and choose **Python**.
- Click **Generate**
- Download the ZIP file and extract the source code.
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
      infomodel.distance.sensorValue = ultrasonicRead(ultrasonic_ranger)
      infomodel.distance.sensorUnits = "cm"
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


## Installation of GrovePi required Modules

* Download and install the latest version of Python 3.x from the official [Python website](https://www.python.org/)

* [Install pip](https://packaging.python.org/tutorials/installing-packages/#install-pip-setuptools-and-wheel)

* Install Visual C++ Build Tools(http://landinghub.visualstudio.com/visual-cpp-build-tools). This is a pre-requisite for installing netifaces.

* Install the required Python modules
	 
	* [Eclipse paho](https://www.eclipse.org/paho/clients/python/)
	* [netinterfaces](https://pypi.python.org/pypi/netifaces)

		* open terminal or console and type the following command
		
				pip install paho-mqtt
				pip install netifaces

## What's next? 

- [Connect an ESP8266 to Eclipse Hono]({{< ref "tutorials/arduino.md" >}})
