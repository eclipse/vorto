---
date: 2016-03-09T20:08:11+01:00
title: Mapping DSL
weight: 110
---

This section describes the DSL grammer for a Vorto Mapping.
<!--more-->

## Information Model Mapping DSL Reference


    'functionblockmapping' '{'
        'targetplatform' id
        (rule)*
    '}'
    ;

    rule:
        'from' informationModelElement (',' informationModelElement)*
        'to' targetElement
    ;

    informationModelElement:
        [functionblock::functionblockModel|qualifiedName] '.'
        functionBlockElement;
    ;

    functionBlockElement:
        (operationElement | configurationElement | statusElement |
            faultElement | eventElement
        )
    ;

    operationElement:
        'operation.' [functionblock::operation]
    ;

    configurationElement:
        'configuration.' [datatype::property]
    ;

    statusElement:
        'status.' [datatype::property]
    ;

    faultElement:
        'fault.' [datatype::property
    ;

    eventElement:
        'event.' [functionblock::event]
    ;

    targetElement:
        stereoType | referencetype
    ;

    stereoType:
        id (with Attribute (',' attribute)*)
    ;
    
    referenceType:
    	'reference' [mapping::MappingModel]
    ;

	qualifiedName: id ('.' id)*;

    attribute:
        id ':' string
    ;

    id:
        '^'?('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
    ;

    string  :
        '"' ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|'"') )* '"' |
        "'" ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|"'") )* "'"
    ;

### Mapping DSL Semantics

<table class="table table-bordered">
  <thead>
	<tr>
		<th>Parameter</th>
		<th>Mandatory</th>
		<th>Description</th>
		<th>Example</th>
	</tr>
  </thead>
  <tbody>
	<tr>
		<td>model</td>
		<td>Y</td>
		<td>Information Model Name   </td>
		<td>PhilipsHue</td>
	</tr>
	<tr>
		<td>target</td>
		<td>Y</td>
		<td>Target platform mapping is used for</td>
		<td>smarthome</td>
	</tr>
	<tr>
		<td>from</td>
		<td>Y</td>
		<td>Information model element</td>
		<td>ColorLight.operation.setR</td>
	</tr>
	<tr>
		<td>to</td>
		<td>Y</td>
		<td>Target plaform platform element</td>
		<td>channelType</td>
	</tr>
  </tbody>
</table>

-----------------------------
