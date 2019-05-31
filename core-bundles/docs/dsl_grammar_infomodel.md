## Information Model DSL Grammar Reference

    InformationModel:
        'vortolang' 1.0
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