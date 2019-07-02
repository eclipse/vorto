# Managing Collaborators

> **What is a Collaborator** ?    
A Collaborator is a user with access to the namespace. 
A Collaborator who has Administrator access can add other collaborators and also assign roles to them.    
This way multiple users can collaborate on the same model/namespace pertaining to their respective roles.


<br />

## Editing Collaborators
After [creating a namespace](./managing_namespaces.md), you will see the option to `Manage Collaborators` as one of the actions of your namespace.

![manage collaborators action](../images/tutorials/managing_collaborators/managing_collaborators.png)

<br />

This action will trigger a dialog box to open in which you can see all the collaborators of the according namespace with their assigned roles.

![collaborators of namespace](../images/tutorials/managing_collaborators/collaborator_overview.png)

<br />

Each Role gives the collaborator permissions for specific actions for the according namespace.   
> **Note**: Please reference the role management matrix below for detailed information about which role gives which permissions.

![add collaborator](../images/tutorials/managing_collaborators/add_collaborator.png)


## Roles
Each Role provides collaborators with specific permissions for the according namespace.

|          | View Models | Create Models | Edit Models | Delete Models | Submit Models for Review | Release Models | Publish Models | Edit Namespace | Add Collaborators | Remove Collaborators | Change Collaborator Roles |
|----------|-------------|---------------|-------------|---------------|--------------------------|----------------|----------------|----------------|-------------------|----------------------|---------------------------|
| VIEW     | ✔           | ✘             | ✘           | ✘             | ✘                        | ✘              | ✘              | ✘              | ✘                 | ✘                    | ✘                         |
| CREATE   | ✔           | ✔             | ✔           | ✔             | ✘                        | ✘              | ✘              | ✘              | ✘                 | ✘                    | ✘                         |
|  PROMOTE | ✔           | ✘             | ✘           | ✘             | ✔                        | ✘              | ✘              | ✘              | ✘                 | ✘                    | ✘                         |
| REVIEW   | ✔           | ✘             | ✘           | ✘             | ✘                        | ✔              | ✘              | ✘              | ✘                 | ✘                    | ✘                         |
| PUBLISH  | ✔           | ✘             | ✘           | ✘             | ✘                        | ✘              | ✔              | ✘              | ✘                 | ✘                    | ✘                         |
| ADMIN    | ✔           | ✘             | ✘           | ✘             | ✘                        | ✘              | ✘              | ✔              | ✔                 | ✔                    | ✔                         |
