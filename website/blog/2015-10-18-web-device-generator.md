---
layout: blog
title: Web Application for Devices in < 2 minutes
date:   2015-10-18
section: blog
imagename: "WebDeviceGenerator.png"
imgtitle: Web Application for Devices in < 2 minutes
videourl: "https://youtu.be/zUJK2wWXIpo&width=1500&height=1000"
author: Nagavijay Sivakumar
githubid: nagavijays
style: "thumb5"
---
{% include base.html %}

Sounds unrealistic? Not really, though!! Here is a quickest way to create a web interface for your physical devices
using Vorto. Here are the list of ingredients you need before you develop it.

<!--more-->

### Prerequisites:
- [Eclipse Luna] or higher
- [Vorto Plugins]
- [Maven 3.x]  (Not required if bundled with Eclipse)

Lets say you want to build a "Weather Station" which measures humidity and temperature. Follow the simple steps to create a web interface using Vorto.


<div class="blogstep">
  <div class="blogpicture">
  <a href="https://youtu.be/HUljUdg4PpU&width=1500&height=1000" rel="prettyPhoto" title="Create a Function Block">
    <img alt="Create Function Block" class="noshadow" src="{{$base}}/img/blogpics/CreateFunctionBlock.gif" width="380" alt="Create a Function Block" >
  </a>
  </div>
  <div class="blogtext">
    <strong>Step 1: Create & describe attributes and behaviours of your Temperature and Humidity sensors
    using <a href="{{base}}/documentation/editors/functionblock.html#defining-a-function-block">function blocks.</a></strong>
    <p>
    (Hint: you can modularize and make it reusuable by using <a href="{{base}}/documentation/editors/datatype.html">entites</a>)
    </p>
  </div>
  <div class="clear">
  </div>
</div>

<div class="blogstep">
  <div class="blogtext">
    <strong>Step 2: Represent your device "WeatherStation" by creating an
    <a href="{{base}}/documentation/editors/information-model.html#defining-an-information-model">Information model.</a></strong>
  </div>
  <div class="clear">
  </div>
</div>

<div class="blogstep">
  <div class="blogpicture">
  <a href="https://youtu.be/SsDBns6MBf0&width=1500&height=1000" rel="prettyPhoto" title="Create a Information Model">
    <img alt="Create Information Model" class="noshadow" src="{{$base}}/img/blogpics/InformationModel.gif" width="380" alt="Create a Information Model">
  </a>
  </div>
  <div class="blogtext">
    <strong>Step 3: Refer all your function blocks (temperature & humidity sensor) into your "WeatherStation" information model
    using simple drag and drop into information model.</strong>
  </div>
  <div class="clear">
  </div>
</div>

<div class="blogstep">
  <div class="blogtext">
    <strong>Step 4: Right click on "WeatherStation" -> Generate Code -> Web Device Application Generator.</strong>
  </div>
  <div class="clear">
  </div>
</div>


Done! Your web application for your "Weather Station" is generated. A new web application called "weatherstation-webapp" is created in your workspace. Inorder to quickly test and see your application live, Run maven "jetty:run" from your web application that you have created. To view your web application visit
[Weather Station - Web Application](http://localhost:8080/weatherstation-webapp){:target="_blank"}


Lets explore the generated web application's structure, technology used and customization options.

### User Interface Styling:

The webapplication user inteface is built using [Twitter Bootstrap]  which is almost defacto standard of any web application. You are free to change your application theme or develop on your own. There are plenty of free & commerical bootstrap themes available (e.g [Bootswatch.com], [startbootstrap.com] to name a few) which can be easily replaced by changing the stylesheet in index.html.

### Web Routes & Modularization:
[Angular js] provides REST controllers using angular routes module, which is used for making server calls. It makes use of  html templates for modularizing the web application and provide component behaviours like tabs,navigation etc. Any angular js compatible library can be used in addition to enrich your web application.

### Http Services:
All necessary REST service which is required for server calls is created using Jersey. The rest services are geneated under "service" package and associated models under "model" package, grouped by function blocks and information model. Your functional logic can be writtern in these service classes for appropriate behaviours.

As you can see, how easy and quick it is to generate a web interface to monitor/control your devices using Vorto. Your development time is considerably reduced so that you can focus more on building your functionality.

*You need to use appropriate device library for your your device, inorder to communicate to this webapplication*

To see how web application is created, watch the video  

[Eclipse Luna]: https://www.eclipse.org/
[Vorto Plugins]: https://www.eclipse.org/vorto/index.html
[Maven 3.x]: https://maven.apache.org/
[Twitter Bootstrap]: http://getbootstrap.com/
[Angular js]: https://angularjs.org/
[Bootswatch.com]: https://bootswatch.com/
[startbootstrap.com]: http://startbootstrap.com/
