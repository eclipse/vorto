---
layout: overview
date : "2015-10-20"
section: overview
title: "Vorto use cases"
---
{% include base.html %}

The image below describes the interaction of three stakeholders with Eclipse Vorto: The stakeholders are

* The device manufacturers
* The vendors of IoT platforms
* Solution developers.

All of them have specific use cases that can be supported by the components of Vorto.

<a href="{{ $base}}/img/VortoEclipseOverview.png" rel="prettyPhoto" title="Vorto use cases" >
<img src="{{ $base}}/img/VortoEclipseOverview.png" style="width:50%;height50%;margin-top:0px;" title="Click to view in high resolution"/>
</a>


## Device Manufacturers

Interoperability is one of the most important criteria in the IoT. This is solved using IoT platforms that are able to integrate devices and to provide an infrastructure for interactions. It is crucial for device vendors to enable platform vendors to integrate their devices without major effort. A device that can be integrated on various platforms can be used in various scenarios which helps to increase the sales of the specific device vendor. The Vorto IoT toolset allows to comfortably create abstract, technology agnostic descriptions of devices. These descriptions are machine readable and thus can be transformed into formats that are required for an integration into a specific platform. By providing such device descriptions a device vendor allows an easy integration of devices into platforms for which Vorto code generators exist.


## Platform Vendors

There is a large variety of smart devices available in the market. A customer doesn't want to be restricted to devices of specific vendors because the platform, his IoT environment is based on, doesn't support others. This is the reason why platform vendors would like to integrate as many devices as possible on their platform. It is very easy to create a platform specific Vorto code generator which allows to transform Vorto device descriptions (so called information models) into formats that are required for an integration into a specific platform. After such a code generator is implemented it is very easy to integrate devices using Information Models that are available within the Vorto repository.


## Solution Developers

Solution developers that integrate devices into specific platforms often have to write a lot of code that could be generated using information about the corresponding device. The Vorto code generator infrastructure allows to do that which results in a significant reduction of development efforts. 