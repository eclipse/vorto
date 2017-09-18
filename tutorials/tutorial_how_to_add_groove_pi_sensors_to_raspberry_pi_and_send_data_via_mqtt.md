# How to add GrovePi sensors to raspberry pi and send data to Bosch IoT Suite via MQTT?

In this tutorial we are going to show you how to add [GrovePi](https://www.dexterindustries.com/grovepi/) sensors to the raspberry pi and read data from it to send to Bosch IoT Suite.

## Pre-requisites

* Please go through this tutorial - [How to connect device to Suite via MQTT Python Generator?](tutorial-connect_device_via_mqtt_python_generator.md)
* The functional blocks of the information model you have chosen must match with the sensor you are going use. In this tutorial, ultrasonic sensors are used and it can measure distance. So the information model must have a distance sensor functional block. If you are using another sensor select the model accordingly.

## Tools

* [GrovePi Sensor Kit](https://www.dexterindustries.com/grovepi/)

## Steps

### Install GrovePi python dependencies on the pi

First step is to install necessary dependencies for GrovePi on the Raspberry pi. To do so, just execute the commands 

```
	sudo curl https://raw.githubusercontent.com/DexterInd/Raspbian_For_Robots/master
	upd_script/fetch_grovepi.sh | bash

	sudo reboot

```

You can find full instructions [here](https://www.dexterindustries.com/GrovePi/get-started-with-the-grovepi/setting-software/)

### Connect the sensor to the pi

In this step select a sensor from the kit and connect it to the Raspberry Pi. In this tutorial, ultrasonic sensor is used. The ultrasonic sensor connected to **pin "3"** on the GrovePi.

### Update the application code

Next step is update few lines of code before you can run it.

* Import the GrovePi dependencies

	```
	from grovepi import *	
	``` 
* Define a constant and assign the pin number of the pin to which the sensor is physically connected

	```
	ultrasonic_ranger = 3
	```
* Read the data and assign it to the funcational block properties

	```
	    infomodel.distancesensor.distance = ultrasonicRead(ultrasonic_ranger)
    	infomodel.distancesensor.sensor_units = "cm"
	```
* Thats all

### Run the application

Now you have updated the code. You can go ahead and run the code. Now the data will read from the sensor and send it to the Suite via MQTT.

Thats it. Have fun!


