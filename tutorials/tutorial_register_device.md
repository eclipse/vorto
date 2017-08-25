# Register a device in Bosch IoT Suite


### Prerequisite

- An information model published to the Vorto Repository. [Read more](tutorial-create_and_publish_with_web_editor.md)
- Evaluation account for the Bosch IoT Suite. [Request account here](https://bosch-si.secure.force.com/content/FormDisplayPage?f=2abiE).

<table>
<tbody>
<tr>
  <td>1</td>
  <td>Open the <a href="https://console.bosch-iot-suite.com">Bosch IoT Developer Console</a>
  </td>
</tr>
<tr>
  <td colspan="1">2</td>
  <td colspan="1">Log in to the console with your evaluation tenant, username and password.</td>
</tr>
<tr>
  <td>3</td>
  <td>
    <p>At the moment, your evaluation account does not have any registered devices yet.</p>
    <p>Let's go and register your XDK by clicking <em>+ Connect thing</em>
    </p>
    <p>
      <img src="./images/connect_xdk_kura/step2_connect.png" width="50%" height="50%"/>
    </p>
  </td>
</tr>
<tr>
  <td colspan="1">4.</td>
  <td colspan="1">
    <p>Search the Vorto repository for <em>xdk</em> and select it in the result table and confirm with <em>Next</em>.</p>
    <p>
      <img src="./images/connect_xdk_kura/step2_connect_vorto.png" width="50%" height="50%"/>
    </p>
  </td>
</tr>
<tr>
  <td colspan="1">5.</td>
  <td colspan="1">Choose <strong>HTTP</strong> as a connector and confirm with <em>Next</em>.</td>
</tr>
<tr>
  <td colspan="1">6.</td>
  <td colspan="1">
    <p>Now, you can give your XDK a specific name in order to identify uniquely. For example</p>
    <p><b>Namespace</b>: demo.vorto.example</p>
    <p>As for the <b>technical device ID</b>, use the bluetooth mac address which you can find on the backside of the XDK. Make sure to remove the dashes! Example: <i>FCD6BD100B88</i></p>
	<p>Your suggested thing ID should look similar to this: <i>demo.vorto.example:FCD6BD100B88<i></p>
    <p>Confirm with <em>Complete.</em>
    </p>
  </td>
</tr>
<tr>
  <td colspan="1">7.</td>
  <td colspan="1">
    <p>Now you have successfully registered your XDK with the Suite for a given XDK information model.</p>
    <img src="./images/connect_xdk_kura/step2_7.png" width="70%" height="50%"/>
  </td>
</tr>
</tbody>
</table>