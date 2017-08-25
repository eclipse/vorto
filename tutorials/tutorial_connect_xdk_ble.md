# Connecting a Bluetooth device with Eclipse Vorto & Eclipse Kura to the Bosch IoT Suite

In this tutorial we would like to show you how you can connect a bluetooth device to the Bosch IoT Suite with Eclipse Kura and Eclipse Vorto.

# Prerequisite

- An information model published to the Vorto Repository. [Read more](tutorial-create_and_publish_with_web_editor.md)
- Evaluation account for the Bosch IoT Suite. [Request account here](https://bosch-si.secure.force.com/content/FormDisplayPage?f=2abiE).
- Device has been successfully registered in the Bosch IoT Suite. [Read more](tutorial_register_device.md) 

# Tools
- [Eclipse Oxygen](https://www.eclipse.org/downloads/packages/eclipse-ide-java-and-dsl-developers/oxygenr)
- [mToolkit](http://mtoolkit-neon.s3-website-us-east-1.amazonaws.com) for deploying Kura bundles to the Pi
- [Kura's Developer's Workspace Archive](http://www.eclipse.org/downloads/download.php?file=/kura/releases/3.0.0/user_workspace_archive_3.0.0.zip)
- [Eclipse XDK Workbench 2.0.1](https://xdk.bosch-connectivity.com/software-downloads) for flashing the XDK firmware


# Steps

<table>
  <tbody>
    <tr>
      <td colspan="1">1</td>
      <td colspan="1">
        <p>Install the Kura gateway on a Raspberry Pi - <a href="http://eclipse.github.io/kura/intro/raspberry-pi-quick-start.html">http://eclipse.github.io/kura/intro/raspberry-pi-quick-start.html</a>
        </p>
        <ul>
          <li>The easiest path would be to use a Raspberry Pi 3 since it already contains bluetooth that is supported. If you use a bluetooth adapter, make sure it is supported by the operating system.</li>
        </ul>
      </td>
    </tr>
    <tr>
      <td colspan="1">2</td>
      <td colspan="1">
        <p>Modify the Kura gateway on the Raspberry Pi</p>
        <ul>
          <li>
            <p>Change from OpenJDK to Oracle JDK</p>
            <pre><code>sudo apt-get install oracle-java8-jdk
sudo update-alternatives --config java</code></pre>
			</li>
	      <li>
	        <p>Add some configuration in Kura init </p>
	        <pre><code>sudo nano /opt/eclipse/kura/kura/config.ini</code></pre>          <p>Add this line to the end of the file</p>
	        <ac:structured-macro ac:name="code">
	          <pre><code>org.osgi.framework.bootdelegation=sun.*,com.sun.*</code></pre>          </li>
	      <li>
	        <p>Reboot.</p>
	        <pre><code>sudo reboot</code></pre>          </li>
	      <li>On the Kura web application, change the necessary firewall rules to suit your development environment<ul>
	          <li>
	            <p>The Kura gateway will create its own wifi access point. If you have a second network on the Raspberry Pi (i.e Using the Ethernet cable), join that network. Check the raspberry pi for its IP<br/> on that network. Use that IP Address to reach the Kura web application. It is on http://[IP Address]/kura.</p>
	          </li>
	          <li>
	            <p>If your raspberry pi doesn't have a second network, you can join the Wifi Access Point created by the Kura gateway, and reach the Kura web application from there via it's IP on that network.</p>
	          </li>
	        </ul>
	      </li>
	    </ul>
	  </td>
	</tr>
	<tr>
	  <td colspan="1">3</td>
	  <td colspan="1">
	    <p>Create a Kura Development Environment (More comprehensive tutorial here: <a href="http://eclipse.github.io/kura/dev/kura-setup.html">http://eclipse.github.io/kura/dev/kura-setup.html</a>)</p>
	    <ul>
	      <li>Start your Eclipse IDE</li>
	      <li>Install <a href="http://mtoolkit-neon.s3-website-us-east-1.amazonaws.com">mToolkit</a> as new software in Eclipse</li>
	      <li>Download <a href="http://www.eclipse.org/downloads/download.php?file=/kura/releases/3.0.0/user_workspace_archive_3.0.0.zip">Kura's Developer's Workspace Archive</a></li>
	      <li>Unzip Developer's Workspace Archive to workspaceImport the unzipped projects</li>
	      <li>Go to "Target Definition" project, click on kura-equinox_3.11.1.target, and click "Set as Target Platform"</li>
	    </ul>
	  </td>
	</tr>
	<tr>
	  <td colspan="1">4</td>
	  <td colspan="1">
	    <p>Generate the XDK Kura Bundle from Vorto</p>
	    <ul>
	      <li>Go to the <a href="http://vorto.eclipse.org/#/details/com.bosch.devices/XDK/1.0.0">XDK Information Model</a> in the Vorto Repository</li>
	      <li>Click on the <strong>Eclipse Kura Generator</strong>:<br/>
	        <br/>
	          <img src="./images/connect_xdk_kura/step5_4.png"/>
	      </li>
	      <li>Select <strong>Bluetooth LE</strong> and <strong>Bosch IoT Suite</strong>
	        <br/>
	        <img src="./images/connect_xdk_kura/step5_4_1.png" width=50%" height="50%"/>
	      </li>
	      <li>Confirm your selection with <strong>Generate</strong>
	      </li>
	    </ul>
	  </td>
	</tr>
	<tr>
	  <td colspan="1">5</td>
	  <td colspan="1">Unzip the generated XDK Kura bundle to Eclipse workspace and import. The project will appear as <em>com.example.kura</em>.</td>
	</tr>
	<tr>
	  <td colspan="1">6</td>
	  <td colspan="1">
	    <p>Add dependencies to XDK Kura bundle</p>
	    <ul>
	      <li>In the commandline, go to the directory of the project and execute 'mvn dependency:copy-dependencies'</li>
	      <li>In Eclipse, refresh your project, then right-click on your project, go to "<em>Plug-in Tools</em>", and click on "<em>Update classpath</em>"</li>
	      <li>Rebuild your project and fix the import errors</li>
		  <li>Download <a href="https://github.com/bsinno/iot-things-examples/blob/master/cr-integration-api-examples/common/src/main/resources/bosch-iot-cloud.jks">bosch-iot-cloud.jks</a> and store it in a "secret" folder of the kura project</li>
		  <li>
		Create a public and private key pair for your solution. Store the CRClient in the "secret" folder as well:
		<pre><code>keytool -genkeypair 
		-noprompt 
		-dname "CN=-, OU=-, O=-, L=-, S=-, C=-" 
		-keyalg EC -alias CR 
		-sigalg SHA512withECDSA 
		-validity 365 -keystore CRClient.jks</code></pre>
		</li>
		<li>
		Extract the public key information into a separate file:
		<pre><code>keytool -export -keystore CRClient.jks -alias CR -rfc -file CRClient_key.cer </code></pre>
		</li>
		<li>
		Print the public key to the command prompt: 
		<pre><code>keytool -printcert 
		-rfc 
		-file CRClient_key.cer</code></pre>
		</li>
		<li>
		Open the Things Adminstration Dashboard for your solution (sent to you via Email) and submit your public key by copy&pasting the key from the command prompt:<br/>
		<img src="./images/connect_xdk_kura/step3_5_publickey.png" width="50%"/>
		</li>
	    </ul>
	  </td>
	</tr>
	<tr>
	  <td colspan="1">7</td>
	  <td colspan="1">
	    <p>Add application logic</p>
	    <ol>
	      <li>Firmware on the device side<ol>
	          <li>Download XDK workbench [<a href="https://xdk.bosch-connectivity.com/software-downloads">https://xdk.bosch-connectivity.com/software-downloads</a>]</li>
	          <li>Open your XDK workbench and import the SensorsToBle project [<ac:link>
	              <a href="./tutorials/examples/SensorsToBle.zip">XDK Firmware</a></li>
	          <li>Flash the SensorsToBle project to the XDK (Please consult the XDK manual for doing this.</li>
	        </ol>
	      </li>
	      <li>On the XDK Kura bundle<ol>
	          <li>
	            <p>In <em>ThingClientFactory.java</em>, add your network proxy if you need one and uncomment the line below:</p>
	            <pre><code>.proxyConfiguration(proxy)</code></pre>              </li>
	          <li>
	            <p>In XDKDevice.java, in the method getResourceId(), modify the method to how you intend to generate the ThingID of your XDK Thing. Make sure this aligns with the ThingID of the Thing you precommissioned in Chapter 2 of this guide.</p>
	            <pre><code>return "demo.vorto.example:" + getBluetoothDevice().getAdress().replace(":", ""); </code></pre>                <p>In our particular implementation, we will get the bluetooth address, strip the colons (":"), and prepend it with "xdk:"</p>
	          </li>
	          <li>
	            <p>In XDKDevice.java, in the method enableTemperature(), change the entire method to </p>
	            <pre><code>this.bluetoothGatt.writeCharacteristicValue("0x0013", "0100");</code></pre>
	            <p>This will turn-ON notifications for the XDK Bluetooth LE.</p>
	          </li>
	          <li>
	            <p>In XDKDevice.java, in the method readTemperature(), change the entire method to </p>
	            <pre><code>TemperatureSensor temperature = new TemperatureSensor();
try {
	this.bluetoothGatt.writeCharacteristicValue("0x0010", "74656D70");
	String btReturnValue = this.bluetoothGatt.readCharacteristicValue("0x0012");
	String[] btReturnValues = btReturnValue.split("\\s+");
	StringBuilder number = new StringBuilder();
	for(String hex : btReturnValues) {
		number.append((char) Integer.parseInt(hex, 16));
	}
	
	temperature.setSensorValue((float) (Float.parseFloat(number.toString())/1000.0));
	temperature.setUnits("Celsius");
} catch (KuraException e) {
	logger.error("Error in Sending Temperature", e);
}
return temperature;</code></pre>
                <p>Here we are writing the ascii representation of the string "temp" to the XDK register to get a reading of the temperature which we then read back and parse.</p>
                <p> </p>
              </li>
            </ol>
          </li>
        </ol>
      </td>
    </tr>
    <tr>
      <td colspan="1">8</td>
      <td colspan="1">
        <p>Deploy the XDK Kura bundle to the Kura gateway created on Step 1.</p>
        <ol>
          <li>Connect to Kura gateway wifi (created on Step 1) or if your gateway has a second network connection, make sure to be on the same network.</li>
          <li>Open mToolkit view (<em>Windows &gt; Show View &gt; Other &gt; mToolkit &gt; Frameworks</em>)</li>
          <li>Add Framework <ol>
              <li>Click on Add Framework icon on top right</li>
              <li>Put IP of running Kura Gateway on IP Address</li>
              <li>Make sure running Kura Gateway's port 1450 is open (configure this on Kura gateway app)</li>
            </ol>
          </li>
          <li>Right click on created framework and click Connect Framework</li>
          <li>Right click on Kura bundle (com.example.kura), go to "Install To", and click on the Framework you added.</li>
          <li>Verify that your plugin was installed by looking for it under Bundles</li>
          <li>Alternatively, you can verify by looking for it in the Kura web app (Device &gt; Bundles)</li>
        </ol>
      </td>
    </tr>
    <tr>
      <td colspan="1">9</td>
      <td colspan="1">
        <p>Change the configuration of our XDK Kura bundle</p>
        <ol>
    	<li>Go to the Kura webapp and look for the configuration page of our Bundle <br/> <img src="./images/connect_xdk_kura/step5_9.png"/></li>
    	<li>Configure the Bosch IoT Things Solution Id<br/>
		<p>You can lookup the solution Id in the Bosch IoT Things Admin Dashboard. Check the email that has been sent to you during the Evaluation Account Registration.</p>
		<img src="./images/connect_xdk_kura/step5_9_2.png"/>
		</li>
        <li>Enable scanning <br/> <img src="./images/connect_xdk_kura/step5_9_3.png"/></li>
        <li>Enable temperature <br/> <img src="./images/connect_xdk_kura/step5_9_4.png"/></li>
        </ol>
      </td>
    </tr>
    <tr>
      <td colspan="1">10</td>
      <td colspan="1">
        <p>Verify changes in Bosch IoT Suite</p>
        <ul>
          <li>Via the <a href="http://Bosch IoT Developer Console">Bosch IoT Developer Console</a>
          </li>
          <li>Via curl 
		  <pre><code>curl -X GET https://things.apps.bosch-iot-cloud.com/api/1/things?ids=ADD_THINGID_HERE 
-H "Authorization: Basic credentials" 
-H "Accept: application/json" 
-H "x-cr-api-token: token"</code></pre>	
		  </li>
        </ul>
      </td>
    </tr>
  </tbody>
</table>

# What's next ?

- [Create a web application consuming the device telemetry data](tutorial_create_webapp_dashboard.md)
- [Build an Amazon Alexa Skillset to voice-control the device](tutorial_build_alexaskill.md)