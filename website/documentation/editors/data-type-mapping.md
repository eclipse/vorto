---
layout: documentation
title: Defining a Data Type Mapping
---
{% include base.html %}

## Defining a Data Type Mapping

With a data type mapping you can define mapping rules for data types.

**Prerequisites**

 - You have crated a data type (refer to [Defining Data Types]({{base}}/documentation/editors/datatype.html)).
 - You have selected the Vorto perspective.

**Proceed as follows**

1. In the **Vorto Model Project Browser**, select the project in the **Select Vorto Project** drop-down list.  
   ![Select a Project]({{base}}/img/documentation/vorto_select_vorto_project.png)  
2. Right click on the data type you want to create a mapping for and choose **New Mapping Model**  
   The **Create Mapping Model** dialog opens.  
   ![Create Mapping Model Dialog]({{base}}/img//documentation/vorto_create_mapping_model_dialog.png)
3. Enter a name as **Mapping Model Name**, for example, `MyIOTPlatform`.  
4. Adjust the entries for the input fields **Namespace**, **Platform** and **Version**, if necessary.
5. Optionally, enter a description in the **Description** entry field.
6. Click **Finish**.  
7. Switch to the Java perspective.
   The new mapping file has been created (with suffix `.mapping`). It is located in the folder `<project>/mappings`.  
   ![Generated source]({{base}}/img/documentation/vorto_create_data_mapping_model_source.png)
8. Edit the mapping file according to your needs.

**Example of a data type mapping file**

	namespace com.mycompany
	version 1.0.0
	displayname "Color_MyIOTPlatform"
	description "Color to MyIOTPlatform mapping"
	using com.mycompany.type.Color ; 1.0.0
	
	entitymapping Color_MyIOTPlatform {
		targetplatform MyIOTPlatform
	
		from Color.version
		to MyColor with { Revision : "Rev-" }
	
		from Color.r, Color.g, Color.b
		to MyColor with { r : "Red", g : "Green", b : "Blue" }
	}
