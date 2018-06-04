---
date: 2018-05-28T21:07:13+01:00
title: What is Vorto?
weight: 10
---
![Material Screenshot](/images/Vorto_Ar.png)
	
> Interaction among different stakeholders via Vorto

Vorto provides an Eclipse Toolset that allows Device Manufacturers to easily describe device functionality and characteristics as Information Models. These models are managed in a [Vorto Repository](http://vorto.eclipse.org/). Platform Providers provide [Code generators](http://vorto.eclipse.org/#/generators) that convert these models into device - specific "stubs" that run on the device and send Information Model compliant messages to the platform-specific IoT Backend. In order to process these messages in the IoT backend, Vorto offers a set of convenient technical components, for example parsers and validators. For devices sending arbitrary, non-Vorto, messages to an IoT backend, e.g. in binary format, the Vorto Mapping Engine helps to transform device messages to IoT Platform specific formats, e.g. to Eclipse Ditto or AWS IoT Shadow.

![Material Screenshot](/images/vorto_technicalview.png)

> Technical View
