# Describing a device with Vorto in 5 minutes

This tutorial explains how to describe a device with Vorto in just a few minutes. The device we are going to create will have the following capabilities:

- Measuring the Outdoor temperature
- Measuring the Indoor temperature
- Reading the Geo Location of these measurements.

Let's get started.

### Prerequisite
* [BoschID](https://accounts.bosch-iot-suite.com/) Account or [GitHub](https://github.com/) 
* You are a collaborator/owner of a namespace

## Steps to follow

1. Log in to [Vorto Repository](https://vorto.eclipse.org) with your BoschID or Github Account

2. Select **Describe device** 

3. Choose **Information Model** and confirm with **Next** 

4. Specify the model ID and confirm with **Next**
	1. Input the **namespace**, e.g. com.mycompany 
	2. Input the **name**, e.g. MyWeatherStation
	3. Input the **version**, e.g. 1.0.0
	
5. Choose **Vorto abstraction**, that provide you with a list of abstracted capabilities.

6. Select **Temperature** from the drop-down list, specify the property name **indoorTemperature** and confirm with **Add**

7. Select **Temperature** from the drop-down list, specify the property name **outdoorTemperature** and confirm with **Add**

8. Select **Location** from the drop-down list, specify the property name **location** and confirm with **Add**

9. Verify your selected properties and click **Create**. This creates the model and opens its details page:

Your model should look like this:

	
	namespace org.mycompany
	version 1.0.0
	displayname "MyWeatherStation"
	description "InformationModel for MyWeatherStation"
	using org.eclipse.vorto.Temperature;1.0.0
	using org.eclipse.vorto.Location;1.0.0
	
	infomodel MyWeatherStation {
	    functionblocks {
	        mandatory indoorTemperature as Temperature
			mandatory outdoorTemperature as Temperature
	        mandatory location as Location
	    }
	}


**Congrats**! You have just created an Information Model for a device. Check out the **What's next** section below that explains how you can further use the model to integrate the device with the Bosch IoT Suite.

## What's next?

- [Generate Python Code](mqtt-python.md) that integrates the device with the Bosch IoT Suite.
- [Visualize device data](create_webapp_dashboard.md) in a Node.js web application.
- [Describe a TI SensorTag with Vorto](describe_tisensor.md)





