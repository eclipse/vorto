---
layout: documentation
title: Introduction
---

{% include base.html %}

## Introduction

This section details the following topics:

[Overview](#overview)

[Features](./features.html)

[Significant Contribution](./contribution.html)

[System Requirements](./system-requirements.html)

## Overview

Vorto is an open source tool that allows for creating and managing technology agnostic, abstract device descriptions, so called information models. Information models describe the attributes and the capabilities of real world devices. These information models can be managed and shared within the Vorto repository. In addition Vorto provides a code generator extension point where code generators can be plugged in.

Standardization organizations and industry consortia work hard on device abstraction related standards. Some of them are domain specific, some are very generic, and all of them are useful for a large number of use cases. However in most cases there is no tooling available that allows for creating and managing standard conform device representations. It is also the case that many standards are very complex and it is not easy to validate existing abstract representations of devices against the standard.

The Vorto project is an approach to leverage the standardization of so called Information Models. Information models are abstract representations of real world devices following a meta information model which is also part of the project. The meta information model shall be very flexible and easy to use. In addition, the project scope includes an eclipse based toolset that allows for creating information models, a repository for finding, managing and sharing information models, and last but not least a set of code generators that allow for the creation of information model based code artifacts to be employed in specific solutions.

![More about Vorto]({{base}}/img/documentation/vorto_eclipse_overview_L.png)
