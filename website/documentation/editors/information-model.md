---
layout: documentation
title: Defining Information Models
---
{% include base.html %}

## Defining Information Models

This section details the following topics:

[Information Models](#information-models)  

[Creating a New Information Model](#creating-a-new-information-model)  

[Editing an Information Model](#editing-an-information-model)  

## Information Models

Information models represent the capabilities of a particular type of device entirety. An information model contains one or more function blocks.

<div class="thumb5">
<a title="Defining a Information Model" data-rel="prettyPhoto" href="https://youtu.be/MJI74NuYTw4&width=1500&height=1000" rel="prettyPhoto" >
<img src="{{ $base}}/img/documentation/defineinfo.jpg"  class="box-img img-responsive zoom1">
<i class="fa fa-play-circle fa-5 play-icon"></i>
</a>
</div>

## Creating a New Information Model

**Prerequisites**  

You have create a function block model (refer to [Creating a New Function Block Project]({{base}}/documentation/editors/functionblock.html#creating-a-new-function-block)).

**Proceed as follows**

1. In the **Vorto Model Project Browser**, select the project in the **Select Vorto Project** drop-down list.  
   ![Select a Project]({{base}}/img/documentation/vorto_select_vorto_project.png)  
2. Right-click in the **Information Models** area and choose **New Information Model** from the context menu.  
   The **Create Information Model** dialog opens:  
   ![Create information model dialog]({{base}}/img/documentation/m2m_tc_create_information_model_dialog.png)
3. Enter a name as **Information Model Name**, for example, `PhilipsHue`.  
4. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
5. Optionally, enter a description in the **Description** entry field.
6. Click **Finish**.  
   The new information model (`PhilipsHue`) is created. Furthermore, the information model DSL source file (`.infomodel`) is generated and displayed in the model editor. The file contains a complete structure according to the DSL syntax with the values given in the preceding steps.  
   ![Information model DSL editor]({{base}}/img/documentation/m2m_tc_information_model_dsl_editor.png)

## Editing an Information Model

**Prerequisites**

You have created an information model (refer to [Creating a new Information Model](#creating-a-new-information-model)).

**Proceed as follows**

Edit the information model by extending the generated source file in the information model DSL editor.

**Example**

1. Drag and drop the data type **Color** from the **Datatypes** area into the `ColorLight` funciton block.
2. Create a new function block model named `Switchable` and update it according the following:  

		namespace com.mycompany.fb
		version 1.0.0
		displayname "Switchable"
		description "Function block model for Switchable"
		category demo	
		functionblock Switchable {
			status{ 
				optional on as boolean
			}
		
			operations{
				on()
				off()
				toggle()
			}
		}

3. Drag and drop the two created and edited function blocks from the **Functionblocks** area into the information model `PhilipsHue` in the **Information Models** area to create the reference.  
   The updated information model appears as follows:
   ![Drag and drop from Function Block to Information Model]({{base}}/img/documentation/m2m_tc_drag_drop_function_block_to_information_model.png)  
