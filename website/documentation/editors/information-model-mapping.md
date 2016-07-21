---
layout: documentation
title: Defining an Information Model Mapping
---
{% include base.html %}

## Defining an Information Model Mapping

In an information model mapping, you can define mapping rules for information model attributes, as well as detailed function block properties for each function block variable, and data types used in function blocks.

**Proceed as follows**

1. In the **Vorto Model Project Browser**, select the project in the **Select Vorto Project** drop-down list.  
   ![Select a Project]({{base}}/img/documentation/vorto_select_vorto_project.png)  
2. Right click on the information model you want to create a mapping for and choose **New Mapping Model**  
   The **Create Mapping Model** dialog opens.  
   ![Create Mapping Model Dialog]({{base}}/img//documentation/vorto_create_mapping_model_dialog.png)
3. Enter a name as **Mapping Model Name**, for example, `MyLightingDevice_MyIOTPlatform`.  
4. Adjust the entries for the input fields **Namespace**, **Platform** and **Version**, if necessary.
5. Optionally, enter a description in the **Description** entry field.
6. Click **Finish**.  
7. Switch to the Java perspective.
   The new mapping file has been created (with suffix `.mapping`). It is located in the folder `<project>/mappings`.  
   ![Generated source]({{base}}/img/documentation/vorto_create_information_model_mapping_model_source.png)
8. Edit the mapping file according to your needs.

**Example of an information model mapping file**

	namespace com.mycompany
	version 1.0.0
	displayname "MyLightingDevice_MyIOTPlatform"
	description "MyLightingDevice to MyIOTPlatform mapping"
	using com.mycompany.MyLightingDevice ; 1.0.0
	using com.mycompany.ColorLight_MyIOTPlatform ; 1.0.0
	
	infomodelmapping MyLightingDevice_MyIOTPlatform {
		targetplatform MyIOTPlatform
	
		from MyLightingDevice.displayname
		to TargetDisplayName
	
		from MyLightingDevice.switchable
		to MySwitch with { Icon : "switch.png" }
	
		from MyLightingDevice.colorlight
		to reference ColorLight_MyIOTPlatform
	}
