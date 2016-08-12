---
layout: documentation
title: Using the Eclipse Vorto Toolset
---

{% include base.html %}


## Using the Eclipse Vorto Toolset

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
4. Create a new Vorto Project by clicking on the **+** button.  
   ![New Vorto Project](/img/tutorials/vortoin5minutes/new-project.png)
   The project is created and selected immediately.
5. In the **Model Repository** tab on the bottom right of the Vorto perspective, search for the **TI_SensorTag** information model.  
   ![Model Repository](/img/tutorials/vortoin5minutes/repo.png)
6. Drag the **TI_SensorTag** information model from the **Model Repository** tab and drop it into the **Information Models** window.  
   ![Drag and Drop](/img/tutorials/vortoin5minutes/drag-drop.png)  
   After the **TI_SensorTag** information model has been downloaded, it is displayed in the **Information Models** area of the **Vorto Model Project Browser** tab.
7. Right-click the **TI_SensorTag** information model and select **Generate Code > Web UI Generator** from the context menu.  
   ![Generate](/img/tutorials/vortoin5minutes/generate.png)  
   The generator generates the files.
8. Switch to the Java perspective to explore the generated files.

Congratulations! You have just created a web application with the least effort with the magic of Vorto.

**Next: Why not try defining your own information models? :D**