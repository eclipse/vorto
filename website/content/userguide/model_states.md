---
menu:
  main:
    parent: 'User Guide'
date: 2016-03-09T20:08:11+01:00
title: Vorto Model States
weight: 102
---
Vorto Models have a lifecycle and undergo various states. This section gives you an overview of these states and the actions that are possible. 
<!--more-->

## Overview

<figure class="screenshot">
	<img src="/images/documentation/model_states.png">
</figure> 

The following table gives an overview of what actions are possible for models in a specific state:

### Draft
A model in Draft state is a model under construction, here people can try out their ideas and test their behaviour. 

### Review
As soon as a developer is happy with the state a model is in, he can request a Review. Then a model supervisor will go through and confirm that all model guidlines are followed. The supervisor can either reject it, or allow it to go on to the Released state.
To request a review all dependent models have to be released.

### Released
A released model, is a model one can build upon. The release state ensures there are not changes introduced which could break comability, if there is a requirement for change a new version of the model should be created and the old one deprecated. 

### Deprecated
If a model is not up to date anymore and it already has been released, it can be deprecated to mark the broken state. A deprecated model signals to other users that this model should not be used anymore and if possible replaced by a newer version.

