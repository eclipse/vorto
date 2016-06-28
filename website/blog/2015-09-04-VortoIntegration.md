---
layout: blog
title: Share & reuse models with the Vorto Repository
date:  "2015-09-04"
sequence: 2
section: blog
author: Mantos Erle Czar
githubid: erlemantos
videourl: "https://www.youtube.com/watch?v=aKnW2udh9m8&width=1500&height=1000"
imagename: "VortoRepoVideoImage.jpg"
imgtitle: Share & reuse models with the Vorto Repository
style: "thumb4"
---
{% include base.html %}
In the current development branch, the following functionalities have been added to the Vorto toolset to enable developers to seamlessly integrate with the **Model Repository**:

* Search models in the repository
* Share models to the repository
* Use a shared model as reference in a local model project
* Import a shared model to the local workspace
* Eclipse preferences for the URL, username and password of the **Model Repository**.
* [Information Model Repository](http://vorto.eclipse.org/repo){:target="_blank"}

<!--more-->

[Click here to watch a demo video](#demo-video)

## Preparation

Before we can use the toolset integration with the Model Repository, we must add the URL of the Model Repository in the preferences. We must also add the username and password if we want to be able to save models to the repository.

### Setting the preferences

1. Go to *Window > Preferences* then choose *Vorto > Remote Repository*
2. Enter the URL of the Model Repository
3. Optionally, you can also enter the username and password if you want to save models to the repository.

![Preferences]({{$base}}/img/blogpics/Preferences.png)

## Adding the Model Repository view to the workspace

There are two ways to add the **Model Repository** view. You can either switch to the **Vorto Perspective**, or you can manually add the **Model Repository** view to the workspace.

### Switching to the Vorto perspective

Click the Vorto perspective button on the upper right corner. When the perspective change is completed, you'll see the **Model Repository** view on the bottom right corner along with the **Console**

![VortoPerspective]({{$base}}/img/blogpics/VortoPerspective.png)

### Manually adding the Model Repository view to the workspace

1. Go to *Window > Show View > Other*.
2. Look for *Vorto*, click on *Model Repository*

![ShowView]({{$base}}/img/blogpics/ShowView.png)

## Searching for models

Searching for models is as simple as typing a filter and clicking the **Search** button. Without a filter, **Search** will return *ALL* models in the **Model Repository**.

![ModelRepositoryView]({{$base}}/img/blogpics/ModelRepositoryView.png)

## Sharing models

After you have created your model, you can open the context menu of the project (by right clicking on it) to see the *Share* button. This require credentials to be entered in the Vorto preferences first.

![SavingModel]({{$base}}/img/blogpics/SavingModel.png)

## Using shared models as references

Dragging a shared model from the **Model Repository** view and dropping it to a local model project will add the shared model to the list of references of the local model project. It will also download the shared model from the **Model Repository** to the **shared folder** of the local model project.

![SharedModelsReferences]({{$base}}/img/blogpics/SharedModelsReferences.png)

## Importing a shared model to the local workspace

Dragging a shared model from the **Model Repository** view and dropping it to one of the three model views (**Datatype Models**, **Function Block Models**, **Information Models**), and not to any particular local model project will import the shared model as a project to the local workspace.

![SharedModelImport]({{$base}}/img/blogpics/SharedModelImport.png)

## Demo Video
