---
layout: documentation
title: System Requirements
---

{% include base.html %}


## System Requirements

For the use of the Vorto Toolset the following software requirements must be met:

- Eclipse
  - [Eclipse IDE for Java and DSL Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-and-dsl-developers/mars2) (Mars or higher)  
    or
  - [Eclipse IDE](http://www.eclipse.org/downloads/packages/release/Mars/2) (Mars or higher)
  - [Xtext](http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/) 2.9.x (or higher)
- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/)

<table class="table table-bordered">
<tbody>
  <tr>
    <td><i class="fa fa-info-circle info-note"></i></td>
    <td>To avoid using a wrong Java version, you can set the required Java version as a preference in the <code>eclipse.ini</code> file:
    <code><br />
      -vmargs<br />
      -Dosgi.requiredJavaVersion=1.8<br />
      ...
    </code></td>
  </tr>
 </tbody>
</table>

<table class="table table-bordered">
<tbody>
  <tr>
    <td><i class="fa fa-info-circle info-note"></i></td>
    <td>Your operating system, Java installation and Eclipse installation must <strong>all</strong> have either 32bit or 64bit architecture!<br />
    If necessary, you can align the path to the correct Java version as a preference in the <code>eclipse.ini</code> file, e.g.:
    <code><br />
      -vm<br />
      C:\Program Files\Java\jdk8\bin\javaw.exe<br />
      ...
    </code></td>
  </tr>
 </tbody>
</table>

<table class="table table-bordered">
<tbody>
  <tr>
    <td><i class="fa fa-info-circle info-note"></i></td>
    <td>Validity period will endure at the most as long as the version of infrastructure software of third party manufacturers defined in this document (Operating Systems, Java, etc.) is publicly and officially supported. We support the most recent patch releases of the respective software product version.</td>
  </tr>
 </tbody>
</table>
