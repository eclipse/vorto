---
date: 2018-05-28T21:07:13+01:00
title: What is Vorto?
weight: 10
---
![Material Screenshot](/images/Vorto_Ar.png)
	
> Interaction among different stakeholders via Vorto

Vorto provides Web Editors that allow Device Manufacturers to easily describe device functionality and characteristics using a simple Vorto language (Vorto DSL) and save them as Information Models in the [Vorto Repository](http://vorto.eclipse.org/). Platform Providers provide [Code generators](http://vorto.eclipse.org/#/generators) that convert these models into device - specific "stubs" that run on the device and send Information Model compliant messages to IoT Digital Twin services. In order to process these messages in the IoT platform, Vorto offers a set of convenient technical components, for example parsers and validators. For devices sending arbitrary messages, e.g. as JSON or Binary, the Vorto Payload Mapping Engine helps to transform these messages to specific IoT Digital Twin API compliant data, such as Eclipse Ditto or AWS IoT Shadow.

![Material Screenshot](/images/vorto_technicalview.png)

> Technical View
