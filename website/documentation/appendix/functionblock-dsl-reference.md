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
        ('breakable')? id '(' (param (paramDescription)?)? ')' ('returns'  returnType)? (returnTypeDescription)?
    ;

    returnType :
        returnObjectType | returnPrimitiveType
    ;

    returnObjectType :
        ('multiple')? [datatype::type | qualifiedName]
    ;

    returnPrimitiveType :
        ('multiple')? primitiveType ('<'constraintType [constraintIntervalType] ("," constraintType [constraintIntervalType])*'>')?
    ;

    primitiveParam:
        (multiple')? id 'as' primitiveType ('<'constraintType [constraintIntervalType] ("," constraintType [constraintIntervalType])*'>')?
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
        blink(blinkType as int <MIN 0, MAX 5> "The type of blink") "sets the blinking type for the lamp"  
        getPowerConsumption() returns int <MIN 0, MAX 5> "gets the amount of power being consumed by the lamp"  
        isOn() returns boolean "checks if the lamp is switched on"  
        breakable off() "turns the lamp off"  
        breakable on() "turns the lamp on"  
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
        blink(blinkType as int <MIN 0, MAX 5> "The type of blink") "sets the blinking type for the lamp"
        getPowerConsumption() returns int <MIN 1, MAX 3000>
          "gets the amount of power being consumed by the lamp"
        isOn() returns boolean "checks if the lamp is switched on"
        breakable off() "turns the lamp off"
        breakable on() "turns the lamp on"
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
    <li>Dictionary (with optional key and value types)</li>
    <li>Another Entity</li>
    <li>Another Enum</li>
	  </ul>
    </td>
  </tr>
  <tr>
    <td>with { [propertyAttribute] }</td>
    <td>N</td>
    <td>Additional property attributes</td>
    <td>Supported property attributes:</br>
    <ul>
    <li>measurementUnit : an Enum literal</li>
    <li>readable: [true or false]</li>
    <li>writable: [true or false]</li>
    <li>eventable: [true or false]</li>
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

    mandatory engineTemperature as float 
      with { measurementUnit: Temperature.Celsius, readable: true, writable: true }

    mandatory positiveNum as int <MIN 0 , MAX 1111>
      "Positive numbers that is within [0 , 1111]"

    mandatory lookupTable as Dictionary[string, Color] "Lookup table conversion for color"

    mandatory lookupRGBTable as Dictionary[Color, RGB] "Lookup table conversion for color from description to RGB"

    mandatory temperatureLabelConversion as Dictionary[int, string] "map of temperature to description"

    mandatory noKeyAndValueTypeMap as Dictionary "example of a map with no key and value types"

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
    <td>Without parameter(s)</td>
    <td>
      <ul>
        <li>resetConfiguration()</li>
        <li>switchOn()</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>With parameter(s)</td>
    <td>
      <ul>
        <li>setTemperature(temperature as int)</li>
        <li>setProductConfigurationList(multiple products as Product)</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>With return type</td>
    <td>
      <ul>
        <li>getProductIdsList() returns multiple string</li>
        <li>getProductConfiguration() returns Product</li>
      </ul>
    </td>
  </tr>
  <tr>
    <td>With parameter(s) and return type</td>
    <td>
      <ul>
        <li>withdraw(productId as string, amount as int) returns boolean</li>
      </ul>
    </td>
  </tr>
  </tbody>
</table>

All operations may have a \<description\> at the end of each parameter declaration and the operations definition.

**Example**

    getProductConfigurationList() returns multiple Product
      "Gets the current configured products from the vending machine"
    getProductConfigurationList(count as int "Number of products to return", offset as int "What row to start") 
      returns multiple Product "Gets the current configured products from the vending machine"

The parameters and return type can also have constraints.

**Example**
    
    setRedComponent(R as int <MIN 0, MAX 255>)
    getRedComponent() returns int <MIN 0, MAX 255>

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