---
layout: documentation
title: Defining Function Blocks
---
{% include base.html %}
## Defining Function Blocks

This section details the following topics:

[Function Blocks](#function-blocks)  

[Creating a New Function Block](#creating-a-new-function-block)  

[Editing a Function Block](#editing-a-function-block)

## Function Blocks

A function block provides an abstract view on a device to applications that want to employ the devices' functionality. Thus, it is a consistent, self-contained set of (potentially re-usable) properties and capabilities.

<div class="thumb5">
<a title="Defining a Function Block" data-rel="prettyPhoto" href="https://youtu.be/bcmXZN3IPmI&width=1500&height=1000" rel="prettyPhoto" >
<img src="{{ $base}}/img/documentation/definefb.jpg"  class="box-img img-responsive zoom1">
<i class="fa fa-play-circle fa-5 play-icon"></i>
</a>
</div>

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
- You have created a project (refer to [Defining Projects]({{base}}/documentation/editors/project.html))

**Proceed as follows**

1. In the **Vorto Model Project Browser**, select the project in the **Select Vorto Project** drop-down list.  
   ![Select a Project]({{base}}/img/documentation/vorto_select_vorto_project.png)  
2.  Right-click in the **Functionblocks** area and choose **New Functionblock** from the context menu.  
   The **Create Function Block Model** dialog opens.  
   ![Creating function block designer dialog]({{base}}/img/documentation/m2m_tc_create_function_block_designer_dialog_2.png)
3. Enter a name as **Function Block Name**, for example, `ColorLight`.  
4. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
5. Optionally, enter a description in the **Description** entry field.
6. Click **Finish**.  
   The new function block (`ColorLight`) is created. Furthermore, the function block DSL source file (`.fbmodel`) is generated and displayed in the model editor. The file contains a complete structure according to the DSL syntax with the values given in the preceding steps.  
   ![Generated source]({{base}}/img/documentation/m2m_tc_create_function_block_generated_source_1.png)


## Editing a Function Block

**Prerequisites**

You have created a function block project (refer to [Creating a New Function Block](#creating-a-new-function-block)).

**Proceed as follows**

Edit the function block project by extending the generated source file in the function block DSL editor.

**Example**

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
