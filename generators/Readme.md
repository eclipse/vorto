# Vorto Code Generators

Vorto Code Generators convert Information Model into platform-specific source code, that runs on the device and integrates with the specific IoT backend. 

The following Code Generators are currently supported:

 - [Bosch IoT Suite Generator](./generator-boschiotsuite/Readme.md) (Key: boschiotsuite)
 - [Eclipse Ditto](./generator-eclipseditto/Readme.md) (Key: eclipseditto)
 - [Eclipse Hono](./generator-eclipsehono/Readme.md) (Key: eclipsehono)

Invoke Generator via Curl:

	curl -GET http://vorto.eclipse.org/api/v1/generators/[Key]/models/[Model ID]


##

For more Vorto Generator examples, visit [Vorto-Examples](https://github.com/eclipse/vorto-examples)
