---
layout: documentation
title: Search models in the repository
---
{% include base.html %}

## Search models in the repository

The user can search for the existing models in the repository and use this shared model as reference in a local model project.

There are 2 ways of accessing vorto repository.

[1. Model Repository page](#model-repository-page)

[2. From Vorto perspective](#vorto-perspective)

## Model Repository page

**Prerequisite**

<a href="http://vorto.eclipse.org/repo" target="_blank">Open the model repository.</a>

**Steps**

- Search for a function block e.g. **Accelerometer**

![Repo Search]({{base}}/img/documentation/vorto_repo_search.jpg)

- Click <img src="{{$base}}/img/documentation/repo_details_icon.jpg"> to get more information on the selected model. Details of the selected model gets displayed as shown below

**References** Displays the function block model or datatype that the selected model is referencing to

**Used by** Displays the Information model(s) or Function block model(s) using the selected model

![Repo details main]({{base}}/img/documentation/repo_details_main.jpg)


- Choosing either one of the values in _References_ or _Used by_ should open more details about the selected device.

- Under _Other formats_ select the format of your choice. This translates the information model to the specific format, e.g. XMI, DSL or Markdown.

### Discussions

Users can comment on the selected model from the **Discussions** tab

![Disscuss]({{base}}/img/documentation/vorto_repo_discuss.jpg)


### Image for information model

The user can upload a suitable image for the information model by clicking on the space available for image.

![Add Image]({{base}}/img/documentation/addimage_repo.jpg)

### Generate code from model repository

Choose one of the existing generators to generate the souce code. Save the _*.zip_ to your desired directory and import it in your eclipse workspace.

![Generators]({{base}}/img/documentation/generators.jpg)

## Vorto Perspective

More information on how to use the vorto repository from the vorto prespective can be found here
[Model Repository Blog post]({{base}}/blog/2015-09-04-VortoIntegration.html)

### Generate code from vorto perspective

Right-click on the selected model in the model repository and click Generate code, to generate the source code.

![Generate_vorto_persp]({{base}}/img/documentation/generate_vorto_persp.jpg)

Choose one of the existing generators to generate the souce code. The source code should be available in the java perspective.

![Generators_vorto_persp]({{base}}/img/documentation/generators_vorto_persp.jpg)