---
layout: documentation
title: Upgrade
---
{% include base.html %}

## Upgrading the Vorto Toolset

This section lists the steps required to upgrade the Vorto Toolset.

**Prerequisites**  

- You have a working installation of the previous version of the Vorto Toolset.  
- This assumes that your Eclipse version is in the Vorto supported versions, and stable releases of *Xtext* has been installed (for more information refer to [System Requirements]({{base}}/documentation/overview/introduction.html#system-requirements)).

**Proceed as follows**  

1. Start Eclipse.
2. Switch to the Vorto perspective and close it by doing one of the following:
   * In the main menu, click **Window > Perspective > Close Perspective**.
   * Right-click on the Vorto perspective icon and choose **Close** from the context menu.
3. In the main menu, click **Help > Install new Software...**.  
   The **Install** dialog opens displaying the **Available Software**.  
4. In the **Work with** selection list, select the entry with the *Vorto Toolset update site* URL.  
   The **Available Software** list is updated.  
5. In the **Available Software** list, select **Vorto**.  
   All containing software parts are checked.
6. Click **Next** to verify the installation of *Vorto Toolset* and its dependencies.  
   The **Install** dialog now displays the **Install Details**.  
7. If the toolset are up-to date the **Finish** button remains inactive. End here by clicking **Cancel**.
8. Otherwise, click **Next**.  
   The **Install** dialog now displays the **Review Licenses**.
9. Select **I accept the terms of the license agreements** and click **Finish**.  
   The software is being installed.  
10. If the **Security Warning** dialog opens click **OK**.  
   After the installation is complete the **Software Updates** dialog opens.  
11. Click **Yes** to restart Eclipse.  
