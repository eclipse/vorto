# Getting Started
When working in an **IoT environment** we have to ensure connected devices can **seamlessly communicate with cloud platforms**, regardless of the different manufacturer, technologies, and systems.

**In the past**, developers have built IoT solutions that were **specifically designed for a limited set of devices** that had the same API. When changing the device type or switching device manufacturers the device integration required time-consuming refactoring to work with the new device.

So the question is, how can we find out **which devices have the type of functionality we want** and how **can we avoid tight coupling** between a specific device so it is easier to switch to a similar device without too much refactoring?

<p align="center">
	<img src="https://www.eclipse.org/vorto/images/vorto.png" width="100px" />
</p>

<p align="center">
<a href="vortolang-1.0.md"><b>Vortolang 1.0 Specification</b></a>
</p>

<br />

## Concepts
Vorto provides an abstraction layer called the **Vorto Information Model**, and a **domain specific language** (DSL) which describes all the meta-information, like properties and functions, of a connected device.
By adding a layer of abstraction, we create a consistent interface that allows us to use different types of devices with similar functionality.

The Vorto project is built on **four main components**:

- **Vorto Language (DSL)**:
The Vorto language is a readable and easily understood domain-specific language that was specifically designed to be usable even by non-developers. It is used to create the abstract models of different devices.  [Read more](vortolang-1.0.md)

- **Metamodel**:
The metamodel is used to define the relationship between the different entities like Information Models, Function Blocks, and Datatypes.

- **Code Generators**:
Based on the DSL and metamodels, the [code generators](../generators) provide a sophisticated but simple way to create source code for a convenient integration of the defined IoT devices with an IoT solution platform.

- **Vorto Repository**:
The repository is used to store, manage, and distribute the created Information Models and Function Blocks for re-use.

For a more in depth introduction into the concepts, feel free to **[read the blog post about Eclipse Vorto](https://blog.bosch-si.com/developer/eclipse-vorto-the-next-step-in-iot-device-integration/).**

<br />

## Learning Path


1. **[Understanding the Vorto Language](vortolang-1.0.md)**

2. **[Create and Manage your namespaces](tutorials/managing_namespaces.md)** to get ready for describing devices.

3. [**Describe your first device**](tutorials/describe_device-in-5min.md) in 5 minutes using the Vorto Web Editor

> You can also describe new devices using the [Vorto Eclipse IDE](tutorials/describe_device_with_eclipse_ide.md)

4. **Provision your device** to [create a new digital twin](tutorials/create_thing.md) inside Bosch IoT Things - [(9min video)](https://www.youtube.com/watch?v=N3IGlCGzJIc)

5. **Integrate your device** using [Python](tutorials/mqtt-python.md) / [Java](tutorials/connect_javadevice.md) / [Arduino](tutorials/connect_esp8266.md) - [(10min video)](https://www.youtube.com/watch?v=QGEXC83JkRc)

6. **Visualize your sensor data** using the [Vorto Dashboard](tutorials/create_webapp_dashboard.md) - [(7min video)](https://www.youtube.com/watch?v=B4HcJVA7NlU)
> You can also use the [Bosch IoT Things SwaggerUI](https://apidocs.bosch-iot-suite.com/?urls.primaryName=Bosch%20IoT%20Things%20-%20API%20v2#/Things) to validate your data if you're using the Bosch IoT Suite.

<br />

If you want to see the full process of creating, integrating and visualizing sensor data for a new device, feel free to watch the webcast [IoT Demo Day Part I: Vorto for Asset Communication Package](https://youtu.be/UA66fJpzNEM). (33min)

---

<br />

### Guides

#### Vorto DSL

- [Vortolang 1.0](vortolang-1.0.md)
- [Describe a TI SensorTag](tutorials/describe_tisensor.md)
- [Describe a device in 5 minutes](tutorials/describe_device-in-5min.md) 
- [Reference Vorto Models files from your custom DSL](https://github.com/eclipse/vorto-examples/blob/master/vorto-dsl-integration/Readme.md) with [Eclipse Xtext](https://www.eclipse.org/xtext)

#### Vorto Repository

- [Create and Manage your Namespaces](tutorials/managing_namespaces.md)
- [Manage Collaborators](tutorials/managing_collaborators.md)
- [Importing your models](../repository/docs/import_model.md)
- [Releasing your models](../repository/docs/model_states.md)
- [Creating a new version of your model](../repository/docs/model_versioning.md)
- [Publishing Models](tutorials/publishing_models.md)
- [Release States](../repository/docs/model_states.md)
- [Official Generators/Plugins](../generators)
  - [Bosch IoT Suite Generator](../generators/generator-boschiotsuite/Readme.md)
  - [Eclipse Ditto](../generators/generator-eclipseditto/Readme.md)
  - [Eclipse Hono](../generators/generator-eclipsehono/Readme.md)
  - [OpenAPI](../generators/generator-openapi/Readme.md)

#### Operators Guide

- [Getting started with running the Repository with docker](../docker/Readme.md)

#### Developer Guide

- [Integrate Vorto with AWS Kinesis for anomaly detection](tutorials/integrate_aws_kinesis.md)
- [Developing Vorto Generator Plugins](../plugin-sdk/Readme.md) for a Code Generation toolchain
- [Parse Vorto DSL files with Java](../utilities/Readme.md)
- Map arbitrary device payload to Vorto compliant data structures
  * [Mapping Engine for Java](../mapping-engine/Readme.md)
  * [Decouple IoT solution from connected sensors](tutorials/create_mapping_pipeline.md)


---

In case you're having difficulties or facing any issues, feel free to [create a new question on StackOverflow](https://stackoverflow.com/questions/ask?tags=eclipse-vorto) and we'll answer it as soon as possible!   
Please make sure to use `eclipse-vorto` as one of the tags. 
