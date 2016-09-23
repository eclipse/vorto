---
layout: documentation
title: Information Model Mapping DSL Reference
---

## Information Model Mapping DSL Reference

This section details the following topics:

[Information Model Mapping DSL Syntax](#information-model-mapping-dsl-syntax)  

[Information Model Mapping DSL Semantics](#information-model-mapping-dsl-semantics)  

## Information Model Mapping DSL Syntax

    'mapping' '{'
        'model' [infomodel::informationModel|qualifiedName]
        'target' id
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
        (StereoType,)*
    ;

    stereoType:
        id (with Attribute (',' attribute)*)
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

## Information Model Mapping DSL Semantics

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
