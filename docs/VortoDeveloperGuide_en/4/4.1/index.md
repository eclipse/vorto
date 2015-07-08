## Creating and Editing a Function Block Definition

**Prerequisites**

You have started your IDE.

**Proceed as follows**

1. Create a new function block with name Lamp.
2. Update the function block with following properties.    

        functionblock Lamp {    
            displayname "Lamp"    
            description "A lamp makes the environment bright"   
            vendor www.bosch.com    
            category demo   
            version 1.0.0    
            configuration{    
                optional blink as boolean   
                optional on as boolean    
            }    
            status {    
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
