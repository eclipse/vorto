---
layout: documentation
title: Defining a Model Mapping
---
{% include base.html %}
# Defining a Model Mapping

Vorto allows the user to define mapping rules to map Vorto models to other target platform domain models.


**Prerequisites**

 - You have followed previous steps to create Entity, Function Block, and Information models.
 - You have selected the Java perspective.

## Define a Entity Mapping
In entity mapping, you can define mapping rules for entity types. 
**Proceed as follows**
			
1. Select data type project **Color**.

2. Right click and choose **Vorto->Create new Mapping Model**
   The **Create Mapping Model** dialog opens.

3. Enter a name for your target platform, e.g., **MyIOTPlatform**, and click **Finish**, a new mapping file will be created under folder **src/mappings/**

4. Edit the mapping file according to your needs.

**Example of an entity mapping file**

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
-----

## Define a Function Block Mapping
In function block model mapping, you can define mapping rules for function block model attributes, as well as detailed function block properties, and data types used in function blocks.

**Proceed as follows**

In function block mapping, you can define mapping rules for function blocks. 
**Proceed as follows**
			
1. Select data type project **ColorLight**.

2. Right click and choose **Vorto->Create new Mapping Model**
   The **Create Mapping Model** dialog opens.

3. Enter a name for your target platform, e.g., **MyIOTPlatform**, and click **Finish**, a new mapping file will be created under folder **src/mappings/**

4. Edit the mapping file according to your needs.

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

-----

## Define an Information Model Mapping
In information model mapping, you can define mapping rules for information model attributes, as well as detailed function block properties for each function block variable, and data types used in function blocks.

**Proceed as follows**

1. Select data type project **MyLightingDevice**.

2. Right click and choose **Vorto->Create new Mapping Model**
   The **Create Mapping Model** dialog opens.

3. Enter a name for your target platform, e.g., **MyIOTPlatform**, and click **Finish**, a new mapping file will be created under folder **src/mappings/**

4. Edit the mapping file according to your needs.


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

-----