# Mapping Syntax

Once mappings gets more complex, 
for example when we need to map binary device payload to Vorto compliant format, 
we have to write a custom mapping that use converters, like e.g. for our case the binary converter.

By looking at the Rule Syntax for creating Mapping Specifications we can understand the mapping rule flow better.

```js
'from' (ModelElement)* 
'to' (StereoType ('with {' attributes+=Attribute (',' attributes+=Attribute)* '}')?)*
```

- **ModelElement**: Model elements represents attributes, properties of a Vorto model like e.g. Function Blocks and features
- **StereoType**: Stereo Type represents a target platform model type, like e.g. a Condition or Resource. It may even contains zero or many attributes.

Basically what this setup allows us to do is define the Function Block or Information Model and the depth of access to which we want the mapping to take effect.
We have the possibility to assign completely new Function Blocks values or do only very fine-grained changes to single values.   
This would look e.g. like the following code snippet.

```js
from Location.status.latitude to source with { xpath: "/gps/values/lat"}
from Location.status.longitude to source with { xpath: "/gps/values/lon"}
```

<br />

#### LWM2M Example Mapping
Looking at an even more complex and complete mapping for this LWM2M payload, 
we can see how easily readable this Vorto Mapping Specification is.

```java
namespace org.openmobilealliance
version 1.0.0
using com.mycompany.DjiPhantomVision ; 3.0.0-Plus
using com.mycompany.fb.Drone ; 1.0.0
using com.mycompany.fb.Switcher ; 1.0.0
mapping LWM2M {
    from DjiPhantomVision
    to ObjectType with {
        Type : "MODefinition", Name : "Location", 
        ObjectID : "6", ObjectURN : "TBD", 
        MultipleInstances : "Single", Mandatory : "Optional"
    }
    functionblock {
        from Drone.status.location.latitude
        to Resource with { ItemID : "0", Units : "deg" }
        from Drone.status.location.longitude
        to Resource with { ItemID : "1", Units : "deg" }
        from Drone.status.location.altitude
        to Resource with { ItemID : "2", Units : "m" }
         
        from Drone.operation.ascend, 
        Switcher.operation.on to Resource with {ItemID : "0", Operations : "R"}
    }
}
```
