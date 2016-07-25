---
layout: documentation
title: Vorto CLI Tool
---
{% include base.html %}

##Vorto **C**ommand **L**ine **I**nterface (CLI) Tool##

[Overview](#overview)

[Basic Structure](#basic-structure)

[Installation](#installation)

[CLI Commands](#cli-commands)


## Overview

Vorto Command Line Interface (CLI) tool is REST-Client that allows you, to access your specified repository with all Vorto related functionalities.

## Basic Structure

The CLI-Tool is based on a simple structure.

It consists of a Command Line Interface as well as a **.yaml** file, that stores all necessary configuration properties like _Username_, _Password_, _Proxy_ and _Repository_.

## Installation

First of all, you have to download the CLI Tool from the <a href="{{ base }}/downloads/index.html">download</a> page and save it to any working directory.

A very convenient way to use the CLI Tool is, to add the path of the directory into the system's environment PATH variable. Once you did this, you can call the CLI-Tool from any directory.

For Windows,
Open Control Panel -> Advanced System Settings -> Advanced -> Environment Variables 

![Environment Variable]({{base}}/img/documentation/cli_evnmt_variable.jpg)

For Mac/Linux, from Terminal window execute,

	export path=%path%:~/Applications/<<vorto cli tool installed directory>>


Before you can use the CLI Tool as a REST Client, you need to initialize basic preferences. 

By default the preferences are stored in `<<home directory>>/.vortocli.yaml.file`, which is located in your home directory.
For Windows,

	`C:\Users\{$username}\.vortocli.yaml.file`

For Mac, 

	`/Users/{$username}/Documents/.vortocli.yaml.file`

For Linux, 

	`/usr/{$username}/Documents/.vortocli.yaml.file`


You have to setup vorto via **vorto config**. If you want to initialize vorto with additional values (or change them afterwards) like _-username_, _-password_, _-proxy_ and _-repo_, then you can extend the **vorto config** command with four additional flag parameters _-username_, _-password_, _-proxy_ and _-repo_, or you can edit the _.vortocli.yaml_ file via text editor. 

	vorto config -username vortouser -password password -repo http://vorto.eclipse.org

Congratulations! - Setup is done and you are now able to use the Vorto CLI Tool.

![CLI Yaml file]({{base}}/img/documentation/yamlfile.jpg)

<table class="table table-bordered">
<tbody>
  <tr>
    <td><i class="fa fa-info-circle info-note"></td>
    <td>Enable the proxy in the configuration file _.vortocli.yaml_ (Line 4) if you are accessing internet via proxy server. </td>
  </tr>
 </tbody>
</table>


## CLI Commands

Now let's continue how you can really gain a benefit from the CLI Tool.

The Vorto CLI Tool provides a scope of seven different Vorto Commands to interact with your specified Repository. 

Each of them is based on a similar structure and offers a unique functionality.

    vorto [command] [-flag1 parameter1 -flag2 parameter2 ... ]

<table class="table table-bordered">
  <thead>
  <tr>
    <th>Command</th>
    <th>Description</th>
    <th>Example</th>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td>vorto config</td>
    <td>With the vorto config command you do the basic CLI Tool setup.<br>Additional flag parameters:<br><ul><li>username - vorto username credentials</li><li>password - vorto password credentials</li><li>proxy- network proxy</li><li>repo - specify repository (Default: http://vorto.eclipse.org)</li></ul></td>
    <td>
	For (Windows/Mac/Linux),<br/>
	<b>vorto config -username vortouser -password password -repo http://vorto.eclipse.org</b></td>
   </tr>
   <tr>
    <td>vorto help</td>
    <td>To get more familiar with the CLI Tool commands, it's a good approach to call the <b>vorto help</b> command.<br><b>vorto help</b> lists you all other possible commands, including flag parameters and examples.</td>
    <td>For (Windows/Mac/Linux),<br/>
	<b>vorto help</b></td>
   </tr>
   <tr>
    <td>vorto query</td>
    <td>After everything is successfully setup, you are able to access the Repository.<br> Via <b>vorto query</b> you can query all models of your Repository.<br>The result of this command contains all models in the Repository.</td>
    <td>
	For (Windows/Mac/Linux),<br/>
		eg.1, <b>vorto query "*"</b><br/>
		eg.2, <b>vorto query "*functionblocks*"</b></td>
   </tr>
    <tr>
    <td>vorto info</td>
    <td>If you are now interested in a certain model and you want to get more information from this specific model, you can use vorto info to get it.<br> The result of this command is a detailed view of the requested model.</td>
    <td>
	For (Windows/Mac/Linux),<br/>
	<b>vorto info examples.informationmodels.TI_SensorTag:1.0.0</b></td>
   </tr>
   <tr>
    <td>vorto download</td>
    <td>Furthermore you can start working with a certain model locally, when you download it via <b>vorto download</b>.<br> To open and to work with it, you can use any text editor to modify this certain model and you can even upload it again.<br>Via the flag -outputPath can you specify a download directory.<br>Additional flag parameters:<br><ul><li>output - (Default: DSL)</li><li>outputPath - path to store the download (Default: current directory)</li><li>includeDependencies - whether to include dependencies or not (Default: false)</li></ul></td>
    <td>
		For Windows, <br/>

		<b>vorto download examples.informationmodels.TI_SensorTag:1.0.0  -outputPath D:\models</b>
		<br/><br/>
		For Mac/Linux, <br/>
		<b>vorto download examples.informationmodels.TI_SensorTag:1.0.0  -outputPath /Users/{$username}/Documents/models/</b>
	</td>

   </tr>
   <tr>
    <td>vorto generators</td>
    <td>If you want to generate platform specific code, you can get a short overview of all active code generators via <b>vorto generators</b>.</td>
    <td>
	For (Windows/Mac/Linux),<br/>
	<b>vorto generators</b></td>
   </tr>
   <tr>
    <td>vorto generate</td>
    <td>Afterwards you can generate <b>vorto generate</b> your platform specific code (specified by -generatorKey) in a very convenient way.<br>Additional flag parameters:<br><ul><li>generatorKey</li><li>outputPath</li></ul></td>
    <td>
	For Windows, <br/>	
	<b>vorto generate examples.informationmodels.TI_SensorTag:1.0.0 -generatorKey mqtt -outputPath D:\models</b>
	<br/><br/>
	For Mac/Linux, <br/>
	<b>vorto generate examples.informationmodels.TI_SensorTag:1.0.0 -generatorKey mqtt -outputPath /Users/{$username}/Documents/models//b>
	</td>
   </tr>
    <tr>
    <td>vorto share</td>
    <td>Additionally to all this access functionality, you are also able to share your models.<br>Therefore you either have to deposit your credentials in the .vortocli.yaml file or you have to pass them as additional parameters.<br>
    Additional flag parameters:<br><ul><li>username - vorto username credentials (Default: value in .vortocli.yaml)</li><li>password - vorto password credentials (Default: value in .vortocli.yaml)</li></ul></td>
    <td>
	For Windows,<br/>
	<b>vorto share D:\Models\TI_SensorTag_CC2650\src\models\TI_SensorTag.infomodel
	</b>
	<br/>
	<b>vorto share D:\Models\TI_SensorTag_CC2650\src\models\TI_SensorTag.infomodel -username vortouser -password password</b>
	<br/><br/>
	For Mac/Linux,<br/>
	<b>vorto share /Users/{$username}/Documents/models/TI_SensorTag.infomodel
	</b>
	<br/><br/>
	<b>vorto share /Users/{$username}/Documents/models/TI_SensorTag.infomodel -username vortouser -password password</b></td>
   </tr>
  </tbody>
</table>