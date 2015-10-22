---
layout: documentation
title: Defining an Information Model Mapping
---
{% include base.html %}
## Defining an Information Model Mapping

Vorto allows the user to define mapping rules to map information models to target platforms.

**Prerequisites**

 - You have created an information model (refer to [Creating a new Information Model]({{base}}/documentation/editors/information-model.html#creating-a-new-information-model)).
 - You have selected the Java perspective.

**Proceed as follows**

1. In the information model project you want to create the mapping for (`PhilipsHue` in the example), navigate to the folder `src/models`.
2. Select the folder `models` and right-click and choose **New > File** from the context menu.  
   The **New File** dialog opens.
3. Enter a file name describing your actual platform with the suffix `.mapping`, e.g., `smarthome.mapping`.
4. Edit the mapping file according to your needs.

**Example of mapping file**

    mapping {
      model com.philips.PhilipsHue
      target smarthome
      from com.mycompany.fb.ColorLight.operation.setR,
           com.mycompany.fb.ColorLight.operation.setG,
           com.mycompany.fb.ColorLight.operation.setB
      to channelType with {name:"color"}
      from com.mycompany.fb.ColorLight.configuration.brightnessLevel
      to channelType with {name: "brightness"},
         configDescription with {name: "brightness", type: "Number"}
    }
