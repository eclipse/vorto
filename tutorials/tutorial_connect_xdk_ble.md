# Connecting a Bluetooth device with Eclipse Vorto & Eclipse Kura to the Bosch IoT Suite

In this tutorial we would like to show you how you can connect a bluetooth device to the Bosch IoT Suite with Eclipse Kura and Eclipse Vorto.

## Prerequisite

- An information model published to the Vorto Repository. [Read more](tutorial-create_and_publish_with_web_editor.md)
- Evaluation account for the Bosch IoT Suite. [Request account here](https://bosch-si.secure.force.com/content/FormDisplayPage?f=2abiE).
- Device has been successfully registered in the Bosch IoT Suite. [Read more](tutorial_register_device.md) 

## Tools
- [Eclipse Oxygen](https://www.eclipse.org/downloads/packages/eclipse-ide-java-and-dsl-developers/oxygenr)
- [mToolkit](http://mtoolkit-neon.s3-website-us-east-1.amazonaws.com) for deploying Kura bundles to the Pi
- [Kura's Developer's Workspace Archive](http://www.eclipse.org/downloads/download.php?file=/kura/releases/3.0.0/user_workspace_archive_3.0.0.zip)
- [Eclipse XDK Workbench 2.0.1](https://xdk.bosch-connectivity.com/software-downloads) for flashing the XDK firmware


## Steps

### 1. Install Kura on a Raspberry Pi

1. <a href="http://eclipse.github.io/kura/intro/raspberry-pi-quick-start.html">Install the Kura gateway on a Raspberry Pi</a>. The easiest path would be to use a Raspberry Pi 3 since it already contains bluetooth that is supported. If you use a bluetooth adapter, make sure it is supported by the operating system

2. Modify the Kura gateway on the Raspberry Pi

- Change from OpenJDK to Oracle JDK
	
```
sudo apt-get install oracle-java8-jdk
sudo update-alternatives --config java
```

- Add some configuration in Kura init

```
sudo nano /opt/eclipse/kura/kura/config.ini
```

- Add this line to the end of the file

```
org.osgi.framework.bootdelegation=sun.*,com.sun.*
```

- Reboot with `sudo reboot`
- On the Kura web application, change the necessary firewall rules to suit your development environment
	- The Kura gateway will create its own wifi access point. If you have a second network on the Raspberry Pi (i.e Using the Ethernet cable), join that network. Check the raspberry pi for its IP
on that network. Use that IP Address to reach the Kura web application. It is on http://[IP Address]/kura.
	- If your raspberry pi doesn't have a second network, you can join the Wifi Access Point created by the Kura gateway, and reach the Kura web application from there via it's IP on that network.

### 2. Create Kura Development Environment

More comprehensive tutorial here: <a href="http://eclipse.github.io/kura/dev/kura-setup.html">http://eclipse.github.io/kura/dev/kura-setup.html</a>

- Start your Eclipse IDE
- Install <a href="http://mtoolkit-neon.s3-website-us-east-1.amazonaws.com">mToolkit</a> as new software in Eclipse
- Download <a href="http://www.eclipse.org/downloads/download.php?file=/kura/releases/3.0.0/user_workspace_archive_3.0.0.zip">Kura's Developer's Workspace Archive</a>
- Unzip Developer's Workspace Archive to workspaceImport the unzipped projects
- Go to "Target Definition" project, click on kura-equinox_3.11.1.target, and click "Set as Target Platform"

### 3. Generate XDK Kura Bundle with Vorto

- Go to the <a href="http://vorto.eclipse.org/#/details/com.bosch.devices/XDK/1.0.0">XDK Information Model</a> in the Vorto Repository
- Click on the **Eclipse Kura Generator**:

	<img src="./images/connect_xdk_kura/step5_4.png"/>

- Select **Bluetooth LE** and **Bosch IoT Suite**

	<img src="./images/connect_xdk_kura/step5_4_1.png" width=50%"/>

- Confirm your selection with **Generate**
- Download the generated Kura bundle, unzip and import it to Eclipse workspace. The project will appear as _com.example.kura_.
- Add dependencies to XDK Kura bundle by executing `mvn dependency:copy-dependencies` in the root of the kura project
- In Eclipse, refresh your project, then right-click on your project, go to _Plug-in Tools_, and click on _Update classpath_
- Rebuild your project and fix the import errors

### 4. Configure the bundle for the Bosch IoT Suite

- Download <a href="https://github.com/bsinno/iot-things-examples/blob/master/cr-integration-api-examples/common/src/main/resources/bosch-iot-cloud.jks">bosch-iot-cloud.jks</a> and store it in a "secret" folder of the kura project
- Create a public and private key pair for your solution. Store the CRClient in the "secret" folder as well
```
keytool -genkeypair -noprompt 
-dname "CN=-, OU=-, O=-, L=-, S=-, C=-" 
-keyalg EC -alias CR -sigalg SHA512withECDSA 
-validity 365 -keystore CRClient.jks
```
- Extract the public key information into a separate file
```
keytool -export -keystore CRClient.jks -alias CR -rfc -file CRClient_key.cer
```
- Print the public key to the command prompt
```
keytool -printcert -rfc -file CRClient_key.cer
```
- Open the Things Adminstration Dashboard for your solution (sent to you via Email during evaluation account registration) and submit your public key by copy&pasting the key from the command prompt

	<img src="./images/connect_xdk_kura/step3_5_publickey.png" width="50%"/>

### 5. Flash XDK firmware

- Download XDK workbench [<a href="https://xdk.bosch-connectivity.com/software-downloads">https://xdk.bosch-connectivity.com/software-downloads</a>
- Open your XDK workbench and import the <a href="./tutorials/examples/SensorsToBle.zip">XDK Firmware</a> project 
- Flash the firmware project onto the XDK device (Please consult the XDK manual for doing this

### 6. Read temperature from Bluetooth

- In _ThingClientFactory.java_, add your network proxy if you need one and uncomment the line below:
```.proxyConfiguration(proxy)```

- In XDKDevice.java, in the method getResourceId(), modify the method to how you intend to generate the ThingID of your XDK Thing. Make sure this aligns with the ThingID of the Thing you precommissioned in Chapter 2 of this guide.
```return "demo.vorto.example:" + getBluetoothDevice().getAdress().replace(":", ""); ```

- In XDKDevice.java, in the method enableTemperature(), change the entire method to
```this.bluetoothGatt.writeCharacteristicValue("0x0013", "0100");```
This will turn-ON notifications for the XDK Bluetooth LE

- In XDKDevice.java, in the method readTemperature(), change the entire method to

```
TemperatureSensor temperature = new TemperatureSensor();
try {
	this.bluetoothGatt.writeCharacteristicValue("0x0010", "74656D70");
	String btReturnValue = this.bluetoothGatt.readCharacteristicValue("0x0012");
	String[] btReturnValues = btReturnValue.split("\s+");
	StringBuilder number = new StringBuilder();
	for(String hex : btReturnValues) {
    	number.append((char) Integer.parseInt(hex, 16));
	}

	temperature.setSensorValue((float) (Float.parseFloat(number.toString())/1000.0));
	temperature.setUnits("Celsius");
} catch (KuraException e) {
	logger.error("Error in Sending Temperature", e);
}
return temperature;
```
Here we are writing the ascii representation of the string "temp" to the XDK register to get a reading of the temperature which we then read back and parse.

### 7. Deploy Kura bundle to gateway

Let us now deploy the XDK Kura bundle to the Kura gateway on the Raspberry Pi.

- Connect to Kura gateway wifi (created on Step 1) or if your gateway has a second network connection, make sure to be on the same network.
- Open mToolkit view _(Windows > Show View > Other > mToolkit > Frameworks)_
- Add Framework
	- Click on Add Framework icon on top right
	- Put IP of running Kura Gateway on IP Address
	- Make sure running Kura Gateway's port 1450 is open (configure this on Kura gateway app)

- Right click on created framework and click Connect Framework
- Right click on Kura bundle (com.example.kura), go to "Install To", and click on the Framework you added.
- Verify that your plugin was installed by looking for it under Bundles
- Alternatively, you can verify by looking for it in the Kura web app (Device > Bundles)

### 8. Configure the XDK Kura Bundle

- Go to the Kura webapp and look for the configuration page of our Bundle

	<img src="./images/connect_xdk_kura/step5_9.png"/>

- Configure the Bosch IoT Things Solution Id. You can lookup the solution Id in the Bosch IoT Things Admin Dashboard. Check the email that has been sent to you during the Evaluation Account Registration

	<img src="./images/connect_xdk_kura/step5_9_2.png"/>

- Enable scanning 

	<img src="./images/connect_xdk_kura/step5_9_3.png"/>

- Enable temperature

	<img src="./images/connect_xdk_kura/step5_9_4.png"/>

### 9. Verify incoming sensor data

To check if the temperature sensor data is being sent successfully to the cloud, just execute the following curl command:
 
```
curl -X GET https://things.apps.bosch-iot-cloud.com/api/1/things?ids=ADD_THINGID_HERE 
-H "Authorization: Basic credentials" 
-H "Accept: application/json" 
-H "x-cr-api-token: apiToken"
```
Alternatively, you can the see the incoming sensor data via the <a href="http://Bosch IoT Developer Console">Bosch IoT Developer Console</a>

## What's next ?

- [Create a web application consuming the device telemetry data](tutorial_create_webapp_dashboard.md)
- [Build an Amazon Alexa Skillset to voice-control the device](tutorial_build_alexaskill.md)