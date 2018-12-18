---
menu:
  main:
    parent: 'User Guide'
title: "Steps to Import Model"
date: 2018-12-02T10:58:37+08:00
weight: 107
---

In this page you will shown how to import an existing model.
<!--more-->

## Prerequisite

The user must be a logged in user to carry out the import function.

## Steps to Import any type of model

* Let's say that the user uses any 3rd party editor and with the DSL documentations comes up with a new model(s) and wants to have it into the repository. He/She needs to do the following two steps

1. Select and upload model(s) for validation
2. Decide to Import the validated model(s)

> The file name extensions could be: _.mapping_, _.infomodel_, _.type_, _.fbmodel_

### Steps to select and upload model(s):

Let's say in our case: function block model with .fbmodel extension

 * Click on the **Import** tab, which leads to the following screenshot.
 
#### Model Selection and Upload
<figure class="screenshot">
    <img src="/images/documentation/model_import_single.png" />
</figure>


 * Choose the **model type** in this case it is _Vorto_
 * Choose the _file_ that needs to be imported by the _browse_ button

> We can also upload many models at once by packaging all the models as a single **.zip** file

 * if every detail is correct, then click **Upload** button
 
#### Validation Response
<figure class="screenshot">
    <img src="/images/documentation/model_upload_validation.png" />
</figure>

### Step to Import the validated model

* if the user has decided to _import_ after the reviewing the validated response, he/she may click on **Import** button.

#### Successful Import Response
<figure class="screenshot">
    <img src="/images/documentation/import_model_success.png" />
</figure>

