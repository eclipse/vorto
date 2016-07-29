---
layout: documentation
title: End user Tutorial
---

{% include base.html %}

## Vorto in 5 minutes

This tutorial will show you how to start with **Vorto** with the least effort required. 

In this tutorial, we will create a web user interface for an information model. We will show you two ways to approach this, one, through the web interface and the other, through the Eclipse toolset.

### Using the web interface

You can use the features of Vorto without installing anything by using the web interface. 

First, with your web browser, go to the Vorto repository on the following address: [http://vorto.eclipse.org/](http://vorto.eclipse.org/)

Then search for an information model. We will search for a **TI_SensorTag** information model.

![Search](/img/tutorials/vortoin5minutes/search.png)

Next we clik the **TI_SensorTag** on the table. This will show the details of the information model and on the right hand side we will see the **Platform Generators** available for this information model. Now click on the **Web UI generator**.

![Web UI Generator](/img/tutorials/vortoin5minutes/generator.png)

After the generator has finished, you should be able to download the generated files.

![Generator Output](/img/tutorials/vortoin5minutes/generator-output.png)

Voila! You just created an AngularJS-based web application with REST controllers for the information model.

### Using the Eclipse toolset

First, you need to install the Vorto Eclipse Toolset by following this instructions : [here]({{base}}/documentation/installation/installation.html)

Afterwards, fire up your Eclipse with the Vorto Toolset, and open the **Vorto Perspective**

![Vorto Perspective](/img/tutorials/vortoin5minutes/open-perspective.png)

Once you are in the Vorto Perspective, create a new Vorto Project by clicking on the **+** button.

![New Vorto Project](/img/tutorials/vortoin5minutes/new-project.png)

Now, you should have a new Vorto Project with the name that you chose. Next, open the **Model Repository**. It is located on the bottom right side.
Now search for the **TI_SensorTag** information model.

![Model Repository](/img/tutorials/vortoin5minutes/repo.png)

Now, drag the **TI_SensorTag** information model from the Model Repository window and drop it into the **Information Models** window.

![Drag and Drop](/img/tutorials/vortoin5minutes/drag-drop.png)

After the **TI_SensorTag** information model has been downloaded, it will show up in the **Information Models** window. Right click on it to bring
up the context menus and click on the **Web UI Generator**.

![Generate](/img/tutorials/vortoin5minutes/generate.png)

After the generator is finished, you should now be able to explore the generated project in the Java perspective.

Congratulations! You have just created a web application with the least effort with the magic of Vorto.

**Next: Why not try defining your own information models? :D**