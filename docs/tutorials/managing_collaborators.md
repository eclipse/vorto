# Managing Collaborators

> **What is a Collaborator** ?    
A Collaborator is user with access to the namespace. 
A Collaborator who has Administrator access can add other collaborators and also assign roles to them.    
This way multiple users can collaborate on the same model/namespace pertaining to their respective roles.


<br />

## Editing Collaborators
After [creating a namespace](./managing_namespaces.md), you will see the option to `Manage Collaborators` as one of the actions of your namespace.
![manage collaborators action](..)

<br />

This action will trigger a dialog box to open in which you can see all the collaborators of the according namespace.
![collaborators of namespace](..)

<br />

Each Role gives the collaborator permissions for specific actions for the according namespace.   
> **Note**: Please reference the role management matrix below for detailed information about which role gives which permissions.
![add collaborator](..)


## Roles

|          | View Models | Create Models | Edit Models | Delete Models | Submit Models for Review | Release Models | Publish Models | Edit Namespace | Add Collaborators | Remove Collaborators | Change Collaborator Roles |
|----------|-------------|---------------|-------------|---------------|--------------------------|----------------|----------------|----------------|-------------------|----------------------|---------------------------|
| VIEW     | ✔           | ✘             | ✘           | ✘             | ✘                        | ✘              | ✘              | ✘              | ✘                 | ✘                    | ✘                         |
| CREATE   | ✔           | ✔             | ✔           | ✔             | ✘                        | ✘              | ✘              | ✘              | ✘                 | ✘                    | ✘                         |
|  PROMOTE | ✔           | ✘             | ✘           | ✘             | ✔                        | ✘              | ✘              | ✘              | ✘                 | ✘                    | ✘                         |
| REVIEW   | ✔           | ✘             | ✘           | ✘             | ✘                        | ✔              | ✘              | ✘              | ✘                 | ✘                    | ✘                         |
| PUBLISH  | ✔           | ✘             | ✘           | ✘             | ✘                        | ✘              | ✔              | ✘              | ✘                 | ✘                    | ✘                         |
| ADMIN    | ✔           | ✘             | ✘           | ✘             | ✘                        | ✘              | ✘              | ✔              | ✔                 | ✔                    | ✔                         |
