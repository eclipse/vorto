---
layout: documentation
title: Code Generators
---
{% include base.html %}

## Example Code Generator - Web Device Application

This section describes the use of an example code generator to generate a Web Device Application.

**Prerequisites**

- You have created the information model project `MyLightingDevice` (refer to [Creating a new Information Model]({{base}}/documentation/editors/information-model.html#creating-a-new-information-model) and [Editing an Information Model]({{base}}/documentation/editors/information-model.html#editing-an-information-model)).
- You have selected the Vorto perspective.
- Eclipse [M2E plug-in](http://download.eclipse.org/technology/m2e/releases) 1.5.\* (can already be packaged with Eclipse Luna)  

<table class="table table-bordered">
	<tbody>
 <tr>
   <td><i class="fa fa-info-circle info-note"></i></td>
    <td>In order to use a repository you have to adjust the maven <code>settings.xml</code> file by adding a new profile.</td>
  </tr></tbody>
</table>

<table class="table table-bordered">
	<tbody>
 <tr>
   <td><i class="fa fa-info-circle info-note"></i></td>
    <td>In the Eclipse preferences (<b>Maven > User Settings > User Settings</b>), add the absolute path to the maven <code>settings.xml</code> file.</td>
  </tr></tbody>
</table>

**Proceed as follows**

Select the information model project created (`MyLightingDevice`), right-click and choose **Generate Code > Web Device Application Generator** from the context menu.

The Web application project `mylightingdevice-webapp` is generated and Eclipse switches to the Java perspective.

![Generated Web App]({{base}}/img/documentation/m2m_vorto_mylightingdevice_webapp_java_layout.png)

<table class="table table-bordered">
	<tbody>
 <tr>
   <td><i class="fa fa-info-circle info-note"></i></td>
    <td>There might be some errors on the generated project. You can select the project, then right-click to open the context menu and click <b>Maven-Update Project...</b> to resolve the errors.</td>
  </tr></tbody>
</table>

The following items are generated:

- POJO Java classes corresponding to the function block model created (with package `com.bosch.iot.<functionblock>.model`). For the purpose of simplicity, only primitive types are generated.
- One service class that support REST operation (e.g., retrieve of device information or perform operations of the device with package `com.bosch.iot.<functionblock>.service`)
- An XML file that allows the application to run as a web application.  
   `Web.xml`
- An html file that provides visualization of a device.  
   `Index.html`
- A POM file that allows the package to run the application as web application. Additionally it also contains configurations for launching the application from Eclipse using jetty server.  
   `pom.xml`

### Running the Generated Web Device Application

Run the generated Web device application to visualize it.

**Prerequisites**  

- You have create a Web device application (refer to [Example Code Generator - Web Device Application](#example-code-generator-web-device-application)).
- You have selected the ava perspective.

**Proceed as follows**

1. In the Package Explorer, select the Web device application project (`mylightingdevice-webapp` in the example). From the context menu, choose **Run As > Run Configurations...**.  
   The **Run Configurations** dialog opens.
2. In the configuration list on the left side, expand **Maven Build** and click the sub item **New_configuration**.  
   A configuration form opens.
3. Change the entry in the **Name** field to, e.g., `mylightingdevice_configuration`.
4. In the **Base directory** input field, enter `${workspace_loc}/mylightingdevice-webapp`.
5. In the **Goals** input field, enter `jetty:run`.
6. Click **Run**.  
   ![Run Jetty]({{base}}/img/documentation/m2m_vorto_jetty_run.png)  
   Wait a few seconds for jetty server to start. Upon successfully start, message `[INFO] Started Jetty Server` should be displayed in the Eclipse console.
7. Open the URL `http://localhost:8080/mylightingdevice-webapp/index.html` in your browser to see the HTML representation of your device over Web.  
   ![Mylightingdevice Web UI]({{base}}/img/documentation/m2m_vorto_mylightingdevice_webui.png)

### Modifying the Behavior of the Generated Web Device Application

The default REST service class `Switchable` generated provides a service to return default properties of the lamp instance. It also provides a service to handle an operation request. However it only contains dummy log statements. If you click the **On** button the status of Lamp is still shown as **false**. The service does not update the status of the lamp instance.

**Prerequisites**  

- You have created an information model mapping with target platform set to "smarthome" (refer to [Editing an Information Model]({{base}}/documentation/editors/information-model.html#editing-an-information-model)).
- Eclipse smart home generator is provided to generate eclipse thing types based on Vorto information model.

**Proceed as follows**

1. Update the service method **on()** of class `Switchable` (file `SwitchableService.java`) to update the device instance status.

        @PUT
        @Path("/on")  
        public void on() {  
            switchableinstance.getStatus().setOn(true);  
        }

2. Stop the application by clicking the red square beside the Console tab in your IDE.
3. Start the application again by selecting the project and choosing **Run As > Run configurations** from the context menu.
4. In your browser, open the URL `http://localhost:8080/mylightingdevice-webapp/index.html` again.
5. Click the **On** button.  
The **On** status is changed to **true**.
