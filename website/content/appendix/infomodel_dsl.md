---
menu:
  main:
    parent: 'Reference Documentation'
date: 2016-03-09T20:08:11+01:00
title: Information Model DSL
weight: 107
--- 
This section describes the DSL grammer for a Vorto Information Model.
<!--more-->

## Information Model DSL Reference

T
Information Model DSL import function block model DSL.

    InformationModel:
        'namespace' qualifiedName
        'version' version
        (modelReference)*
        'infomodel' id '{'
        'displayname' string
        ('description' string)?
        ('category' category)?
        'functionblocks' '{'
            (functionblockProperty)*
        '}'
    ;

    category:id;

    functionblockProperty: 
    	('mandatory' | 'optional')? ('multiple')? id 'as' [fbs::functionblockModel|qualifiedName]
        (description)?;

    string:
        '"' ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|'"') )* '"' |
        "'" ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|"'") )* "'"
    ;

    qualifiedName: id ('.' id)*;

    version : int('.' int)*('-'id)?;

    id:
        '^'?('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
    ;

### Information Model DSL Semantics

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
    <td>A descriptive name</td>
    <td>Philips Hue</td>
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
    <td>Short description</td>
    <td>Light Strip</td>
  </tr>
  <tr>
    <td>category</td>
    <td>N</td>
    <td>Custom tag to categorize the model</td>
    <td>Light, Smarthome, PayloadMapping</td>
  </tr>
  <tr>
    <td>functionblocks</td>
    <td>Y</td>
    <td>References the Function Blocks that define the capabilities of the device</td>
    <td>Gyrometer, Distance</td>
  </tr>
  </tbody>
</table>

-------------------------------------------
