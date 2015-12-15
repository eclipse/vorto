---
layout: documentation
title: Defining a Data Type
---
{% include base.html %}

## Defining a Data Type

This section details the following topics:  

[Data Types](#data-types)  

[Creating a New Data Type](#creating-a-new-data-type)  

[Editing a Data Type](#editing-a-data-type)

## Data Types

A data type is a reusable entity that can be shared between function blocks.

<div class="thumb1">
<a title="Defining a Data Type" data-rel="prettyPhoto" href="https://youtu.be/zZBwPKJTR-4&width=1500&height=1000" rel="prettyPhoto" >
<img src="{{ $base}}/img/documentation/definedt.jpg"  class="box-img img-responsive zoom1">
<i class="fa fa-play-circle fa-5 play-icon"></i>
</a>
</div>

## Creating a New Data Type

**Prerequisites**  

- You have started your IDE.  
- You have selected the Vorto perspective.

**Proceed as follows**

1. Right-click in the **Datatype Models** tab and choose **Create new Entity** from the context menu, and the **Create entity type** dialog opens:  

   ![Create a new entity dialog]({{base}}/img/documentation/m2m_tc_create_a_new_entity_dialog.png)  
2. Enter `Color` as **Entity Name**.  
3. Click on **Finish**.  
   The new data type (entity) `Color` is created and the data type model DSL source file (`Color.type`) is generated.  

   ![New data type entity]({{base}}/img/documentation/m2m_tc_new_data_type_entity.png)

## Editing a Data Type

**Prerequisites**

You have created a data type (refer to [Create a new Data Type](#creating-a-new-data-type)).

**Proceed as follows**

1. In the **Datatype Models** tab, click the data type entity you want to edit, e.g., `Color`.  
   The DSL editor for the file `Color.type` opens.
2. In the DSL editor, edit the entity according to your needs.

**Example**

	namespace com.mycompany.type
	version 1.0.0
    entity Color{
      mandatory r as int <MIN 0, MAX 255>
      mandatory g as int <MIN 0, MAX 255>
      mandatory b as int <MIN 0, MAX 255>
    }

## Video on Defining a new Data Type