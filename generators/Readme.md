# Vorto Code Generators

Vorto Code Generators convert Information Model into platform-specific source code, that runs on the device and integrates with the specific IoT backend. 

The following Code Generators are currently supported:

 - [Bosch IoT Suite Generator](./generator-boschiotsuite/Readme.md) (Key: boschiotsuite)
 - [Eclipse Ditto](./generator-eclipseditto/Readme.md) (Key: eclipseditto)
 - [Eclipse Hono](./generator-eclipsehono/Readme.md) (Key: eclipsehono)
 - [OpenAPI](./generator-openapi/Readme.md) (Key: openapi)

Invoke Generator via Curl:

	curl -GET https://vorto.eclipseprojects.io/api/v1/generators/[Key]/models/[Model ID]


## Test the Generators locally

You can easily spin up the Vorto Generator plugins as a local micro service in order to test them. [Read here](generator-runner/Readme.md)

## More examples of Generator Plugis

For more Vorto Generator examples, visit [Vorto-Examples](https://github.com/eclipse/vorto-examples)
