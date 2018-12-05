---
menu:
  main:
    parent: 'Tutorials'
date: 2016-03-09T20:08:11+01:00
title: Connecting an ESP8266 to Eclipse Hono
weight: 31
---

This tutorial explains how to generate an Arduino sketch for a given Information Model and send the device data to Eclipse Hono via MQTT.

<!--more-->

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
    
- Open the [Distance Sensor](http://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorial/DistanceSensor/1.0.0) in the Vorto Repository
- Click **Eclipse Hono Generator** and select **Arduino** as device platform. Confirm with **Generate**.
- Download the ZIP file and extract the source code.
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

## What's next? 

- [Connect a GrovePi to Eclipse Hono]({{< ref "tutorials/grovepi.md" >}})
