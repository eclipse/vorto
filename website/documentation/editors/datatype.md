---
layout: documentation
title: Defining Data Types
---
{% include base.html %}

## Defining Data Types

This section details the following topics:  

[Data Types](#data-types)  

[Creating a New Data Type](#creating-a-new-data-type)  

[Editing a Data Type](#editing-a-data-type)

## Data Types

A data type is a reusable entity that can be shared between function blocks. We distinguish between **Enum** represening an enumeration data type and **Entity** representing one of all other data types. 

<div class="thumb5">
  <a title="Defining a Data Type" data-rel="prettyPhoto" href="https://youtu.be/zZBwPKJTR-4&width=1500&height=1000" rel="prettyPhoto" >
  <img src="{{ $base}}/img/documentation/definedt.jpg"  class="box-img img-responsive zoom1">
  <i class="fa fa-play-circle fa-5 play-icon"></i>
  </a>
</div>

## Creating a New Data Type

**Prerequisites**  

- You have started your IDE.  
- You have selected the Vorto perspective.
- You have created a project (refer to [Defining Projects]({{base}}/documentation/editors/project.html))

**Proceed as follows**

1. In the **Vorto Model Project Browser**, select the project in the **Select Vorto Project** drop-down list.  
   ![Select a Project]({{base}}/img/documentation/vorto_select_vorto_project.png)  
2. Right-click in the **Datatypes** area and choose **New Entity** (or **New Enum**, if applicable) from the context menu.  
   The **Create entity type** dialog opens:  
   ![Create a new entity dialog]({{base}}/img/documentation/m2m_tc_create_a_new_entity_dialog.png)  
3. Enter a name as **Entity Name**, for example, `Color`.  
4. Adjust the entries for the input fields **Namespace** and **Version**, if necessary.
5. Optionally, enter a description in the **Description** entry field.
6. Click **Finish**.  
   The new data type (entity `Color`) is created. Furthermore, the data type DSL source file (`.type`) is generated and displayed in the model editor. The file contains a complete structure according to the DSL syntax with the values given in the preceding steps.  
   ![New data type entity]({{base}}/img/documentation/m2m_tc_new_data_type_entity.png)

## Editing a Data Type

**Prerequisites**

You have created a data type (refer to [Creating a new Data Type](#creating-a-new-data-type)).

**Proceed as follows**

1. In the **Datatype Models** area, click the data type entity you want to edit, for example, `Color`.  
   The DSL editor for the file `*_Color_*.type` opens.
2. In the DSL editor, edit the entity according to your needs.

**Example**

    namespace com.mycompany.type
    version 1.0.0
    displayname "Color"
    description "Type for Color"
    category demo		
    entity Color {
      mandatory r as int <MIN 0, MAX 255>
      mandatory g as int <MIN 0, MAX 255>
      mandatory b as int <MIN 0, MAX 255>
    }
