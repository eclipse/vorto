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
8. ***TODO***: Run the code
9. ***TODO***: Show the Web UI App in browser

Congratulations! You have just created a web application with the least effort with the magic of Vorto.

**Next: Why not try defining your own information models? :D**