# Managing Namespaces

> **What is a Namespace** ?    
A namespace provides a secure workspace for saving models, without interfering with models of other users.



<br />

## Creating your Namespace
When you first login with your account, you won't be able to create Vorto Models.   
In order to create them, you need to either own your own namespace or be part of a project in which you've been assigned the role of *Model Creator*.

Creating a new namespace can easily be done by clicking on the `Manage` tab and then `Create`.
![namespace tab](../images/tutorials/managing_namespaces/create_namespace.png)

<br />

This will trigger a dialog box which let's you enter the namespace you want to use.   
> **Note**: Each newly created Namespace will use the `vorto.private` prefix. If you want to claim an official namespace like e.g. `org.eclipse.vorto` you need to [reach out to the Vorto Team](mailto:vorto-development@bosch-si.com?Subject=Request%20Vorto%20Repository%20Namespace&body=Dear%20Vorto%20Team%2C%20%0A%0AI%20would%20like%20to%20request%20for%20an%20official%20namespace.%20%0A%0ANamespace%20Owner%20%28user%20ID%29%20%3A%20%0ANamespace%3A%0A%0AThank%20you.%20%0A%0ABest%20regards%2C%20).

![namespace tab](../images/tutorials/managing_namespaces/define_namepsace.png)

<br />

Once the create button is clicked, your new namespace will be created.   
By default, you as the namespace owner will have all permissions and are able to manage the namespace and it's collaborators. 

![namespace tab](../images/tutorials/managing_namespaces/namespace_created.png)




<br />

## Claiming an official Namespace
If you want to make the models you've created publicly available, you need to own the according official namespace.   
In order to claim an official namespace, like e.g. `org.eclipse.vorto`, you need to [reach out to the Vorto Team](mailto:vorto-development@bosch-si.com?Subject=Request%20Vorto%20Repository%20Namespace&body=Dear%20Vorto%20Team%2C%20%0A%0AI%20would%20like%20to%20request%20for%20an%20official%20namespace.%20%0A%0ANamespace%20Owner%20%28user%20ID%29%20%3A%20%0ANamespace%3A%0A%0AThank%20you.%20%0A%0ABest%20regards%2C%20).

When reaching out to the Vorto Dev Team, please make sure to **include the name of the namespace and UserId of its Owner**.

---

In case you're having difficulties or facing any issues, feel free to [create a new question on StackOverflow](https://stackoverflow.com/questions/ask?tags=eclipse-vorto) and we'll answer it as soon as possible!   
Please make sure to use `eclipse-vorto` as one of the tags. 

<br />

## Sub-namespaces

> **What is a "sub-namespace"?**   
> 
> Namespaces are expressed as dot-separated sequences of characters, reflecting some organizational hierarchy much like package names in most programming languages.
> 
> Sub-namespaces are "virtual" namespaces that allow models to be placed "one or more levels deeper" than their "parent" namespace, purely for aesthetical or organizational purposes.
>
> Access criteria for models are identical between a namespace and any sub-namespace it might feature. 
>
> It is also worth noting that sub-namespaces are the only way to create hierarchical namespaces. 
>
> In other words, if the namespace `com.mycompany` exists, one can only create models in `com.mycompany.examples` via a sub-namespace. 
> However, creating another namespace called `com.mycompany.examples` is not possible.  