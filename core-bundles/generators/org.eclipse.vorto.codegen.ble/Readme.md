# Bluetooth Generator Model

This plugin provides a meta-model into which a Vorto model can be transformed to make generation of Bluetooth LE software easier. It consists of a representation for Device, Service and Characteristics. The plugin also includes its own version of an InvocationContext which needs to be instantiated in the code generator.

An example is shown below, where a Bluetooth 'device' is created based on an 'infomodel' with mappings for the targetplatform 'blegatt'. Secondly an InvocationContext is created which includes the newly created device.
```
var Device device = new ModelTransformer(infomodel, context).transform();
var BleInvocationContext bleContext = new BleInvocationContext(context, device)
```

While the model transformation creates backward references from the BLE model to the Vorto information model, there are no forward references.  Hence it is recommended to base any template for your own BLE generator on the BleGattTemplate provided by this plugin as it provides a couple of convenience functions which will come in handy for your code generator to dervice those references:
```
Service findService(Device device, FunctionblockModel fbm)
Characteristic findCharacteristic(Device device, Property property)
```

----------

List of other available [Code Generators](../Readme.md).
