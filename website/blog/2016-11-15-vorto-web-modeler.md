---
layout: blog
title: Vorto Web System Modeler
date:   "2016-11-15"
sequence: 3
section: blog
author: Nagavijay Sivakumar
githubid: nagavijays
---
{% include base.html %}

Have you struggled building IoT solution for your business or customers or just as a hobby for yourself by 
working with myriad technologies, platforms, APIs, devices etc? Whether it’s a proof of concept or building real
complex IoT solution, were you set off by integrating with heterogeneous technologies/systems?

<!--more-->

### Introducing
Vorto Visual Web Modeling Tool is an attempt to solve the problems by abstracting 
nitty-gritty details about getting you to know upfront about any specific technologies/platforms/devices 
for building your IoT solution. Before getting into further details, were you aware of [Vorto](http://www.eclipse.org/vorto/) 
and [Vorto's philosophy](http://www.eclipse.org/vorto/overview/vorto_overview.html)? 
If not, please visit the link and read/watch through lot of information available in the Vorto website. I shall wait until you read and come back here.

Hmmm.... So, now I believe you understand what is Vorto is all about. If you are wondering that Vorto is mainly focuses on solution developers, platform vendors and device manufacturers and it serves the purpose. But wait, there are students, Hobbyists, Domain experts and embedded developers who have different backgrounds and different expertise when it comes to using the technology or developing IoT applications.

### Web.. Web.. Web.. Everywhere...

How to address these issues? What are the common skills that exist with all these different group of people. What’s the easiest way that everyone can understand, use and master it easily?
Vorto Visual Web Modeling Tool is a genuine attempt to answer some of these questions.

Here are the features of Vorto Web Modeler Dashboard,

- Graphically design IoT applications with simple Drag and Drop elements.
- Export/Import the IoT design diagram
- Export the IoT solution metadata model definition.

Developer features,

- Declaratively add or configure IoT Elements.
- Categorize IoT elements and configure Input/Output nodes.


### Don't talk ... Show me...

Here are some examples to design IoT solution in visual way using browser. 
The examples show how to design simple IoT applications. 

1) Sensor monitoring system from smartphone

<img alt="Sensor monitoring system from smartphone" class="noshadow" src="{{$base}}/img/blogpics/VortoWebPhone.gif" width="380" alt="Sensor monitoring system from smartphone">


2) Autonomous controlling of Air conditioner based on temperature.

<img alt="Autonomous controlling of Air conditioner" class="noshadow" src="{{$base}}/img/blogpics/VortoWebAircon.gif" width="380" alt="Autonomous controlling of Air conditioner">



### Background

Just to give you a background, the idea was conceptualized with lot of inspirations from many visual tools for IoT development like ([Node Red](https://nodered.org/), [Bitreactive](http://www.bitreactive.com/), [Octoblu](https://www.octoblu.com/), [AT&T Flow](https://flow.att.com/) etc). 
Most of the tools involve complex to setup, requires lot of learning to understand the designing, 
too much granular level of designing or its just general purpose application development.
Most of the tools are meant to be used as Visual programming tool. 
Whereas Vorto Web Modeler tool stands out by not focussing on visual programming but rather visual modeling.
The idea of the Vorto Web modeler tool is to abstract lot of details and 
make it simple system level modeling without focussing granular too many design details or state machine modeling. 
The modeling sets the foundation to build necessary software building blocks and build IoT solution quickly for different scenarios. 

Currently, the Vorto Web Modeler is in alpha release level and its naive. It lacks integration with Vorto repository or Code generators. Here are the list of features missing,

- Validation of IoT solutions
- Configuration of individual IoT elements
- Generators for overall IoT solution
- Many //@TODO code

### Feedback...
If this topic interests you, I would encourage you to provide feedback or participate in technical discussions in [Vorto Forums](http://eclipse.org/forums/eclipse.vorto).

You can try @ [Vorto Web Modeler demo](https://nagavijays.github.io/vorto-web-modeler/)
The sourcecode can be found [Vorto Web Modeler Source](https://github.com/nagavijays/vorto-web-modeler)
