---
layout: documentation
title: Defining an Information Model
---
{% include base.html %}
## Defining an Information Model

This section details the following topics:

[Information Models](#information-models)  

[Creating a New Information Model](#creating-a-new-information-model)  

[Editing an Information Model](#editing-an-information-model)  

## Information Models

Information models represent the capabilities of a particular type of device entirety. An information model contains one or more function blocks.

<div class="thumb1">
<a title="Defining a Information Model" data-rel="prettyPhoto" href="https://youtu.be/MJI74NuYTw4&width=1500&height=1000" rel="prettyPhoto" >
<img src="{{ $base}}/img/documentation/defineinfo.jpg"  class="box-img img-responsive zoom1">
<i class="fa fa-play-circle fa-5 play-icon"></i>
</a>
</div>

## Creating a New Information Model

**Prerequisites**  

You have create a function block model (refer to [Creating a New Function Block Project]({{base}}/documentation/editors/functionblock.html#creating-a-new-function-block)).

**Proceed as follows**

1. Right-click in the **Information Models** tab and choose **Create new Information Model** from the context menu:  
   ![Create new information model]({{base}}/img/documentation/m2m_tc_create_new_information_model.png)  
   The **Create Information Model** dialog opens:  
   ![Create information model dialog]({{base}}/img/documentation/m2m_tc_create_information_model_dialog.png)
2. Enter, e.g., `PhilipsHue` as **Information Model Name**.  
3. Click **Finish**.
   The information model DSL source file (with suffix .infomodel) is generated and displayed in the information model editor. The file contains a complete structure according the DSL syntax with the values given in the preceding step.  
   ![Information model DSL editor]({{base}}/img/documentation/m2m_tc_information_model_dsl_editor.png)

## Editing an Information Model

**Prerequisites**

You have created an information model (refer to [Creating a new Information Model](#creating-a-new-information-model)).

**Proceed as follows**

Edit the information model by extending the generated source file in the information model DSL editor.

**Example**

1. Create a new function block model named `Switchable` and update it according the following:

		namespace com.mycompany.fb
		version 1.0.0
		displayname "Switch"
		description "Function block model for Switch"
		category demo
		
		functionblock Switchable {
			status {
				optional on as boolean
			}
		
			operations {
				on()
				off()
				toggle()
			}	

		}


2. Create a new function block model named `ColorLight`, drag and drop the entity model **Color** from **Data Types** into this funciton block model,  and update it according the following:

	
		namespace com.mycompany.fb
		version 1.0.0
		displayname "ColorLight"
		description "A light makes the environment bright and colorful"
		category hue
		using com.mycompany.type.Color ; 1.0.0
		
		functionblock ColorLight {
			configuration {
				optional brightnessLevel as int
				optional defaultColor as Color
			}
		
			fault {
				mandatory bulbDefect as boolean "true if the light bulb of the lamp is defect"
			}
		
			operations {
				setR(r as int)
				setG(g as int)
				setB(b as int)
			}
		
		}

3. Drag and drop the two created and edited function blocks from the **Function Block Models** tab into the information model **MyLightingDevice** in the **Information Models** tab to create the reference.  
   ![Drag and drop from Function Block to Information Model]({{base}}/img/documentation/m2m_tc_drag_drop_function_block_to_information_model.png)
4. Update the information model according the following:

		namespace com.mycompany
		version 1.0.0
		displayname "PhilipsHue"
		description "Information model for PhilipsHue"
		category demo
		using com.mycompany.fb.Switchable ; 1.0.0
		using com.mycompany.fb.ColorLight ; 1.0.0
		
		infomodel MyLightingDevice {
		
			functionblocks {
				switchable as Switchable
				colorlight as ColorLight
			}
		}