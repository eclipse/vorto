# Integrating a device with Python into the Bosch IoT Hub using Vorto

This tutorial explains how to integrate a device with the Bosch IoT Suite using MQTT. Your device should already be created as a thing from an Information Model at this point.   
We will use our [RaspberryPi Information Model](https://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorials:RaspberryPi:1.0.0) again.

<img src="../images/tutorials/connect_grovepi/cover.png"/>

## Prerequisites

* [Python](https://www.python.org/downloads/) is installed

* Some code editor is installed (e.g. [VSCode](https://code.visualstudio.com/))

* Created a thing in the Bosch IoT Suite (refer to [Creating a Thing in the Bosch IoT Suite](create_thing.md)).

<br />

## Tools
> **Note**: This tutorial will only show the integration of the in-built temperature sensor of the RaspberryPi. Sending the data to hub, however, is similarilly done for the other sensors.

* [GPS module](https://learn.adafruit.com/adafruit-ultimate-gps-on-the-raspberry-pi?view=all)
* [Pi-Top Battery](https://www.pi-top.com/) ([Reading the battery status](https://github.com/rricharz/pi-top-setup))

<br />

## Steps
1. Setup your device
1. Download the generated integration script
1. Configure the scripts with the information of your created thing
1. Reading the sensor data
1. Start sending data

<br />

## Setup your device.
> **Note**: This tutorial will assume that your RaspberryPi is already running a version of [Raspbian](https://www.raspberrypi.org/learning/software-guide/) and is [connected to the network](https://www.raspberrypi.org/learning/software-guide/wifi/).   
You can either use a display, keyboard, and mouse to directly work on the RaspberryPi or [setup SSH](https://www.raspberrypi.org/documentation/remote-access/ssh/) and later copy your script over to it.

<br />

## Download the generated integration script

**1.** On the Vorto Repository page of your Information Model (we will use the [RaspberryPi Information Model](https://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorials:RaspberryPi:1.0.0)), click on the `Bosch IoT Suite` generator. This will trigger a pop up to appear with the available generators.     
<img src="../images/tutorials/create_thing/code_generators.png" />

**2.** We want to integrate a device using Python. Click the `Source Code` button to download the generated python script.
<img src="../images/tutorials/connect_grovepi/python-generator.png" height="500"/>

**3.** Unzip the downloaded file and open it in your code editor. 

**4.** In order to guarantee secure transmission of your data, the integration uses SSL. We therefore need a certificate.   
Right click and save the [iothub.crt](https://docs.bosch-iot-hub.com/cert/iothub.crt) file and place it in the root folder of the just unzipped folder.

<br />

## Configure the scripts with the information of your created thing

**5.** Before we start reading in the sensor data and sending it to Hub, we need to configure our credentials. This will allow the script to send the sensor data to our digital twin.   
The main `.py` file (`RaspberryPiApp.py` in this case) will contain a section that looks like this:   
```python
# DEVICE CONFIG GOES HERE
tenantId = "ADD TENANT HERE"
device_password = "ADD DEVICE PASSWORD HERE"
hub_adapter_host = "mqtt.bosch-iot-hub.com"
deviceId = "ADD DEVICE ID HERE"
clientId = deviceId
authId = "ADD AUTH ID HERE"
certificatePath = "./iothub.crt"
ditto_topic = "ADD TOPIC HERE, e.g. com.mycompany/4711"
```
> **Note:** Make sure to point the `certificatePath` at your just downloaded `.crt` file.

**6.** We will use the request response we got upon creating a thing with the postman script. Since it holds exactly the information we need, we can copy and paste the different ids from the response.
<img src="../images/tutorials/connect_grovepi/postman_json.png" />

<br />

## Reading the sensor data	

**7.** After filling in the credentials, we can start integrating our sensors.   
The implementation is done inside of the `periodicAction` function.   
This function is called periodically every `timePeriod` seconds. It is meant to periodically read your sensor's values and transmit them using the thing type as a data model layer and the Paho MQTT Client.

The variable `timePeriod` is set further down in the file. In this example, the variable `timePeriod` is set to 10.
```python
# Period for publishing data to the MQTT broker in seconds
timePeriod = 10
```

**8.** We will only cover the integration of the built-in cpu temperature sensor here.   
The code contains comments which will tell you where to edit code and do your implementation.   
There is no need to edit any other section of the python file.

Code sections which can be customized for your needs are marked with
```python
### BEGIN SAMPLE CODE
...
### END SAMPLE CODE
```

In order to read the cpu temperature, we need to add this code block inside the `periodicAction` function between the `BEGIN` and `END` comments.
```python
def get_cpu_temperature():
    with open("/sys/class/thermal/thermal_zone0/temp", "r") as tempFile:
        temp = float(tempFile.read())
        tempC = temp/1000
        return tempC
    
# Read the data and assign it to the functional block properties
infomodel.cpuTemperature.value = {
    "currentMeasured" : get_cpu_temperature()
}
```

> **Note:** In order to implement the sensors in the right way, we have to look at the specifc paths of the attributes.   
<details>
    <summary>
        <b>
            How do I find the specific path?
        </b>
    </summary> 
    

    
The `cpuTemperature` in our [Raspbi IM](https://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorials:RaspberryPi:1.0.0) is a [Temperature Function Block](https://vorto.eclipse.org/#/details/org.eclipse.vorto:Temperature:1.0.0). 
```js
infomodel RaspberryPi {
	functionblocks {
		...
		cpuTemperature as Temperature
	}
}
```

This means we have to check the internals of this FB.   
```js
functionblock Temperature {
    status {
      value as SensorValue
    }
}
```
As we can see, it has a `value` which is a [SensorValue Data Type](https://vorto.eclipse.org/#/details/org.eclipse.vorto.types:SensorValue:1.0.0). This DT has one mandatory value, `currentMeasured` and two optional ones.   
Since the nesting ends with this DT, we know that the path to our `currentMeasured` and optional values is:   
```js
cpuTemperature -> value -> currentMeasured, minMeasured, ...
```

Therefore the structure will look like this:
```python
cpuTemperature.value.currentMeasured
cpuTemperature.value.minMeasured
cpuTemperature.value.maxMeasured
```

Which in combination with the parent element is represented like this:
```js
infomodel.cpuTemperature.value = {
    "currentMeasured" : 0.0,
    "minMeasured": 0.0,
    "maxMeasured": 0.0
}
```
</details>

<br />

## Start sending data

**9.** Before we can run the script, we need to install additional dependencies. The bundle contains a file called `requirements.txt`. This file describes the dependencies that still have to be installed in order to run the script.      
Install the dependencies using the `pip install -r requirements.txt` command which looks at the current `requirements.txt` file and installs the dependecies listed there.

**10.** Once the dependencies are installed, we can start the script by simply typing `python RaspberryPiApp.py`.
After starting the script using you Terminal, you should be able to observe the sensor data being sent to Hub. This will looks something like this:

```bash
Publish Payload:  {"topic": "com.bosch.xyz.sandbox/raspbi123/things/twin/commands/modify","headers": {"response-required": false},"path": "/features/location/properties","value" : {"status" : {"latitude": 1.34749, "longitude": 103.841133}, "configuration" : {} } }  to Topic:  telemetry/t0e7d25ea57db4191ab41415a432f5a50_hub/com.bosch.xyz.sandbox:raspbi123
Publish Payload:  {"topic": "com.bosch.xyz.sandbox/raspbi123/things/twin/commands/modify","headers": {"response-required": false},"path": "/features/battery/properties","value" : {"status" : {"remainingCapacity": {"value": 86}, "value": {"currentMeasured": 86, "minMeasured": 0, "maxMeasured": 100}}, "configuration" : {"remainingCapacityAmpHour": 0} } }  to Topic:  telemetry/t0e7d25ea57db4191ab41415a432f5a50_hub/com.bosch.xyz.sandbox:raspbi123
```

> If you don't see the Terminal outputting this kind of sensor data packages, unfold the following section.
<details>
    <summary>
        <b>
            Your script is not sending data?
        </b>
    </summary> 

#### Enable Logging
If you started the script and you can't see any data being sent your script most likely contains an error in the way the values are assigned to the attributes.

In order to see the error message, you need to add a logger to the script.   
This is easily done by adding the logging module at the top:
```python
import logging
logging.basicConfig(level=logging.DEBUG)
```

And near the end of the file, where the client is created, you have to only paste in those two lines:

```python
# Create the MQTT client
client = mqtt.Client(hono_clientId)
...

logger = logging.getLogger(__name__)
client.enable_logger(logger)
```

<br />

#### Reset the Bridge (Connection of the Asset Communication Package)
If you see the sensor data being sent as shown in the `Publish Payload:...` example above but you still can't see any changes in the Dashboard or SwaggerUI API when retrieving the current state of the Thing, the connection between Hub and Things might have corrupted at some point.

This is simple to solve. This so called "Bridge" is created by default one subscribing to the Asset Communication.
1. Head over to your [Subscriptions page](https://accounts.bosch-iot-suite.com/subscriptions/) and click on the **Go to Dashboard** button of your Asset Communication Subscription.

2. On the newly opened tab, click on the **Connection** tab that will display all the connections you've set up.
By default there will be a **Telemetry Bosch IoT Hub** connection.

> **Note** that if there is a problem, you will see a triangle with an exclamation mark inside that indicates some failed connection.

3. Click on the connection and hit the **Close connection** button. Wait until the connection is closed.

4. Once the connection was closed, re-open it by clicking the **Open connection** button.

5. After the connection has been re-established, **restart your sensor data sending script** and observe.

</details>

<details>
    <summary>
        <b>
            Your get an error message?
        </b>
    </summary> 

#### `getaddr` - Double check your connection.
If you started the script and you get an `getaddr` error, this means that the address resolution could not take place. This can have several reasons.    
However, it's most likely that you either are not connected to the internet or a **proxy** is blocking the sending of sensor data.

Unfortunately, for now you can't set any proxy. **Please switch to a network without Proxy.**

<br />

#### `Connection to MQTT broker failed: 4` - Double check your configuration 
If you see the sensor a log message with the error value of `4`, this means that the connection was refused due to some bad user name or password. However, this can relate to any of the fields in the configuration tab.

Therefore you have to go in and double check every field of the configuration section for an additional space or some character that does not belong there.

>  Please note that if you have followed the other tutorials, this error is **definitely** due to a fault in entering the credentials correctly.

</details>

**11**. We can now verify that there is data incoming by either using
- the [Vorto Dashboard](create_webapp_dashboard.md) that simply displays your data in different UI widgets.
- or the [SwaggerUI](https://apidocs.bosch-iot-suite.com/?urls.primaryName=Bosch%20IoT%20Things%20-%20API%20v2) which doesn't require anything to be installed and allows a quick insight into whether your data is updating.

##### Once you can see your data updating, you have successfully connected your ESP8266 device to the Bosch IoT Suite!    Now you can go in and connect and integrate the GPS and battery.

<br />

## What's next ?

 - [Use the Vorto Dashboard](create_webapp_dashboard.md) to visualize the device data in UI widgets.
 - [Generate an OpenAPI Spec for your device](create_openapi.md)
- Integrate your device with the Bosch IoT Suite using:
  - [Arduino](./connect_esp8266.md)
  - [Java](./connect_javadevice.md)

---

In case you're having difficulties or facing any issues, feel free to [create a new question on StackOverflow](https://stackoverflow.com/questions/ask?tags=eclipse-vorto) and we'll answer it as soon as possible!   
Please make sure to use `eclipse-vorto` as one of the tags. 
