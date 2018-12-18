---
menu:
  main:
    parent: 'Reference Documentation'
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
    <td>Name</td>
    <td>Y</td>
    <td>The name of the Mapping</td>
    <td>DistanceIpsoMapping</td>
  	</tr>
  <tr>
    <td>namespace</td>
    <td>Y</td>
    <td>Namespace Identifier </td>
    <td>com.bosch.mapping.ipso</td>
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
    <td>Short description of the mapping</td>
    <td></td>
  </tr>
  <tr>
		<td>targetplatform</td>
		<td>Y</td>
		<td>Target platform mapping is used for</td>
		<td>lwm2m</td>
	</tr>
	<tr>
		<td>from</td>
		<td>Y</td>
		<td>Function Block model element</td>
		<td>ColorLight.configuration.on</td>
	</tr>
	<tr>
		<td>to</td>
		<td>Y</td>
		<td>Maps Function Block property to a stereotype or other reference mapping</td>
		<td>from ColorLight.configuration.on to OBJECT with {ID:4523}</td>
	</tr>
  </tbody>
</table>

-----------------------------
