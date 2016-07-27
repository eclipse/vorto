---
layout: documentation
title: Function Block Model DSL Reference
---
{% include base.html %}
## Function Block Model DSL Reference

This section details the following topics:  

[Function Block DSL Syntax](#function-block-dsl-syntax)  

[Function Block DSL Semantics](#function-block-dsl-semantics)  

## Function Block DSL Syntax

The following code represents the Function Block Model DSL syntax. Function block model use all variables that are defined in Data Type DSL.

    functionblockmodel:
        'namespace' qualifiedName
        'version' version
        'displayname' displayname
        ('description' description)?
        'category' category
        (modelReference)*
        'functionblock' id ('extends' functionblockmodel)? '{'
            functionblock
        '}'
    ;

    functionblock:
        'configuration' '{' 
            (property)*  
        '}'

        ('status' '{'
            (property)*
        '}')?

        ('fault' '{'
            (property)*
        '}')?

        ('events' '{'
            (event)*
        '}')?

        ('operations' '{'
            (operation)*
        '}')?
    ;

    operation :
        ('breakable')? id '(' (param (description)?)? ')' ('returns'  returnType)? (string)?
    ;

    returnType :
        returnObjectType | returnPrimitiveType
    ;

    returnObjectType :
        ('multiple')? [datatype::type | qualifiedName]
    ;

    returnPrimitiveType :
        ('multiple')? primitiveType
    ;

    primitiveParam:
        (multiple')? id 'as' primitiveType
    ;

    refParam:
        ('multiple')? id 'as' [datatype::type|qualifiedName]
    ;

    param:
        primitiveParam | refParam
    ;

    event:
        id '{'
            (property)*
        '}'
    ;

    string:
        '"' ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|'"') )* '"' |
        "'" ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|"'") )* "'"
    ;

    qualifiedName: id ('.' id)*;

    version : int('.' int)*('-'id)?;

    id:
        '^'?('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
    ;

## Function Block DSL Semantics

This section details the following topics:

[Function Block Model](#function-block-model)  

[Function Block](#function-block)  

[Property](#property)  

[Operation](#operation)  

[Event](#event)  

[Entity](#entity)  

[Enum](#enum)

### Function Block Model

A function block model describes a function block with its properties and behavior. For this it can contain a functionblock element (refer to [Function Block](#function-block)) and no or several entity elements (refer to [Entity](#entity)).

**Syntax**

Refer to functionblockmodel in [Function Block DSL Syntax](#function-block-dsl-syntax).

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

### Function Block

A `functionblock` element describes the properties and behavior of a function block. A `functionblock` element contains

- General metadata parameters (`displayname, description, vendor, category, version`).
- Property elements (`configuration, status, fault`).
- An `operations` element.

**Syntax**

Refer to `FunctionBlock` in [Function Block DSL Syntax](#function-block-dsl-syntax).

**Usage**

The following table describes the parameters and elements of a `functionblock`. Mandatory parameters or elements are marked with `Y` in the column **Mandatory**

<table  class="table table-bordered">
  <thead>
  <tr>
    <th>Parameter/Element</th>
    <th>Mandatory</th>
    <th>Description</th>
    <th>Type</th>
    <th>Examples</th>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td border="2">displayname</td>
    <td border="2"></td>
    <td border="2">A descriptive and user friendly name of the function block.</td>
    <td border="2">String enclosed in quotation marks</td>
    <td border="2">displayname "Vending Machine"</td>
  </tr>
  <tr>
    <td>description</td>
    <td></td>
    <td>Extra information about the function block.</td>
    <td>String enclosed in quotation marks</td>
    <td>description "A vending machine withdraws food or drinks"</td>
  </tr>
  <tr>
    <td>category</td>
	<td>Y</td>
    <td>The category should be used to logically group function blocks that semantically belong to the same domain.</td>
    <td>top level category (mandatory) and a sub category (optional), separated by aslash (/)</td>
    <td>IAP/smarthome</br>indego</td>
  </tr>
  <tr>
    <td>configuration</td>
	<td>Y</td>
    <td>Contains one or many configuration properties for the function block.</td>
    <td>complex type containing properties (see grammar for Property)</td>
    <td>...</br>configuration {</br>&nbsp;&nbsp;// properties</br>}</td>
  </tr>
  <tr>
    <td>status</td>
    <td></td>
    <td>Contains one or many status properties for the function block.</td>
    <td>complex type (see grammar for Property)</td>
    <td>...</br>status {</br>&nbsp;&nbsp;// properties</br>}</td>
  </tr>
  <tr>
    <td>fault</td>
    <td></td>
    <td>Contains one or many fault properties for the function block.</td>
    <td>complex type (see grammar for Property)</td>
    <td>...</br>fault {</br>&nbsp;&nbsp;// properties</br>}</td>
  </tr>
  <tr>
    <td>operations</td>
    <td></td>
    <td>Contains one or many operations for the function block.</td>
    <td>complex type (see grammar for Operation)</td>
    <td>...</br>operations {</br>&nbsp;&nbsp;// operations</br>}</td>
  </tr>
  <tr>
    <td>events</td>
    <td></td>
    <td>Contains one or many events for the function block.</td>
    <td>complex type (see grammar for Event)</td>
    <td>...</br>events {</br>&nbsp;&nbsp;// events go here</br>}</td>
  </tr>
  </tbody>
</table>

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
      events{
        defect{
          mandatory on as boolean "if on is true"
          mandatory powerConsumption as int "if powerConsumption is 0"
        }

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

### Property

**Syntax**

Refer to property in [Function Block DSL Syntax](#function-block-dsl-syntax).

**Usage**

The following table describes the property elements. Mandatory property elements are marked with `Y` in the column **Mandatory**

<table class="table table-bordered">
<thead>
  <tr>
    <th>Property element</th>
    <th>Mandatory</th>
    <th>Description</th>
    <th>Type</th>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td>optional&nbsp;|&nbsp;mandatory</td>
	<td>Y</td>
    <td>Declares if the property is optional or mandatory.</td>
    <td></td>
  </tr>
  <tr>
    <td>multiple</td>
    <td></td>
    <td>Defines if the property can have more than one value.</td>
    <td></td>
  </tr>
  <tr>
    <td>&lt;property_name&gt;</td>
	<td>Y</td>
    <td>A descriptive name of the property.</td>
    <td>String (identifier, without spaces)</td>
  </tr>
  <tr>
    <td>as &lt;type&gt;</td>
	<td>Y</td>
    <td>The data type of the property.</td>
    <td>Supported types:</br>
	  <ul>
		<li>boolean</li>
		<li>dateTime</li>
		<li>float</li>
		<li>int</li>
		<li>string</li>
		<li>long</li>
		<li>short</li>
		<li>double</li>
		<li>base64Binary</li>
		<li>byte</li>
	  </ul>
    </td>
  </tr>
  <tr>
    <td>&lt;constraints&gt;</td>
    <td></td>
    <td>Key value pair(s) enclosed by &lt;&gt;, following the pattern:</br>&lt;CONSTRAINT_KEYWORD value&gt;</td>
    <td>&lt;STRLEN value>, defining the length of a string</br>
	    &lt;MIN value&gt;, defining the maximum of a numeric property</br>
		&lt;MAX value&gt;, defining the minimum of a numeric property</br>
		&lt;REGEX value&gt;, defining the regular expression pattern of the property</br>
		&lt;REGEX value&gt;, defining the regular expression pattern of the string property. Value format expected should be of XML schema pattern format, e.g: [A-Z], [0-9], ([a-z][A-Z])+</td>
  </tr>
  <tr>
    <td>&lt;description&gt;</td>
    <td></td>
    <td>Extra information about the property.</td>
    <td>String enclosed in quotation marks</td>
  </tr>
  </tbody>
</table>

**Examples**

    optional multiple products as Product
      "the products that are offered by the vending machine"

    optional vendingMachineIdentifier as string <STRLEN 8>
      "the id of the vending machine"

    mandatory positiveNum as int <MIN 0 , MAX 1111>
      "Positive numbers that is within [0 , 1111]"

    mandatory biggerThanInt as long <MAX 999999999999999999>
      "Value for constraint is validated according to the property type,
      in this case, Long instead of Int"

    mandatory deviceName as string <REGEX '[a-zA-Z][a-zA-Z][0-9]'>
      "Only allow 2 uppercase or lowercase characters and 1 digit for
      device name"

### Operation

An operation can be seen as a function the function block can perform. Function block operations support the one-way as well as request-response operation pattern and are composed inside the operations element of a functionblock.

**Syntax**

Refer to operation in [Function Block DSL Syntax](#function-block-dsl-syntax).

**Usage**

The following table shows the operation types.

<table class="table table-bordered">
<thead>
  <tr>
    <th>Operation type</th>
    <th>Examples</th>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td>One-way without parameter(s)</td>
    <td>resetConfiguration()</br>switchOn()</td>
  </tr>
  <tr>
    <td>One-way with parameter(s)</td>
    <td>setTemperature(temperature as int)</br>setProductConfigurationList(multiple products as Product)</td>
  </tr>
  <tr>
    <td>Request - response without parameter(s)</td>
    <td>getProductIdsList() returns multiple string</br>getProductConfiguration() returns Product</td>
  </tr>
  <tr>
    <td>Request - response with parameter(s)</td>
    <td>withdraw(productId as string, amount as int) returns boolean</td>
  </tr>
  </tbody>
</table>

All operations may have a \<description\> at the end of the operations definition.

**Example**

    getProductConfigurationList() returns multiple Product
      "Gets the current configured products from the vending machine"

### Event

An event can be seen as a combination of zero or more properties. The events element of a functionblock can contain 0 or many event elements.

**Syntax**

Refer to event in [Function Block DSL Syntax](#function-block-dsl-syntax).

**Usage**

Refer to *Usage* in [Property](#property).

Example

    ...  
      events {  
        ready {  
          optional since as dateTime  
        }  
      }  
    ...  

### Entity

The entity element defines a new data type that can be referenced by the function block definition. In a function block model 0 or many entity elements can be defined (refer to [Function Block Model](#function-block-model)).

**Syntax**

Refer to entity in [Function Block DSL Syntax](#function-block-dsl-syntax).

**Usage**

<table class="table table-bordered">
<thead>
  <tr>
    <th>Entity element</th>
    <th>Mandatory</th>
    <th>Description</th>
    <th>Type</th>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td>&lt;entity_name&gt;</td>
    <td>Y</td>
    <td>A descriptive name of the entity.</td>
    <td>String (identifier, without spaces)</td>
  </tr>
  <tr>
    <td>extends &lt;property_name&gt;</td>
    <td></td>
    <td>The entity extends the property &lt;property_name&gt;.</td>
    <td></td>
  </tr>
  <tr>
    <td>&lt;optional&nbsp;|&nbsp;mandatory&gt;</td>
    <td></td>
    <td>Declares if the entity is optional or mandatory.</td>
    <td></td>
  </tr>
  <tr>
    <td>multiple</td>
    <td></td>
    <td>Defines if the entity can have more than one value.</td>
    <td></td>
  </tr>
  <tr>
    <td>&lt;property_name&gt;</td>
    <td>Y</td>
    <td>The name of the property.</td>
    <td>String (identifier, without spaces)</td>
  </tr>
  <tr>
    <td>as &lt;type&gt;</td>
    <td>Y</td>
    <td>The data type of the property.</td>
    <td>Supported types:
	  <ul>
	    <li>boolean</li>
		<li>dateTime</li>
		<li>float</li>
		<li>int</li>
		<li>string</li>
	  </ul>
	</td>
  </tr>
  <tr>
    <td>&lt;description&gt;</td>
    <td></td>
    <td>Extra information about the entity.</td>
    <td>String enclosed in quotation marks</td>
  </tr>
  </tbody>
</table>

**Example**

    entity BlinkType {  
      mandatory type as int "0: fast flash, 1: slow flash"  
    }  

### Enum

An Enum is data structure using group literals as values.

**Syntax**

Refer to entity in [Function Block DSL Syntax](#function-block-dsl-syntax).

**Usage**

<table class="table table-bordered">
<thead>
  <tr>
    <th>Entity element</th>
    <th>Mandatory</th>
    <th>Description</th>
    <th>Type</th>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td>&lt;enum_name&gt;</td>
    <td>Y</td>
    <td>A descriptive name of the enum.</td>
    <td>String (identifier, without spaces)</td>
  </tr>
  <tr>
    <td>{</td>
    <td>Y</td>
    <td>Delimiter of the literal list (comma separated literals).</td>
    <td></td>
  </tr>
  <tr>
    <td>&lt;literal&gt;</td>
    <td>Y</td>
    <td>A descriptive name of an enum value.</td>
    <td>String (identifier, without spaces)</td>
  </tr>
  <tr>
    <td>}</td>
    <td>Y</td>
    <td>The name of the property.</td>
    <td></td>
  </tr>
  </tbody>
</table>

**Example**

    // definition of an enum type
    enum Gender{
      Male, Female, NA
    }

    // usage as a field
    mandatory sex as Gender
