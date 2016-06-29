---
layout: documentation
title: Searching Models
---
{% include base.html %}

## Searching Models Using the Eclipse Vorto Toolset

This section details the following topics:

[Searching Models](#searching-models)

[Previewing a Model](#previewing-a-model)

[Importing a Model from the Vorto Repository](#importing-a-model-from-the-vorto-repository)

### Searching Models

You can search for an existing model in the repository and use this shared model as reference in a local model project.


**Prerequisites**

* You have set the required preferences for accessing the Vorto Repository out of Eclipse (refer to [Setting Required Preferences](./setting-preferences-eclipse#Setting Required Preferences)).
* You have started Eclipse.
* You have selected the Vorto perspective with the Model Repository view.

**Proceed as follows**

1. In entry field of the Model Repository view, enter a search string to find the item you want, e.g., `temperature` to find models relating temperature.
2. Click the **Search** button.  
   The model table is updated according to the filter criteria and displays a subset of all models now.  
   ![Searching the Repository in Eclipse]({{base}}/img/documentation/vorto_repository_search_eclipse.png)  

<table class="table table-bordered">
  <tbody>
    <tr>
      <td><i class="fa fa-info-circle info-note"></i></td>
      <td>If you leave the entry field empty, you will get all models available in the Vorto Repository.</td>
    </tr>
  </tbody>
</table>


###Previewing a Model

**Prerequisites**

The model table displayes the model(s) you want to preview (refer to [Searching Models](#searching-models)).

**Proceed as follows**

Right-click the model you want to preview and select **Preview model**.  
![Preview a Model]({{base}}/img/documentation/vorto_repository_eclipse_preview_model.png)  
An editor tab opens with the downloaded model definition.  
![Preview a Model in the Editor]({{base}}/img/documentation/vorto_repository_eclipse_preview_model_editor.png)  

###Importing a Model from the Vorto Repository

**Prerequisites**

The model table displayes the model you want to import (refer to [Searching Models](#searching-models)).

**Proceed as follows**

Drag and drop the model you want to import from the model table in the Model Repository view into the tab of the appropriate model type on the left side.  
In the example below, the data type model `TemperatureUnit` has been imported into the **Datatypes** tab and the function block model `TemperatureSensor` has been imported into the **Functionblocks** tab.  
![Import a Model]({{base}}/img/documentation/vorto_repository_eclipse_import_model.png)  