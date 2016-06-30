---
layout: documentation
title: Sharing Models Using the Web Interface
---
{% include base.html %}

##Sharing Models Using the Web Interface

You can share models (information models, function block models or data type models) by uploading them to the Vorto Repository within the Vorto Repository Web interface.

For sharing models using the Eclipse Vorto Toolset, refer to [Sharing Models Using the Eclipse Vorto Toolset](../eclipse-vorto-integration/share-eclipse.html).  
For sharing models using the Vorto CLI tool, refer to [Vorto CLI Tool]({{base}}/documentation/cli-tool/cli-tool.html).

**Prerequisites**

You are logged in to the Vorto Repository (refer to [Login](/login-register.html)).

**Proceed as follows**

1. In the menu bar, click **Share**.  
   The Upload page opens.  
   ![Share Web Interface]({{base}}/img/documentation/vorto_repository_share.png)
2. Click the **Choose File** button.  
   The operating system's file browser opens.
3. Browse to the model file (or zip file containing multiple models) you want to share and click the **Open** button.  
   The file name is taken over to the **Upload** entry field.
4. Click the **Upload** button to upload the selected file.  
   The file(s) are checked. The content must correlate with the Vorto DSL guidelines. In addition, the dependencies of the files, if applicable, must be correct.  
5. If the file(s) is (are) invalid, the **Upload Results** are displayed with an error message; the following image shows an invalid bulk upload.  
   ![Share Web Interface Upload Error]({{base}}/img/documentation/vorto_repository_share_bulk_upload_error.png)  
   Check and revise your model file(s) and restart the sharing procedure.
6. If the file(s) is (are) valid, the **Upload Results** are displayed with an upload confirmation message.  
   ![Share Web Interface Upload]({{base}}/img/documentation/vorto_repository_share_upload.png)  

7. Click the **Checkin** button.  
   The model(s) is (are) checked in to the Vorto Repository and the **Upload Results** are displayed with a check in confirmation message.  
   ![Share Web Interface Checkin]({{base}}/img/documentation/vorto_repository_share_checkin.png)
