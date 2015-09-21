## Creating and Editing a Information Model Definition

**Prerequisites**

You have started your IDE, and you are in Vorto Perspective

**Proceed as follows**

1. Create a new function block with name Lamp, update the function block with following properties.    

	namespace com.mycompany.fb
	version 1.0.0
	displayname "Lamp"
	description "A lamp makes the environment bright"
	category demo
	functionblock Lamp {
	
		configuration{ 
			optional blink as boolean
			optional on as boolean
		}
	
		status{ 
			optional on as boolean
			optional powerConsumption as int  
		}
	
		fault{
			optional bulbBroken as boolean
		}
	
		operations{
			off() "turns the lamp off"  
			on() "turns the lamp on" 
			toggle() "switches the lamp on or off"
		}
	}	  

2. Create a new information model with name "MyLightingDevice", in Vorto perspective, drag function block "Lamp" to this information model.

	namespace com.mycompany
	version 1.0.0
	using com.mycompany.fb.Lamp ; 1.0.0
	displayname "MyLightingDevice"
	description "Information model for MyLightingDevice"
	category demo	

	infomodel MyLightingDevice {
	
		functionblocks {
			lamp as Lamp
		}
	}