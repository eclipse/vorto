---
layout: documentation
title: Model Mappings
---
{% include base.html %}

## Mapping Models

Vorto allows the user to define mapping rules to map Vorto models to other target platform domain models.

The primary purpose of defining Mapping models in Vorto is to directly translate or map datatype/functionblock/information model descriptions from Vorto to any other IoT platforms (For e.g Kura, Eclipse Smarthome). Vorto Mapping models acts as a bridge between Vorto definitions and target platforms/technologies.

In addition to that, the mapping models provides an option to enhance the Vorto models with additional meta data descriptions targeted for a specific platform. Thereby the code generators can leverage the expanded meta data descriptions customized for their platforms. Vorto helps to  integrate with other IoT platforms through the Mapping models in flexible manner. Here are few possible scenarios where Mapping models can be used. 

 - Translate Vorto model definitions to understand other IoT platforms
 - Provide additional metadata in which Vorto models is not able to describe for specific IoT solutions.
 - Provide different mapping models for multiple versions of target platforms (For eg. iOS 7/8) 


Mapping models can be defined for
 
 - [Datatypes](./data-type-mapping.html)
 - [Function Blocks](./function-block-mapping.html)
 - [Information Models](./information-model-mapping.html)   

The Mapping models contains direct mapping of Vorto grammar descriptions to custom defined `Stereotypes` and `Attributes` specific for that target platform. 

`Stereotypes` are custom defined keyword which is leveraged by target platform generators.

`Attributes` are custom defined key value pairs for a specific Stereotypes which is again leveraged by specific target platform generators.    

A simple Mapping model definition from Vorto to Philips Hue looks likes below, 

	from ColorLight.category to philipshue with {configuration : "devicetype"}

In the above mapping 'philipshue' is a custom defined stereotype and 'configuration' is custom defined attribute and a value 'devicetype' is actual value which is being mapped to Philips Hue definitions. The code generators can make use of these additional metadata which could be targeted for specific IoT solutions or platforms. 



To demonstrate the potential for Mapping models, let us define a mapping model to enhance Alexa commands for AWS genertor.

Lets take a simple example of `ColorLight` functionblock definition, 


	namespace com.mycompany.fb
	version 1.0.0
	displayname "ColorLight"
	description "Function block model for ColorLight"
	category demo	
	functionblock ColorLight {
		status{ 
			mandatory brightness as int
		}
		operations{
			setBrightness(level as int)		
		}
	}


and with `ColorLightSystem` information model definition, 	

	namespace com.mycompany.informationmodels
	version 1.0.0
	displayname "ColorLightSystem"
	description "Information model for ColorLightSystem"
	category demo
	using com.mycompany.fb.ColorLight ; 1.0.0
	using com.mycompany.fb.Switcher ; 1.0.0
	
	infomodel ColorLightSystem {
	
		functionblocks {
			colorlight as ColorLight
		}
	}

By invoking AWS generator without any mapping models from Vorto Repository, it generates the following Alexa voice command vocabulary in `colorlightsystemUtterances.txt` file. These  voice commands are provided as a input to AWS service to set the appropriate brightness level to any device. The details of how the AWS services control the actual devices are beyond the scope of this documentation. 
	
	setBrightness {level}
	
	brightness get brightness status
  

Lets define a mapping model `ColorLightMapping` to enhance the Alexa voice commands vocabulary for the `ColorLight` functionblock. Thereby more voice commands are recognized by AWS services to control the actual devices.

	namespace examples.mappings.aws
	version 1.0.0
	displayname "Color Light AWS Mapping"
	description "Maps Color Light functionblock to AWS services"
	using com.mycompany.fb.ColorLight;1.0.0
	functionblockmapping ColorLightMapping {
		targetplatform aws
		from ColorLight.operation.setBrightness to alexa with {command: "set brightness to {level} ; increase brightness to {level} ; decrease brightness to {level}"}		
	}	


In the above mapping, the Alexa voice command is enhanced by additional command with `increase` and `decrease` to control the `ColorLight`.  Here `alexa` is a custom defined `Stereotype` keyword which AWS code generator interprets and makes use of the additional values defined in the mapping model. Again `command` is a custom defined `Attribute` keyword which is AWS code generator can understand. 


Once this mapping model `ColorLightMapping` is shared using Vorto Repository Web UI page as below

 ![Share Mapping Model]({{base}}/img/documentation/vorto_share_mapping_model.png). 

<table class="table table-bordered">
	<tbody>
 <tr>
   <td><i class="fa fa-info-circle info-note"></i></td>
    <td>Note: Currently mapping models are shared only through Vorto repository Web UI.</td>
  </tr></tbody>
</table>  


With this mapping model, when the AWS generator is invoked, it generates the following `colorlightsystemUtterances.txt`

	setBrightness set brightness to {level} 
	setBrightness  increase brightness to {level} 
	setBrightness  decrease brightness to {level}
	
	brightness get brightness status   

Here the Vorto mapping model descriptions greatly enhances the capabilities of any platform, in this case it is AWS Alexa platform.



<div class="thumb1">
<a title="Defining a new Mapping" data-rel="prettyPhoto" href="https://youtu.be/C5c5tTFwG0U&width=1500&height=1000" rel="prettyPhoto" >
<img src="{{ $base}}/img/documentation/defineinfomap.jpg"  class="box-img img-responsive zoom1">
<i class="fa fa-play-circle fa-5 play-icon"></i>
</a>
</div>

