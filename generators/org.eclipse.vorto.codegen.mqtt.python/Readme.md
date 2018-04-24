# Python Code Generator for MQTT

## Introduction
This code generator allows for easy implementation of a device sending telemetry data to an MQTT broker.
It is based on the Eclipse Paho MQTT client for Python.

## Prerequisites

* Python 3.x
* Eclipse Paho Python
 * Install by running `pip install paho-mqtt`

## Sample Code

The generator outputs a sample application which uses the Vorto information model objects. You will need to modify a couple settings to make it work for your usecase, such as
* Device identity: you might only want to change the prefix, but potentially also the complete name
* MQTT broker
* Certificates and security settings

----------

List of other available [Code Generators](../Readme.md).
