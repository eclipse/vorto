---
menu:
  main:
    parent: 'Reference Documentation'
date: 2016-03-09T20:08:11+01:00
title: Function Block DSL
weight: 108
---
This section describes the DSL grammer for a Vorto Function Block.
<!--more-->

## Function Block Model DSL Reference

The following code represents the Function Block Model DSL syntax. Function block model use all variables that are defined in Data Type DSL.

    functionblockmodel:
        'namespace' qualifiedName
        'version' version
        'displayname' displayname
        ('description' description)?
        ('category' category)?
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
        (presence)? ('breakable')? id '(' (param (paramDescription)?)? ')' ('returns'  returnType)? (returnTypeDescription)?
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


### Function Block Model

A function block model describes a function block with its properties and behavior. For this it can contain a functionblock element (refer to [Function Block](#function-block)) and no or several entity elements (refer to [Entity](#entity)).

**Syntax**

Refer to functionblockmodel in [Function Block DSL Syntax](#function-block-dsl-syntax).

**Example**

	namespace com.mycompany.fb
	version 1.0.0
	description "A lamp makes the environment bright"  
    functionblock Lamp {  
        
      configuration{  
        mandatory blinking as boolean "if the lamp is currently blinking or not"  
        mandatory on as boolean "if the lamp is currently switched on"  
      }  

      status {
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

- General metadata parameters (`displayname, description, namespace, category, version`).
- Property elements (`configuration, status, fault, events`).
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
    <th>Examples</th>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td>Name</td>
    <td>Y</td>
    <td>The name of the Function Block</td>
    <td>DistanceSensor</td>
  </tr>
  <tr>
    <td>namespace</td>
    <td>Y</td>
    <td>Namespace Identifier </td>
    <td>com.bosch</td>
  </tr>
  <tr>
    <td>version</td>
    <td>Y</td>
    <td>Model Version</td>
    <td>2.0.0</td>
  </tr>
  <tr>
    <td>description</td>
    <td>Y</td>
    <td>Short description of the Function Block</td>
    <td></td>
  </tr>
  <tr>
    <td>category</td>
    <td>N</td>
    <td>Custom tag to categorize the Function Block</td>
    <td>Smarthome, Manufacturing</td>
  </tr>
  <tr>
    <td>extends [Other Function Block]</td>
    <td>N</td>
    <td>Extends another Function Block by specializing it with extended properties</td>
    <td>Oven extends Managable</td>
  </tr>
  <tr>
    <td>configuration</td>
	<td>N</td>
    <td>Defines read- and writable properties to configure a device</td>
    <td>
		configuration {
			mandatory on as boolean
		}
	</td>
  </tr>
  <tr>
    <td>status</td>
    <td>N</td>
    <td>Defines readable properties that define the current status of the device</td>
    <td>
		status {
			mandatory powerConsumption as float
		}
	</td>
  </tr>
  <tr>
    <td>fault</td>
    <td>N</td>
    <td>Defines readable properties that define the fault states of device</td>
    <td>
		fault {
			mandatory code as int
		}
	</td>
  </tr>
   <tr>
    <td>events</td>
    <td>N</td>
    <td>Defines readable properties that describe events that a device can publish</td>
    <td>
		events {
			TemperatureTooHigh {
				mandatory temperatureValue as float
				mandatory temperatureThreshold as float
			}
		}
	</td>
  </tr>
  <tr>
    <td>operations</td>
    <td></td>
    <td>Defines operations that can be invoked on the device from e.g. external applications</td>
    <td>
		operations {
			setColor(r as int <MIN 0,MAX 255>,g as int <MIN 0,MAX 255>,b as int <MIN 0,MAX 255>)
			getUpdateStatus() returns string
		}
	</td>
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
      }
      
      status {
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

### Function Block Property

Here are some example of Function Block properties which can be used for status-, configuration-, fault- or event properties:

**Examples**

    optional multiple products as Product
      "the products that are offered by the vending machine"

    optional vendingMachineIdentifier as string <STRLEN 8>
      "the id of the vending machine"

    mandatory engineTemperature as float 
      with { measurementUnit: Unit.Celsius, readable: true, writable: true }

    mandatory positiveNum as int <MIN 0 , MAX 1111>
      "Positive numbers that is within [0 , 1111]"

    mandatory lookupTable as dictionary[string, Color] "Lookup table conversion for color"

    mandatory lookupRGBTable as dictionary[Color, RGB] "Lookup table conversion for color from description to RGB"

    mandatory temperatureLabelConversion as dictionary[int, string] "map of temperature to description"

    mandatory noKeyAndValueTypeMap as dictionary "example of a map with no key and value types"

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


-------------------------------------
