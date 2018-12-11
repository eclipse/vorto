---
menu:
  main:
    parent: 'Tutorials'
date: 2016-03-09T20:08:11+01:00
title: How to describe a TI SensorTag device with Vorto
weight: 34
---
This tutorial explains how to describe a device with Vorto using the example of a [TI SensorTag](http://www.ti.com/tool/TIDC-CC2650STK-SENSORTAG).
Read more about the benefits of Vorto in this [blog post](https://blog.bosch-si.com/developer/avoid-tight-coupling-of-devices-in-iot-solutions/).

### Prerequisite
* [Github](https://github.com/) account
* Device you want to describe
* Related specifications


### Let's get started

Before you start, you need to understand what kind of information you need to describe your device with Vorto. You might find yourself confronted with a long list of specifications, but not all of them are needed.
The SensorTag in our example consists of a temperature, movement, humidity, barometric pressure and optical sensor. These five functionalities are what we want to describe in our *Information Model* and are defined in the *Function Blocks* respectively. 

To keep this example simple, we will focus only on the humidity sensor. The remaining functionalities can be modeled the same way.
As Vorto is all about using generic classes and reducing integration effort, we will make use of the already existing description of a [humidity sensor](http://vorto.eclipse.org/#/details/org.eclipse.vorto:Humidity:1.0.0?s=humid) you can conveniently find in the [Vorto Repository](http://vorto.eclipse.org/). 
This is how the template looks like:
```
namespace org.eclipse.vorto
version 1.0.0
displayname "Humidity"
description "Functionblock for Humidity"
category sensors
using org.eclipse.vorto.types.SensorValuePercentage;1.0.0

functionblock Humidity {
    status {
        value as SensorValuePercentage
    }
}
```
As we can see, it only consists of a single *status property*. This value is defined as *entity*, at which we can have a closer look when clicking on *SensorValuePercentage:1.0.0* under *References*.
<figure class="screenshot">
![](/images/tutorials/tisensor/tisensor_model_overview.jpg)
</figure>

This entity specifies the sensor data in three values:
```
entity SensorValuePercentage {
    mandatory currentMeasured as Percentage
    optional  minMeasured     as Percentage
    optional  maxMeasured     as Percentage
}
```
Our sensor only delivers the *currentMeasured* value and doesn't record the minimum or maximum value. But as the two values are defined as *optional*, we can still make use of this template. 
How exactly the values send by the sensor are converted to fit into this format is part of a *mapping* and shouldn't concern us at this point. You can find a follow-up tutorial [here](https://github.com/eclipse/vorto/blob/development/mapping-engine/Readme.md).
For now, we only focus on the abstract description of the device.

But as we can see in the [listed specifications](http://processors.wiki.ti.com/index.php/CC2650_SensorTag_User's_Guide#Humidity_Sensor), the sensor has four different characteristics that need to be described in the *Function Block*: Data, Notification, Configuration and Period. 
<figure class="screenshot">
![](/images/tutorials/tisensor/tisensor_model_specs.png)
</figure>

As of now, we only have the measured value as a *status property*. In order to describe the sensor completely, we simply extend the existing *Humidity Function Block* by adding more functions. To do so, we [create a new model](https://www.eclipse.org/vorto/userguide/create_model/) by clicking on <img height="30" src="/images/tutorials/tisensor/tisensor_create_model_button.png"/>, select *Function Block*, decide on a name (e.g. *Humidity*) and store it in the corresponding namespace, in this case *org.eclipse.vorto.tutorial.tisensortag*. 
<figure class="screenshot">
![](/images/tutorials/tisensor/tisensor_create_model.png)
</figure>


Add *extends Humidity* after the name of your *Function Block* and reference to the model you want to inherit from. Use the ![](/images/tutorials/tisensor/tisensor_lookup_button.png) button to search for the model and copy the *namespace* to the clipboard.
<figure class="screenshot">
![](/images/tutorials/tisensor/tisensor_lookup_models.jpg)
</figure>



After pasting the copied *namespace* into your model, the *Function Block* should look like this: 
```
namespace org.eclipse.vorto.tutorial.tisensortag
version 1.0.0
displayname "Humidity"
description "Functionblock for Humidity"
using org.eclipse.vorto.Humidity;1.0.0

functionblock Humidity extends Humidity {
    configuration {}
    status {}
    events {}
    operations {}
}
```
* The first property is called *configuration*. As stated in the [User Guide](https://www.eclipse.org/vorto/userguide/quickhelp_dsl/), it consists of "Read- and Writable properties that can be set on the device". Looking at the specifications of our device, we can easily see that we can enable notifications, data collection and set the interval period on the sensor. This directly matches with the definition of settings that configure the functionality of the device for the future.
We define the configuration of the notifications and data collection as *boolean*, as they can only have the status *enabled* or *disabled* (*true* or *false*). The interval period only accepts values from 100 ms to 2,550 ms with a resolution of 10 ms. We could use [*enum*](https://www.eclipse.org/vorto/userguide/quickhelp_dsl/) as data type as we have a finite number of values, but we want to keep this model as generic as possible. Therefore we define the period as *int* in the model and specify it more in the *mappings*.
The *configuration* block now looks like this (the names can differ of course):

```
configuration {
    mandatory notifications_enable  as boolean "Function to enable notifications."
    mandatory sensor_enable         as boolean "Function to enable data collection."
    mandatory period                as int     "Function to set interval period."
}
```
* *Status* includes properties that are indicating the current state of the device. This could be the firmware or the battery level for example. Important to notice is that these information are inquired by the user or the backend and not send automatically by the device. In our case we only have the sensor value as a status property. As we inherit this function from the *Humidity Function Block*, we don't need to model this again and the block therefore stays empty:

```
status {}
```

* *Events* are properties that are published by the device. This means that the sensor sends these data actively for example on state changes or after a certain amount of time. If the humidity sensor is enabled, it generates a new value as configured by the interval period and can therefore be seen defined as an *event*. An *event* consists of methods describing the event and variables specifying the data send.
Here we only have one event, namely the generation of a new value after a certain amount of time. For this we use the same definition of the sensor value as in *Humidity*. Don't forget to reference back to this *entity*.

```
using org.eclipse.vorto.types.SensorValuePercentage;1.0.0
events {
    new_value {
        value as SensorValuePercentage
    }
}
```

* We don't have a property that qualifies as *operations* in our example, however we will mention it here for completeness.  
A operation indicates a functionality that may lead to device state changes or give additional meta-data information. In contrast to a *configuration*, this only refers to one-time or short-term changes on the device. For example if pushing a button would trigger a certain notification. 

We have thus completed the *Function Block* for our humidity sensor. Now we have to include it into the overall *Information Model*. 
After referencing to the respective *Namespace* you can list all the needed *Function Blocks*.
```
namespace org.eclipse.vorto.tutorial
version 1.0.0
displayname "TISensorTag"
description "InformationModel for TISensorTag"
using org.eclipse.vorto.tutorial.tisensortag.Temperature;1.0.0
using org.eclipse.vorto.tutorial.tisensortag.Movement;1.0.0
using org.eclipse.vorto.tutorial.tisensortag.Humidity;1.0.0
using org.eclipse.vorto.tutorial.tisensortag.BarometricPressure;1.0.0
using org.eclipse.vorto.tutorial.tisensortag.Optical;1.0.0

infomodel TISensorTag {
    functionblocks {
        optional  temperature        as Temperature
        mandatory movement           as Movement
        mandatory humidity           as Humidity
        mandatory barometricPressure as BarometricPressure
        mandatory optical            as Optical
    }
}
```
Careful readers surely noted that we used the key word *optional* for the temperature sensor instead of *mandatory*. That is because this sensor is no longer manufactured and is not fitted on SensorTags produced after June 2017. But as we want to keep the model as general as possible, we still include it to make the model suitable for all SensorTags, even the outdated ones that are still on the market.


### Closing remarks
Once you are satisfied with your created model, you can request a review by the Vorto Team. After the revision the model might be released and made accessible for other users.
For more information on *Model States* click [here](https://www.eclipse.org/vorto/userguide/model_states/).
<figure class="screenshot">
![](/images/tutorials/tisensor/tisensor_lifecycle.jpg)
</figure>

### Next steps
* Now it is time to specify how the arbitrary data stream from and to your device is adjusted to the abstract framework of the Vorto model. This is done with the [mapping feature](https://github.com/eclipse/vorto/blob/development/mapping-engine/Readme.md) and will be explained in a follow-up tutorial we are currently working on. 
* To further leverage the work you can convert your *Information Model* with the help of [Code Generators](https://www.eclipse.org/vorto/userguide/generators/) into a platform-specific source code, that runs on the device and integrates with the specific IoT backend.





