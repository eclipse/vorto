---
layout: documentation
title: Share information models to the repository
---
{% include base.html %}

## Vorto Repository ##

[Overview](#overview)

[Search models](#search-models-in-the-repository)

[Share models to the repository](#share-models-to-the-repository)


## Overview

The Vorto Repository allows users to upload Data Types, Functionblocks, and Information Models. 

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

## Share models to the repository

[Prerequisites](#prerequisites)

[Share information models](#share-information-models)

In order to share a model to the repository, the user is required to Login to the Vorto Repository.

## **Prerequisites**

Users new to Vorto are required to **Sign-Up**, by providing values to all the mandatory fields and **Register**.

Key in values in mandatory fields

![Registration Screen]({{base}}/img/documentation/vortoregistration.jpg)

The following screen is displayed after successful registration.

![Sign-Up Confirmation]({{base}}/img/documentation/vorto_reposignup.jpg)

The user should also receive a confirmation email notification regarding the Vorto registration.

## **Share Information models**

Information models can be shared in three different ways

[1. Vorto Web Repository](#vorto-web-repository)

[2. Eclipse pllugin](#eclipse-plugin)

[3. CLI tool](#cli-tool)


#### **1. Vorto Web Repository**

Existing Vorto users can login to Vorto repository. After logging in, the users can either search or share models to the repository.

Here is a step-by-step instruction to share models to the repository:

1. Choose **Share** from the menu
2. **Browse** for the information model/datatype/function block model file 
3. The properties of the selected file will be displayed in the screen
4. Click **Upload** to upload the desired file.
5. The file can be uploaded successfully, if the file content correlates with the Vorto DSL guidelines
![Upload File]({{base}}/img/documentation/uploadfile_torepo.jpg)
6. The details of the uploaded file will be displayed as shown below:
![Upload success]({{base}}/img/documentation/upload_success.jpg)

#### **2. Eclipse Plugin**

Information models can be shared using eclipse plugin.
Key in the remote repository details using _Eclipse - Preferences - Vorto_
![Eclipse preference]({{base}}/img/documentation/eclipse_pref.jpg)

Right-click directly from the vorto perspective to share the models

![Share from Prespective]({{base}}/img/documentation/share_presp.jpg)

#### **3. CLI tool**

<a href="{{base}}/documentation/cli-tool/cli-tool.html"> Find more information on how to use the CLI tool</a>