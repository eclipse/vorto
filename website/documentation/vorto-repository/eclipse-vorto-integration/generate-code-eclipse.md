---
layout: documentation
title: Generating Code
---
{% include base.html %}

##Generating Code

This section details the following topics:

[Generating Code from the Vorto Repository](#generating-code-from-the-vorto-repository)

[Generating Code from Your Local Workspace](#generating-code-from-your-local-workspace)

###Generating Code from the Vorto Repository

You can generate code from shared information models in the Vorto Repository. The code generation is carried out in the Vorto Repository backend. You can choose a desired generator from a list of available generators provided in the Vorto Repository.

**Prerequisites**  

* The information model you want to generate code from exists in the Vorto Repository.
* You have started Eclipse.
* You have selected the Vorto perspective with the Model Repository view.

**Proceed as follows**

1. In the Model Repository view, search for the information model you want to generate the code from by entering an appropriate search string in the **Search** entry field and clicking the **Search** button.
2. Select the information model you want to generate code from, right-click and choose **Generate Code** from the context menu.  
   ![Generate Code Eclipse]({{base}}/img/documentation/vorto_repository_generate_code_eclipse.png)  
   The **Generator Overview** dialog opens.  
   ![Generate Code Eclipse Dialog]({{base}}/img/documentation/vorto_repository_generate_code_eclipse_dialog.png)  
3. Choose the desired code generator for generating the souce code and click the corresponding **Generate** button.  
   The code is generated.
4. Select the Java perspective to check the generated code.

###Generating Code from Your Local Workspace

You can generate code from information models in your local workspace. The code generation is carried out locally on your computer. You can choose one of the code generators which have been installed along with the Eclipse Vorto Toolset.

**Prerequisites**  

* The information model you want to generate code from exists in your local workspace.
* You have started Eclipse.
* You have selected the Vorto perspective.

**Proceed as follows**

1. In the **Information Models** browser, select the information model you want to generate code from.
2. Right-click to open the context menu click **Generate Code**, Choose a code generator and click it.  
   ![Eclipse Generate Code]({{base}}/img/documentation/vorto_eclipse_generate_code.png)  
   Depending on the code generator several files are generated. Eclipse switches to the Java perspective where you can check the generated files.