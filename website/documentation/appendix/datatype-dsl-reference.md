---
layout: documentation
title: Data Type Model DSL Reference
---
{% include base.html %}
## Data Type Model DSL Reference

The following code represents the Data Type Model DSL syntax.

	model:entity | enumeration;

	modelReference: 'using' qualifiedName ';' version;

	qualifiedName: id ('.' id)*;

	entity:
	   'namespace' qualifiedName
	   'version' version
	   ('displayname' displayname)?
	   ('description' description)?
	   ('category' category)?
	   (modelReference)*
	   'entity' id ('extends' [entity|qualifiedName])? '{'
	    	(property)*
	   '}'
	;

	enumeration:
	   'namespace' qualifiedName
	   'version' version
	   (modelReference)*
		'enum' id '{'
	    	(enumLiteral)*
		'}'
	;

	enumLiteral: id;

	property:
	    presence ('multiple')? id 'as' type 
	        ('with' '{' propertyAttributes (',' propertyAttributes)* '}')?
	        ('<'constraintType [constraintIntervalType] ("," constraintType [constraintIntervalType])*'>')?
	        (description)?
	;

	propertyAttributes: booleanPropertyAttribute | enumLiteralPropertyAttribute;

	booleanPropertyAttribute: ('readable' | 'writable' | 'eventable') ':' (value ?= 'true' | 'false');

	enumLiteralPropertyAttribute: 'measurementUnit' ':' value;

	type : primitiveType | entity | enumeration | complexPrimitiveType;

	primitiveType : 'string' | 'int' | 'float' | 'boolean' | 'dateTime' |
	    'double' | 'long' | 'short' | 'base64Binary' | 'byte' ;

	complexPrimitiveType : 'Dictionary' ('[' type ',' type ']')?

	presence : 'mandatory' | 'optional';

	constraintType: 'MIN' | 'MAX' | 'STRLEN' | 'REGEX' ;

	constraintIntervalType: int| signedint| float| datetime| string;

	id: '^'?('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

	string:  
	'"' ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|'"') )* '"' |
	"'" ( '\\' ('b'|'t'|'n'|'f'|'r'|'u'|'"'|"'"|'\\') | !('\\'|"'") )* "'"
	;

	date:  
	('0'..'9')('0'..'9')('0'..'9')('0'..'9')('-')('0'..'9')('0'..'9')('-')
	('0'..'9')('0'..'9')  
	;

	time:  
	('0'..'9')('0'..'9')(':')('0'..'9')('0'..'9')(':')('0'..'9')('0'..'9')
	(timezone)?  
	;

	timezone: 'Z'|(('+'|'-')('0'..'9')('0'..'9')(':')('0'..'9')('0'..'9'))  ;

	datetime: date('T')time;

	int: ('0'..'9')+;

	signedint: '-'INT;

	float: int('.'int)?;

	version: int('.' int)*('-'id)?;


<table class="table table-bordered">
	<tbody>
  <tr>
    <td><i class="fa fa-info-circle info-note"></td>
    <td>To find out more about which language elements are available when writing a data type DSL, have a look at the respective DSL Reference. </br>
    Use the keyboard shortcut <span style="font-variant:small-caps;">Ctrl + Space</span> to get a list of available DSL elements, that can be applied in the current scope you are in.</td>
  </tr>
	</tbody>
</table>
