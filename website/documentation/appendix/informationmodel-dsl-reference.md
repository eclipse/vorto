---
layout: documentation
title: Information Model DSL Reference
---
{% include base.html %}

## Information Model DSL Reference

This section details the following topics:

[Information Model DSL Syntax](#information-model-dsl-syntax)  

[Information Model DSL Semantics](#information-model-dsl-semantics)  

## Information Model DSL Syntax

Information Model DSL import function block model DSL.

    InformationModel:
        'namespace' qualifiedName
        'version' version
        (modelReference)*
        'infomodel' id '{'
        'displayname' stirng
        ('description' string)?
        'category' category
        'functionblocks' '{'
            (functionblockProperty)*
        '}'
    ;

    category:id;

    functionblockProperty: id 'as' [fbs::functionblockModel|qualifiedName]
        (string)?;

    string:
        '"' ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|'"') )* '"' |
        "'" ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|"'") )* "'"
    ;

    qualifiedName: id ('.' id)*;

    version : int('.' int)*('-'id)?;

    id:
        '^'?('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*
    ;

## Information Model DSL Semantics

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
    <td>description</td>
    <td>Y</td>
    <td>Short description</td>
    <td>Light Strip</td>
  </tr>
  <tr>
    <td>vendor</td>
    <td>Y</td>
    <td>Vendor Identifier </td>
    <td>www.philips.com</td>
  </tr>
  <tr>
    <td>category</td>
    <td>Y</td>
    <td>Device Category</td>
    <td>Light</td>
  </tr>
  <tr>
    <td>version</td>
    <td>Y</td>
    <td>Model Version</td>
    <td>2.0.0</td>
  </tr>
  <tr>
    <td>functionblocks</td>
    <td>N</td>
    <td>Composition of Function blocks</td>
    <td>ColorLight, Switch</td>
  </tr>
  </tbody>
</table>
