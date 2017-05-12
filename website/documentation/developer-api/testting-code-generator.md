---
layout: documentation
title: Testing the Code Generator
---
{% include base.html %}


### Testing the Code Generator

After packaging and deploying the plug-in in the eclipse plug-ins folder, the code can be triggered using the context menu.

**Prerequisites**

You have implemented the code for the code generator plug-in project (refer to [Implementing the Code](./implementing-code.html)).

**Proceed as follows**

1. In the **Package Explorer**, select the plug-in project (`HelloWorldGenerator` in the example).
2. From the context menu, choose **Run As > Eclipse Application**.  
   A new Eclipse instance is started.
3. Switch to the **Vorto** perspective.
4. Create a new Vorto Project.
5. Create a function block or an information model or download an existing model from vorto repository (e.g., `XDK`) and select it.  
   If necessary, you must do some settings for the new Eclipse environment (refer to [Setting Required Preferences]({{base}}/documentation/vorto-repository/eclipse-vorto-integration/setting-preferences-eclipse.html)).
6. From the context menu, choose **Generate Code > HelloWorldGenerator**.  
   ![testing demo generator]({{base}}/img/documentation/vorto_invoke_HelloWorldGenerator.png)  
   A new project with name `XDK_helloworldgenerator_generated` is generated.  
   ![testing demo generator]({{base}}/img/documentation/vorto_HelloWorldGenerator_result.png)
7. Open the file `sample.txt`, it shows "Hello world from information model XDK !".

This code generator is provided for you to understand code generator easier. There are already some predefined code generators included in our code base, please see the list [here](https://github.com/eclipse/vorto/blob/development/server/generators/Readme.md). 
