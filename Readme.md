[![Build Status](https://travis-ci.org/eclipse/vorto.svg?branch=development)](https://travis-ci.org/eclipse/vorto)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/569649bfe2594bedae2cd172e5ee0741)](https://www.codacy.com/app/alexander-edelmann/vorto?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=eclipse/vorto&amp;utm_campaign=Badge_Grade)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=org.eclipse.vorto%3Aserver%3Adevelopment)](https://sonarcloud.io/dashboard?id=org.eclipse.vorto%3Aserver%3Adevelopment ) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/org.eclipse.vorto.parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/org.eclipse.vorto.parent)

# Overview

[Vorto](http://www.eclipse.org/vorto) provides an **Eclipse Toolset** that lets you describe the device functionality and characteristics as **Information Models**. These models are managed in a [Vorto Repository](http://vorto.eclipse.org). [Code generators](http://vorto.eclipse.org/#/generators) convert these models into device - specific "stubs" that run on the device and send Information Model compliant messages to an IoT Backend. In order to process these messages in the IoT backend, Vorto offers a set of **technical components**, for example parsers and validators. For devices sending arbitrary, non-Vorto, messages to an IoT backend, the **Vorto Mapping Engine** helps you to execute device message transformations to IoT Platform specific meta-models, e.g. Eclipse Ditto or AWS IoT Shadow.  

Learn more about how to use Vorto in our [tutorial section](tutorials/Readme.md). 
 
 <img src="./tutorials/images/vorto_overview.png" width="90%"/>


# Getting started with Vorto 

## 1. Install Vorto Toolset

2. Download the [Vorto Eclipse Toolset](https://marketplace.eclipse.org/content/vorto-toolset)
3. Restart Eclipse

## 2. Describe a device with Vorto

Switch to the Vorto perspective and start describing the functionality of a device as an Information Model and Function Blocks.

## 3. Generate Java Code that integrates with Eclipse Hono via MQTT

Integrate the device with Eclipse Hono by sending Vorto Information Model specific data as JSON to the Eclipse Hono MQTT Connector.

1. Right-Click on your Information Model -> **Generate Code** -> Eclipse Hono Generator
2. Choose **Java** confirm with **Generate**. This generates a Maven Java Project and switches to the Java Perspective automatically.
3. Open the generated project and configure it by specifying the Eclipse Hono Sandbox endpoint configuration
	> Please take a look at the [Eclipse Hono Sandbox](https://www.eclipse.org/hono/sandbox/) configuration for more details.
4. Run as a Java Application and verify the incoming data by consuming the Eclipse Hono Sandbox AMQP topic.
	> Please take a look at the [Eclipse Hono Developer Guide](https://www.eclipse.org/hono/dev-guide/java_client_consumer/) for more info

**Hint:** The JSON data, sent by the device is already compliant to Eclipse Ditto. Therefore, you can directly update a Ditto-based thing with the JSON via Web Sockets.
	 > Please take a look at the [Eclipse Ditto Sandbox](https://ditto.eclipse.org/) for more info

# Developer Guide

## Generator SDK

With the **Generator SDK** you can easily create and plug-in a new Vorto Generator. [Read the Tutorial](tutorials/tutorial_create_generator.md)

Here is a list of currently supported [Vorto Generators](http://vorto.eclipse.org/#/generators)

## Repository Client API

Access models and generate code via the [Repository Client API](server/repo/repository-java-client/Readme.md)

## Data Mapping API

Learn more about, how to map arbitrary device data, such as JSON or BLE GATT, to Vorto compliant Eclipse Ditto payload. [Data Mapping API](server/repo/repository-mapping/Readme.md)  

# Documentation

- Read our [tutorials](tutorials/Readme.md)
- Read our [Vorto Documentation](http://www.eclipse.org/vorto/documentation/overview/introduction.html)

# Contact us
 - You want to chat with us ? [![Join the chat at https://gitter.im/eclipse/vorto](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/eclipse/vorto?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
 - You have problems with Vorto ? Open a [GitHub issue](https://github.com/eclipse/vorto/issues)
 - Find out more about the project on our [Vorto Homepage](http://www.eclipse.org/vorto)
 - Reach out to our developers on our [Discussion Forum](http://eclipse.org/forums/eclipse.vorto) 

# Contribute to the Project

Make sure, that you have installed [Vorto for contributors](tutorials/tutorial_vortosetup_contributors.md)

When you create a Pull Request, make sure:

1. You have a valid CLA signed with Eclipse
2. All your commits are signed off (git commit -s)
3. Your commit message contains "Fixes #`<Github issue no>`
4. Target to merge your fix is development branch



