# Vorto Generator for the IOS Platform

This generator currently supports the generation of BlueTooth Low Energy specific Swift code in order to bind your device to BLE.

## Getting started

Create an information model mapping:

    **Mapping Example**
	namespace example.mapping
	version 1.0.0
	displayname "MyDevice_IOS"
	description "MyDevice to ios mapping"
	using examples.devices.MyDevice;1.0.0
	infomodelmapping MyDevice_IOS {
		targetplatform ios
		from MyDevice to BLE
	}


