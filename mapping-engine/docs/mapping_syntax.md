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

<br />

## Comparing Payloads
To understand the difference of the normalized, mapped payload and the target platform specific ones a little bit better, let's compare the normalized Vorto payload coming out of the Mapping Engine with two target platform specific payloads that can be used with the platforms.

We will be using the [RaspberryPiTutorial Information Model](https://vorto.eclipse.org/#/details/org.eclipse.vorto.tutorials:RaspberryPiTutorial:1.0.0) as an example.
It has 3 features, temperature, battery, and location.     
In the **normalized Vorto format**, the location feature, e.g. looks like this.

```json
{
  "location": {
    "status": {
      "latitude": 0,
      "longitude": 0
    }
  }
}
```

It is very minimalistic and only contains the status value for the location feature.

When looking at the **Eclipse Ditto specific format** of the exact same feature, we can see that we also have a reference to the according Function Block of the Vorto Repository.

```json
{
  "location": {
      "definition": [
        "org.eclipse.vorto:Location:1.0.0"
      ],
      "properties": {
        "status": {
          "latitude": 0,
          "longitude": 0
        },
        "configuration": {}
      }
  }
}
```

In addition to the reference we also see another level of attributes, the configuration.

To contrast the Eclipse Ditto specific payload, let's look at the **payload of the AWS IoT Service**. The format looks quite different in terms of naming.

```json
{
  "state": {
    "reported": {
      "location": {
        "latitude": 0,
        "longitude": 0
      }
    }
  },
  "metadata": {
    "reported": {
      "location": {
        "latitude": {
          "timestamp": 1561344504
        },
        "longitude": {
          "timestamp": 1561344504
        }
      }
    }
  },
  "version": 2,
  "timestamp": 1561344697
}
```

However when looking at the core element, which is the *reported state*, we can see that it simply holds the latitude and longitude values mapped to the location attribute.
All other attributes like the *metadata*, *version*, and *timestamps* have been added by AWS IoT.
