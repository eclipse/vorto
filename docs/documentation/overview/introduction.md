---
layout: documentation
title: Introduction
---

{% include base.html %}

## Introduction

This section details the following topics:

[Overview](#overview)

[Features](#features)

[System Requirements](#system-requirements)

## Overview

Vorto is an open source tool that allows for creating and managing technology agnostic, abstract device descriptions, so called information models. Information models describe the attributes and the capabilities of real world devices. These information models can be managed and shared within the Vorto repository. In addition Vorto provides a code generator extension point where code generators can be plugged in.

Standardization organizations and industry consortia work hard on device abstraction related standards. Some of them are domain specific, some are very generic, and all of them are useful for a large number of use cases. However in most cases there is no tooling available that allows for creating and managing standard conform device representations. It is also the case that many standards are very complex and it is not easy to validate existing abstract representations of devices against the standard.

The Vorto project is an approach to leverage the standardization of so called Information Models. Information models are abstract representations of real world devices following a meta information model which is also part of the project. The meta information model shall be very flexible and easy to use. In addition, the project scope includes an eclipse based toolset that allows for creating information models, a repository for finding, managing and sharing information models, and last but not least a set of code generators that allow for the creation of information model based code artifacts to be employed in specific solutions.

![More about Vorto]({{base}}/img/documentation/vorto_eclipse_overview_L.png)


## Features

Vorto plug-ins provide the following features:

**Datatype (Entity/Enum) wizard**  
 Vorto provides an Eclipse wizard to create a new data type (Entity or Enum). You can use auto-completion and validation to update data types.

**Function block model wizard**  
Vorto provides an Eclipse project wizard to create a function block model project containing all necessary resources.

**Information model wizard**  
Information models represent the capabilities of a particular type of device in its entirety. An information model contains one or more function blocks. Vorto provides an UI wizard to create an information model project and all required resources.


**Mapping model wizard**  
Vorto provides an Eclipse project wizard to create a mapping model from existing Datatype, Function block, or Information models.

**DSL editors**  
The new Domain-specific Language (DSL) is a simple way to define and read datatype models, function block models and information models without the need to understand XML, XSD or even Java. On-the-fly auto-completion and validation lets you create the named models even faster than ever.

Datatype model DSL editor:  

![Datatype model DSL editor]({{base}}/img/documentation/m2m_tc_datatype_model_dsl_editor.png)

Function block model DSL editor:  

![Function block model DSL editor]({{base}}/img/documentation/m2m_tc_function_block_model_dsl_editor.png)

Information model DSL editor:

![Information model DSL editor]({{base}}/img/documentation/m2m_tc_information_model_dsl_editor.png)

Mapping model DSL editor:

Vorto provides Mapping DSL editor for user to create customized mapping that map a vorto model to another platform model.

**Vorto perspective**  
The Vorto perspective as a new Eclipse perspective simplifies the view and, thus, the work with the more abstract models.

![Vorto Perspective]({{base}}/img/documentation/m2m_tc_vorto_perspective.png)


**Example code generators**  
Vorto provides several code generators, so that the user can generate code based on the select information model. Vorto also provides a code generator extension point, so that the user can create his own code generator.

![Generating function block model]({{base}}/img/documentation/m2m_vorto_code_generator_menu.png)

##System Requirements

For the use of the Vorto plug-ins the following software requirements must be met:

- IDE
  - Eclipse (Luna or higher)
  - Eclipse [Xtext plug-in](http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/) 2.7.\* (Xtext Redestributable)
- [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/)  

<table class="table table-bordered">
<tbody>
  <tr>
    <td><i class="fa fa-info-circle info-note"></td>
    <td>Validity period will endure at the most as long as the version of infrastructure software of third party manufacturers defined in this document (Operating Systems, Java, etc.) is publicly and officially supported. We support the most recent patch releases of the respective software product version.</td>
  </tr>
 </tbody>
</table>
