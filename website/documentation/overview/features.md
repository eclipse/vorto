---
layout: documentation
title: Features
---

{% include base.html %}

## Features

Vorto is characterized by the following features:

* With the IoT Eclipse **Vorto Toolset**, you can describe the characteristics and functionality of devices as Information Models, share them with others and generate code with the help of Code Generators.
  
  ![Vorto Features Models]({{base}}/img/documentation/vorto_features_models.png){:width="700px"}
  
* The **Vorto repository** manages your device descriptions (Information Models). Users are able to search device characteristics and device functionality
  
  ![Search page]({{base}}/img/documentation/vorto_repository_search.png){:width="700px"}
  
* Integrate devices into platforms easily with the help of platform specific **code generators**, for example Bosch, AWS, ThingWorx, etc. 
Many code generators illustrate the many possibilities of how code generators can be effectively used. They can also be taken as a code reference to implement your own generator with the Generator API. 
  
  ![Generators]({{base}}/img/documentation/vorto_repository_generators.png){:width="700px"}
  
* A **Command Line Interface Tool** lets you explore information models, share new information models as well as generate code directly on the Vorto Repository server
  
  ![Vorto CLI Tool]({{base}}/img/documentation/vorto_features_cli_tool.png)
  
* Vorto Code Generators convert an Information Model into executable code that is specific for the platform. Every generator translates the platform independent information model into a platform model and lastly source code. The **Vorto Code Generator API** lets you plug in your own code generator into the toolset.
