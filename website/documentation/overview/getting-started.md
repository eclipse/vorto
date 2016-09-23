---
layout: documentation
title: Getting Started
---

{% include base.html %}

## Getting Started

This tutorial will show you how to start with **Vorto** with the least effort required.

In this tutorial, you will create a Web user interface for an information model.

**Prerequisites**

- You have installed Eclipse (refer to [System Requirements]({{base}}/documentation/overview/system-requirements.html)).
- You have installed the Vorto Toolset (refer to [Installing the Vorto Toolset]({{base}}/documentation/installation/installation.html)).
- You have started Eclipse.

1. Start Eclipse.
2. In the main menu, click **Window > Perspective > Open Perspective > Other**.  
   The **Open Perspective** dialog opens.
3. Select **Vorto** and click **OK**.
   The Vorto Perspective opens.  
   ![Vorto Perspective](/img/tutorials/vortoin5minutes/open-perspective.png)
4. In the Model Repository view, search for the information model **TI_SensorTag** (the information model you want to generate the code from) by entering the string `TI_SensorTag` in the **Search** entry field and clicking the **Search** button.
5. Select the information model **TI_SensorTag**, right-click and choose **Generate code** from the context menu.  
   ![Generate Code Eclipse]({{base}}/img/documentation/vorto_repository_generate_code_eclipse.png)  
   The **Generator Overview** dialog opens.  
   ![Generate Code Eclipse Dialog]({{base}}/img/documentation/vorto_repository_generate_code_eclipse_dialog.png)
6. Choose the **Web UI** code generator for generating the souce code for the Web user interface and click the **Generate** button.  
   The code is generated.
7. Select the Java perspective to check the generated code.  
   The following Java projects have been generated:  
   * `java.example.model` (Java beans for the selected information model)
   * `webdevice.example` (Web UI for the selected information model)  

   ![Generated Projects](/img/tutorials/vortoin5minutes/generated-projects.png)
8. Select both projects, right-click and choose **Maven > Update Project...** from the context menu.
   ![Update Maven Projects Dialog](/img/tutorials/vortoin5minutes/update-maven-projects-dialog.png)
9. Click **OK**.  
   Maven updates the projects.
10. Select the project `webdevice.example`, right-click  and choose **Run As > Java Application** from the context menu.
11. In the **Select type** entry field, enter `Application` to filter for applications.  
   ![Select Java Application Dialog](/img/tutorials/vortoin5minutes/select-java-application-dialog.png)
12. Select the `Application - webdevice.example` line and click **OK**.  
   The Web application starts.
13. In the address bar of your browser, enter localhost:8080/webdevice.  
   The Web UI opens.
   ![Web UI](/img/tutorials/vortoin5minutes/web-ui.png){:width="900px"}


Congratulations! You have just created a Web application with the least effort with the magic of Vorto.

**Next: Why not try defining your own information models? :D**