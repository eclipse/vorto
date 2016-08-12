---
layout: documentation
title: Defining a Function Block Mapping
---
{% include base.html %}

## Defining a Function Block Mapping

In a function block mapping, you can define mapping rules for function block attributes, as well as detailed function block properties and data types used in function blocks.

**Prerequisites**

 - You have crated a function block (refer to [Defining Function Blocks]({{base}}/documentation/editors/functionblock.html)).
 - You have selected the Vorto perspective.

**Proceed as follows**

1. In the **Vorto Model Project Browser**, select the project in the **Select Vorto Project** drop-down list.  
   ![Select a Project]({{base}}/img/documentation/vorto_select_vorto_project.png)  
2. Right click on the function block you want to create a mapping for and choose **New Mapping Model**  
   The **Create Mapping Model** dialog opens.  
   ![Create Mapping Model Dialog]({{base}}/img//documentation/vorto_create_mapping_model_dialog.png)
3. Enter a name as **Mapping Model Name**, for example, `ColorLight_MyIOTPlatform`.  
4. Adjust the entries for the input fields **Namespace**, **Platform** and **Version**, if necessary.
5. Optionally, enter a description in the **Description** entry field.
6. Click **Finish**.  
7. Switch to the Java perspective.
   The new mapping file has been created (with suffix `.mapping`). It is located in the folder `<project>/mappings`.  
   ![Generated source]({{base}}/img/documentation/vorto_create_functionblock_mapping_model_source.png)
8. Edit the mapping file according to your needs.

**Example of a function block mapping file**

	namespace com.mycompany
	version 1.0.0
	displayname "ColorLight_MyColorLight"
	description "ColorLight to MyColorLight mapping"
	using com.mycompany.fb.ColorLight ; 1.0.0
	using com.mycompany.Color_MyIOTPlatform ; 1.0.0
	
	functionblockmapping ColorLight_MyIOTPlatform {
		targetplatform MyIOTPlatform
	
		from ColorLight.displayname
		to TargetDisplayName
	
		from ColorLight.configuration.defaultColor
		to reference Color_MyIOTPlatform
	
		from ColorLight.operation.setR, ColorLight.operation.setG, ColorLight.operation.setB
		to channelType with { Attribute : "color" }
	}
