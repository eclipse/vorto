---
layout: documentation
title: Defining a Function Block
---
{% include base.html %}
## Defining a Function Block

This section details the following topics:

[Function Blocks](#function-blocks)  

[Creating a New Function Block](#creating-a-new-function-block)  

[Editing a Function Block](#editing-a-function-block)

## Function Blocks

A function block provides an abstract view on a device to applications that want to employ the devices' functionality. Thus, it is a consistent, self-contained set of (potentially re-usable) properties and capabilities.

For more detailed concept of function block please refer to section *Function Block Specification* in the *Vorto Developer Guide*.

A function block definition is typically structured as follows:

- **Properties**  
    - Status  
    - Configuration  
    - Fault  
    - Events  
- **Services** - they represent the functional operations offered by the device.

## Creating a New Function Block

**Prerequisites**  

- You have started your IDE.  
- You have selected the the Vorto perspective.

**Proceed as follows**

1.  Right-click in the **Function Block Models** tab and choose **Create new Function Block** from the context menu:  
  ![Creating function block]({{base}}/img/documentation/m2m_tc_create_function_block_2.png)  
   Alternatively, you can click **File > New > Project...**. in the main menu. In the opening **New Project** dialog select **Vorto > Function Block Model** and click **Next**.  
   The **Create Function Block** dialog opens.  
   ![Creating function block designer dialog]({{base}}/img/documentation/m2m_tc_create_function_block_designer_dialog_2.png)

2. Adjust the entries for the input fields **Function Block Name**, **Version**, **Description** and **Project Name** according to your needs and click **Finish**.  
   The function block model DSL source file (with suffix .fbmodel) is generated and displayed in the function block editor. The file contains a complete structure according the DSL syntax with the values given in the preceding step.  
   ![Generated source]({{base}}/img/documentation/m2m_tc_create_function_block_generated_source_1.png)

## Editing a Function Block

**Prerequisites**

You have created a function block project (refer to [Creating a New Function Block](#creating-a-new-function-block)).

**Proceed as follows**

Edit the function block project by extending the generated source file in the function block DSL editor.

**Example**

	namespace com.mycompany.fb
	version 1.0.0
    functionblock Lamp {
      displayname "Lamp"
      description "A lamp makes the environment bright"
      category demo

      configuration{
        mandatory blinking as boolean "if the lamp is currently blinking or not"
        mandatory on as boolean "if the lamp is currently switched on"
        mandatory powerConsumption as int
          "the amount of power the lamp is consuming"  
      }

      fault{
        mandatory bulbDefect as boolean
          "true if the light bulb of the lamp is defect"
      }

      operations{
        blink(blinkType as int) "sets the blinking type for the lamp"
        getPowerConsumption() returns int
          "gets the amount of power being consumed by the lamp"
        isOn() returns boolean "checks if the lamp is switched on"
        off() "turns the lamp off"
        on() "turns the lamp on"
        stopBlinking() "stops the blinking of the lamp"
        toggle() "switches the lamp on or off"
        toggleWithDelay(delayInSeconds as int)
          "switches the lamp on or off with a delay of the specified seconds"
      }
    }
