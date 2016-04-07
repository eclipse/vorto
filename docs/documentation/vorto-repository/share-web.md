---
layout: documentation
title: Share information models to the repository
---
{% include base.html %}

## Share models to the repository

In order to share a model to the repository, the user is required to <a href="{{base}}/documentation/vorto-repository/userregistration.html"> Login</a> to the Vorto Repository.

Information models can be shared in three different ways

[1. Vorto Web Repository](#vorto-web-repository)

[2. Eclipse plugin](#eclipse-plugin)

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