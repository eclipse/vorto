---
layout: documentation
title: Using REST API
---
{% include base.html %}

##Using REST API

REST API allows to communicate with the Vorto Repostiory by means of REST calls.  

**Prerequisites**

You have opened the Vorto Repository Web interface (refer to [Opening the Vorto Repository Web Interface](#opening-the-vorto-repository-web-interface)).

**Proceed as follows**

1. In the menu bar, click **REST API**.  
   The **REST API** page opens.  
   ![Rest API]({{base}}/img/documentation/vorto_repository_rest_api.png)  
   The calls are combined in three groups:
   * model-generation-controller: REST API to controll Code Generator
   * model-repository-controller: REST API to manage Models
   * share-model-controller: REST API to upload Models
2. To expand a group, click the group name or the **open/hide** link.  
   The group expands so all calls combined in it are displayed.  
   ![Rest API Expand Group]({{base}}/img/documentation/vorto_repository_rest_api_expanded_group.png)
3. To expand a call, click the call name.  
   The call expands so all its parameters are displayed.  
   ![Rest API Expand Call]({{base}}/img/documentation/vorto_repository_rest_api_expanded_call.png)
4. Fill in all necessary parameters and click **try it out!** button.  
   ![REST API Enter Parameters]({{base}}/img/documentation/vorto_repository_rest_api_enter_parameters.png)  
   The call is processed and the response is displayed below the **try it out!** button.
   ![REST API Response]({{base}}/img/documentation/vorto_repository_rest_api_response.png)  

