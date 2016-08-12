---
layout: documentation
title: Using the Vorto Repository Web Interface
---

{% include base.html %}

## Using the Vorto Repository Web Interface

**Proceed as follows**

You can use the features of Vorto without installing anything by using the Vorto Repository Web interface (refer to [Vorto Repository Web Interface]({{base}}/documentation/vorto-repository/web-interface/login-register.html)).

1. In your browser, open the address [`http://vorto.eclipse.org`](http://vorto.eclipse.org).  
   The Vorto Repository Web interface opens with the **Search** page.  
2. In the **Search** entry field, enter `TI_SensorTag` to find the **TI_SensorTag** information model.  
3. Click **Search**.  
   The model table is updated and the displays, maybe among others, the **TI_SensorTag** information model. 
   ![Search](/img/tutorials/vortoin5minutes/search.png)
4. Click the **TI_SensorTag** line.  
   The information page of the model opens with the active **Model** tab.  
   On the right hand side the **Platform Generators** available for this information model are displayed.
5. Click on the **Web UI generator**.  
   ![Web UI Generator](/img/tutorials/vortoin5minutes/generator.png)  
   The necessary files are generated in the Vorto Repository backend. After completion, the download of a zip file containing all generated files is initiated and the operating system's file save dialog for the files to be downloaded opens.  
   ![Generator Output](/img/tutorials/vortoin5minutes/generator-output.png)
6. Save the file in a folder of your choice.
7. Unpack the zip file.
8. Start Eclipse and switch to the Java perspective.
9. Import the unpacked project into Eclipse.  
   a) Click **File > Import...**, select **Existing Maven Projects** and click **Next**.  
   b) In the **Maven Projects** dialog, enter the unpacked directory in the **Root Directory** entry field or search for it by clicking the **Browse** button.
   c) Click the **Finish** button.
   d) In the **Setup Maven plugin connectors** dialog, click the **Finish** button.
10. Right-click the project **xyz** and select **Run as... > xyz** from the context menu.

Voila! You just created an AngularJS-based web application with REST controllers for the information model.
