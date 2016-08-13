---
layout: documentation
title: Vorto CLI Tool Command Summary
---
{% include base.html %}

##Vorto CLI Tool Command Summary

The Vorto CLI Tool provides a scope of different Vorto Commands to interact with your specified Repository. 
Each of them offers a unique functionality and is based on the following syntax:

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
    <td><code>vorto config</code></td>
    <td>With the vorto config command you do the basic CLI Tool setup.<br>Additional flag parameters:<br>
      <ul>
        <li><code>username</code><br>
          Vorto Repository username credentials</li>
        <li><code>password</code><br>
          Vorto Repository password credentials</li>
        <li><code>proxy</code><br>
          network proxy</li>
        <li><code>repo</code><br>
          Vorto repository URL (default: <code>http://vorto.eclipse.org</code>)</li>
      </ul>
    </td>
    <td>
      For (Windows/Mac/Linux),<br/>
      <p><code>vorto config -username vortouser -password password -repo http://vorto.eclipse.org</code></p>
    </td>
  </tr>
  <tr>
    <td><code>vorto help</code></td>
    <td>To get more familiar with the CLI Tool commands, it's a good approach to call the <code>vorto help</code> command.<br><code>vorto help</code> lists you all other possible commands, including flag parameters and examples.</td>
    <td>For (Windows/Mac/Linux),<br/>
      <p><code>vorto help</code></b>
    </td>
  </tr>
  <tr>
    <td><code>vorto query</code></td>
    <td>After everything is successfully setup, you are able to access the Repository.<br> Via <code>vorto query</code> you can query models of your Repository.
    </td>
    <td>
      For (Windows/Mac/Linux),<br/><br>
      Query all models of your Repository:<br>
      <code>vorto query "*"</code><br><br>
      Query all models containing the string <code>"functionblocks"</code>:<br>
      <code>vorto query "*functionblocks*"</code></td>
   </tr>
    <tr>
    <td><code>vorto info</code></td>
    <td>If you are now interested in a certain model and you want to get more information from this specific model, you can use <code>vorto info</code> to get it.<br>
    The result of this command is a detailed view of the requested model.</td>
    <td>
      For (Windows/Mac/Linux),<br/>
      <code>vorto info examples.informationmodels.TI_SensorTag:1.0.0</code></td>
   </tr>
   <tr>
    <td><code>vorto download</code></td>
    <td>Furthermore you can start working with a certain model locally, when you download it via <code>vorto download</code>.<br>
    To open and to work with it, you can use any text editor to modify this certain model and you can even upload it again.<br>
    Via the flag <code>-outputPath</code> you can specify a download directory.<br>
    Additional flag parameters:<br>
    <ul>
      <li><code>output</code><br>
        output format (default: <code>DSL</code>)</li>
      <li><code>outputPath</code><br>
        path to store the download (default: current directory)</li>
      <li><code>includeDependencies</code><br>
        whether to include dependencies or not (default: <code>false</code>)</li>
    </ul>
    </td>
    <td>
      For Windows,<br/>
      <code>vorto download examples.informationmodels.TI_SensorTag:1.0.0  -outputPath D:\models</code><br/><br/>
      For Mac/Linux,<br/>
      <code>vorto download examples.informationmodels.TI_SensorTag:1.0.0  -outputPath /Users/{$username}/Documents/models/</code>
    </td>
  </tr>
  <tr>
    <td><code>vorto generators</code></td>
    <td>If you want to generate platform specific code, you can get a short overview of all active code generators via <code>vorto generators</code>.
    </td>
    <td>
	For (Windows/Mac/Linux),<br/>
	<code>vorto generators</code>
    </td>
  </tr>
  <tr>
    <td><code>vorto generate</code></td>
    <td>Afterwards you can generate <code>vorto generate</code> your platform specific code (specified by <code>-generatorKey</code>) in a very convenient way.<br>
    Additional flag parameters:<br>
    <ul>
      <li><code>generatorKey</code></li>
      <li><code>outputPath</code></li>
    </ul>
    </td>
    <td>
	For Windows, <br/>	
	<code>vorto generate examples.informationmodels.TI_SensorTag:1.0.0 -generatorKey mqtt -outputPath D:\models</code><br/><br/>
	For Mac/Linux,<br/>
	<code>vorto generate examples.informationmodels.TI_SensorTag:1.0.0 -generatorKey mqtt -outputPath /Users/{$username}/Documents/models</code>
	</td>
  </tr>
  <tr>
    <td><code>vorto share<code></td>
    <td>Additionally to all this access functionality, you are also able to share your models.<br>
    Therefore you either have to deposit your credentials in the <code>.vortocli.yaml</code> file or you have to pass them as additional parameters.<br>
    Additional flag parameters:<br>
    <ul>
      <li><code>username</code><br>
        Vorto Repository username credentials (default: value in <code>.vortocli.yaml</code>)</li>
      <li><code>password</code><br>
        Vorto Repository password credentials (default: value in <code>.vortocli.yaml</code>)</li>
    </ul>
    </td>
    <td>
    For Windows,<br/>
    <code>vorto share D:\Models\TI_SensorTag_CC2650\src\models\TI_SensorTag.infomodel</code><br/>
    <code>vorto share D:\Models\TI_SensorTag_CC2650\src\models\TI_SensorTag.infomodel -username vortouser -password password</code><br/><br/>
    For Mac/Linux,<br/>
    <code>vorto share /Users/{$username}/Documents/models/TI_SensorTag.infomodel</code><br/><br/>
    <code>vorto share /Users/{$username}/Documents/models/TI_SensorTag.infomodel -username vortouser -password password</code>
    </td>
  </tr>
  </tbody>
</table>