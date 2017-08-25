# Create a device-specific web application 

In this tutorial, we want to build a small Spring-boot based IoT webapp that is able to consume device values, e.g. XDK, from a IoT Cloud backend and display the data in a dashboard. In this example, we will use the <a href="http://vorto.eclipse.org/#/details/com.bosch.devices/XDK/1.0.0"> XDK Information Model</a> and create a web application specific to the XDK functionality.

### Prerequisite

- An information model published to the Vorto Repository. [Read more](tutorial-create_and_publish_with_web_editor.md) 
- Evaluation account for the Bosch IoT Suite. [Read more](tutorial_register_device.md) 
- You have successfully registered your XDK in the Bosch IoT Suite. [Read more](tutorial_register_device.md) 

### Steps

<table>
  <tbody>
    <tr>
      <td>1.</td>
      <td>
        <p>Go to the <a href="http://vorto.eclipse.org/#/details/com.bosch.devices/XDK/1.0.0">XDK Information Model</a> and select 'Web-based Device Dashboard' from the list of generators on the right hand-side.</p>
        <p>
           <img src="./images/create_webapp_dashboard/step3_1.png"/>
        </p>
      </td>
    </tr>
    <tr>
      <td>2.</td>
      <td>
        <p>In the generator configuration page, select the <strong>Bosch IoT Suite</strong> as a cloud platform connector. Feel free to select other add-ons as needed:</p>
        <p>
          <img src="./images/create_webapp_dashboard/step3_2.png" width="50%"/>
        </p>
        <p>By clicking <strong>Generate</strong>, the generator generates a ZIP achieve containing a maven project of the XDK dashboard spring-boot application.</p>
      </td>
    </tr>
    <tr>
      <td>3.</td>
      <td>
        <strong>Unzip </strong>and <strong>import </strong>the project as a Maven Project into your Eclipse IDE.</td>
    </tr>
    <tr>
      <td>4.</td>
      <td>
        <p>Download <a href="https://github.com/bsinno/iot-things-examples/blob/master/cr-integration-api-examples/common/src/main/resources/bosch-iot-cloud.jks">https://github.com/bsinno/iot-things-examples/blob/master/cr-integration-api-examples/common/src/main/resources/bosch-iot-cloud.jks</a>
        </p>
        <p>and store this file under <em>src/main/resources/secure/bosch-iot-cloud.jks</em>
        </p>
      </td>
    </tr>
    <tr>
      <td>5.</td>
      <td>
        <p>Create a <a href="https://things.apps.bosch-iot-cloud.com/dokuwiki/doku.php?id=002_getting_started:booking:manage-solution-ui#private_and_public_key">public and private key for your solution</a> and store the CRClient.jks private key under <em>src/main/resource/secure/CRClient.jks</em></p>
		<ul>
		<li>
		Create a public and private key pair for your solution:
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
		<img src="./images/create_webapp_dashboard/step3_5_publickey.png" width="50%"/>
		</li>
		</ul>
      </td>
    </tr>
    <tr>
      <td colspan="1">6.</td>
      <td colspan="1">
        <p>Open the <em>src/main/resources/application.yml </em>and insert the Bosch IoT Permissions and Bosch IoT Things credentials that you have received via Email during the evaluation account registration:</p>
        <pre><code>spring:
  jackson:
    serialization:
      write-dates-as-timestamps: false
bosch:
  permissions:
    endpointUrl: https://permissions-api.apps.bosch-iot-cloud.com
    clientId: [enter Bosch IoT Permissions client id here]
    clientSecret: [enter Bosch IoT Permissions secret here]
  things:
    alias: CR
    alias.password: [enter keystore password]
    endpointUrl : https://things.apps.bosch-iot-cloud.com
    wsEndpointUrl : wss://events.apps.bosch-iot-cloud.com
    apiToken: [enter Bosch IoT Things API Token here ]
    keystoreLocation : /secure/CRClient.jks
    trustStoreLocation : /secure/bosch-iot-cloud.jks
    trustStorePassword : jks
    solutionid: [enter Bosch IoT Things solution ID here ]
    keystore:
      password: [enter keystore password]]]>
    </code></pre>
    <p><b>Behind proxy ?</b></p>
    <p>1) Add proxy information in the application.yml</p>
    <p>2) Open java class com.example.iot.xdk.config.LocalConfiguration and uncomment the proxy authentication configuration.</p>
      </td>
    </tr>
    <tr>
      <td colspan="1">7.</td>
      <td colspan="1">
        <p>Open the <em>com.example.iot.xdk.config.LocalConfiguration</em> and use the clientID <em>&lt;solutionID&gt; + ":xdkapp. </em>
        </p>
            <p>Make sure the <strong>clientID</strong> in your configuration matches the clientID in the ACL that you had created via the Developer Console earlier!</p>
      </td>
    </tr>
    <tr>
      <td colspan="1">8.</td>
      <td colspan="1">
        <p>Open your browser under <a href="http://localhost:8080">http://localhost:8080</a>
        </p>
        <p>
          <img src="./images/connect_xdk_kura/step3_8.png" width=50%"/>
        </p>
      </td>
    </tr>
    <tr>
      <td colspan="1">8.</td>
      <td colspan="1">
        <p>Login with your Bosch IoT Permissions credentials, e.g:</p>
        <p>Username: <em>tenant\username</em>
        </p>
        <p>Password: <em>password</em>
        </p>
      </td>
    </tr>
    <tr>
      <td colspan="1">9.</td>
      <td colspan="1">
        <p>After successful authentication, you can see the XDK device, that you had registered earlier (see chapter " Register / Pre-commission device in Bosch IoT Suite")</p>
        <p>
           <img src="./images/connect_xdk_kura/step3_9.png" width=50%"/>
        </p>
      </td>
    </tr>
    <tr>
      <td>10.</td>
      <td>
        <p>Click on the XDK device to see the details containing UI widgets for the individual function blocks</p>
        <p>
           <img src="./images/create_webapp_dashboard/step3_10.png" width=50%"/>
        </p>
        <p> </p>
      </td>
    </tr>
    <tr>
      <td>11.</td>
      <td>
		Verify and send data to the Bosch IoT Suite that gets displayed in your web application:
		<ul>
			<li>Simulate the device. <a href="tutorial_simulate_device.md">Read the tutorial</a></li>
			<li>Via curl, e.g.
			<pre><code>curl -X PUT 
https://things.apps.bosch-iot-cloud.com/api/1/things/ADD_THING_ID_HERE/features/temperature
-H 'authorization: Basic  ADD_CREDENTIALS_HERE' \
-H "Accept: application/json" \
-H 'x-cr-api-token: ADD_THINGS_API_TOKEN_HERE' \
-d '{"properties": {"sensor_value":22.2}}'</code></pre>
			</li>
		</ul>
      </td>
    </tr>
  </tbody>
</table>

### What's next ?

- [Simulate the device and send test data](tutorial_simulate_device.md)