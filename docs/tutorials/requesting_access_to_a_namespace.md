# Requesting access to a namespace

## Introduction

> **What is a Namespace?**    
A namespace provides a secure workspace for saving models, without interfering with models of other users.
>

Vorto features two kinds of namespaces. 

- Public or "official" namespaces. Those typically reflect an organization or self-contained business unit. Models are typically readable by everyone, including anonymous users
- Private namespaces (easily identifiable as their name starts with `vorto.private`). Those typically belong to individuals or small teams. They are 
intended as sandboxes and feature stronger access restrictions. For instance, models in 
private namespaces are not visible by default to every user, and never to anonymous users.

<hr/>

Accessing models inside namespaces is regulated by roles.
The following roles are available:

Role name | Purpose
----------|--------
`model_viewer` |  To view models in a namespace. <br/>This is implicitly granted to anyone including anonymous users, for models in **public** namespaces. 
`model_creator` | To create new models and modify existing ones.
`model_promoter` | To submit models for review (prior to release) or deprecate them. 
`model_reviewer` | To initiate a release process for a model being reviewed.
`model_publisher` | To approve model releases. 
`namespace_admin` | To manage the namespace and its collaborators. 

Vorto does not feature user-defined roles at this time. 

<hr/>

> **What is a "sub-namespace"?**    
Namespaces are expressed as dot-separated sequences of characters, reflecting some organizational hierarchy much like package names in most programming languages. 
> Sub-namespaces are a decorative feature allowing models to be placed "one or more levels deeper" than their "parent" namespace, purely for aesthetical and organizational purposes.
> Access criteria for models are identical between a namespace and any sub-namespace it might feature. 

<hr/>

## Workflow

When starting up with Vorto, an authenticated user will have read access to models in all public namespaces, and may have all access (including administrative access) to models in a private namespace of their choice, should they decide to create one.

Sometimes, this is not enough. A user may want to collaborate with other users by performing actions illustrated by the roles described above, on models living in other users' namespaces.
  
Vorto facilitates collaboration by allowing users to request access to namespaces. 

Once a user successfully requests for access to a namespace, any user who is `namespace_admin` role of that namespace (i.e. any user managing that namespace) will receive a detailed message via e-mail, stating which user requests access and with which desired role(s). 

Then, it is upon the namespace administrator(s) to take action, and fulfill or ignore the request. 

An important pre-requisite for this workflow to function is that Vorto needs to be able to reach out to the namespace administrator(s) via e-mail. 

> **How do I set up my e-mail address in Vorto?**     
Simply navigate to the `Account settings` in the top navigation bar, then type your e-mail in the appropriate input box and press `Save`. 
>
>Vorto will now be able to contact you via e-mail, e.g. on behalf of other users requesting access to the namespace(s) you manage.
>
## User manual

The form to request access to a namespace has 3 operating modes, all of which requiring the requesting user to be authenticated first.

User authentication in Vorto is done through the OAuth2 provider of your choice. 
First-time authentication signs up the user with a standard Vorto account, once the required permissions are approved by the authenticating user.   

### Modal view
This reflects the common "in-Vorto" usage of the form. 

- After logging on to Vorto, select `Manage` in the top navigation bar, then click the `Request access to a namespace` button. 
- The form opens in a modal window.
- Search and select your target namespace in the first combo-box. This will list all public namespaces, and all private namespaces where you already are a collaborator. You cannot request access to a private namespace where your user has no role already.
- Select which user - yourself, or any technical user of your choice (including a new one) - you are requesting access for.
- Acknowledge the terms and conditions.
- Select the role(s) you wish the user to have on that namespace (see above for a description of the roles).
- Click `Send`. At this point, Vorto will check whether any user managing that namespace has an e-mail set, and if so, attempt to send them a message.
- Whether the message is sent successfully on Vorto's side or not (no e-mail set for any admin, networking issues, etc.), the form will notify you accordingly. 
- Assuming the message is sent and received, it is now in the hands of the namespace administrator(s) to react. Vorto cannot choose the proper course of action in their place, and will not send them a reminder in time. 
- The recipient(s) of the message can decide whether to add your user as a collaborator, and they can also fine-tune which roles to add the user with.

### Standalone view without parameters

- This mode is virtually identical to the modal mode in terms of functionality and UI. 
- The only notable difference is that instead of navigating to the form from Vorto's navigation bar, you can access it in a standalone page at https://vorto.eclipse.org/#/requestAccessToNamespace
- However, there is a far more interesting usage of the standalone form - see below.

### Standalone view with parameters

- This mode has been designed for integration with other services, e.g. the Bosch IoT Suite Portal at the time of writing.
- The most notable difference with this mode is that it is designed to request access for a technical user only.
- The form supports a small set of GET parameters that will slightly change the UI and improve the workflow when the page is linked by a 3rd-party site. 
- The supported parameters are:

Name | Required? | Purpose | Validation
-----|-----------|---------|-----------
`userId` | **Yes** | The only compulsory parameter. <br/>When `userId` is missing or empty, the page will fall back to "standalone without parameters" and ignore any other parameters. <br/>The parameter represents the username of the user for whom access to a given namespace is requested. <br/>When the page loads, Vorto will try to resolve the `userId` to an existing user, and display it. If the user does not exist, it will prompt you to create it as a technical user. | Not empty
`oauthProvider` | No | Only applicable when `userId` does not resolve to an existing user. <br/>This is the OAuth provider the new technical user to be created will be associated with. | Must follow Vorto conventions, e.g. `BOSCH-ID`, `ECLIPSE`, `GITHUB`, `BOSCH-IOT-SUITE-AUTH`, etc.). 
`subject` | No | Only applicable when `userId` does not resolve to an existing user. <br/>The technical user to be created with be persisted with this value as `subject`. <br/>Technical users in Vorto *must* have a subject. | Any 4+ alphanumeric sequence.
`clientName` | No | Purely cosmetic - used by the calling service to display contextual information about the OAuth client involved. | None

  
