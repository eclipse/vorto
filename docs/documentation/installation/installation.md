---
layout: documentation
title: Installing the Plugins
---
{% include base.html %}
## Installing the Plug-ins

This section details the following topics:

[Getting the Vorto Plug-ins](#getting-the-vorto-plug-ins)

[Installing the Vorto Plug-ins](#installing-the-vorto-plug-ins)  

[Upgrading the Vorto Plug-ins](#upgrading-the-vorto-plug-ins)  

## Getting the Vorto Plug-ins

The Vorto plug-ins are available on the Eclipse update site server. The URL to the server is [http://download.eclipse.org/vorto/update/milestones](http://download.eclipse.org/vorto/update/milestones/0.4.0_M2/ "http://download.eclipse.org/vorto/update/milestones"). You must add this repository to your IDE or, alternatively, import the Vorto plug-in zip archive to get the Vorto plug-ins.


**Proceed as follows**

1. Start your IDE (Eclipse Luna or later).  
2. In the main menu, click **Help > Install new Software...**.  
   The **Install** dialog opens displaying the **Available Software**.
3. Click the selection button of the **Work with** selection list field to check if the *Vorto plug-ins repository* is already present.  
   ![Software update repository]({{base}}/img/documentation/m2m_tc_vrm_software_updates_install_vorto_repository_present.png)  
   If the *Vorto plug-ins repository* URL is displayed in the expanding list, you can end the procedure here.  
   Otherwise proceed with the next step.
4. Add the *Vorto plug-ins repository* URL:  
   a. Click the **Add...** button beside the **Work with** field. The **Add Repository** dialog opens.  
   b. Enter a Name (optionally, e.g., *Vorto*).  
   c. Enter the *Vorto plug-ins repository* URL [http://download.eclipse.org/vorto/update/milestones](http://download.eclipse.org/vorto/update/milestones/0.4.0_M2/ "http://download.eclipse.org/vorto/update/milestones") into the **Location** field.  
   d. Click **OK**.

In the **Install** dialog, the available software list is updated and now contains the software found in the added repository and in the installed archive, respectively.

<table class="table table-bordered">
	<tbody>
		<tr>
			<td><i class="fa fa-info-circle info-note"></i></td>
    <td>The update of the available software list may take a while.</td>
    </tr>
	</tbody>
</table>

## Installing the Vorto Plug-ins

**Prerequisites**  

- You have started your IDE.  
- You have added the *Vorto plug-ins repository* to your IDE (refer to [Getting the Vorto Plug-ins](#getting-the-vorto-plug-ins)).

**Proceed as follows**  

1. If not yet done click **Help > Install new Software...** in the main menu.  
   The **Install** dialog opens displaying the **Available Software**.  
2. In the **Work with** selection list, select the entry with the *Vorto plug-ins* URL.  
   ![Software update repository]({{base}}/img/documentation/m2m_tc_vrm_software_updates_install_vorto_repository_present.png)  
   The **Available Software** list is updated.  
3. In the **Available Software** list, select Vorto.  
   All containing software parts (features) are checked.  
   ![Software updates selected]({{base}}/img/documentation/m2m_tc_vrm_software_updates_selected_m2m_plugin_1.png)  
4. Click **Next** to verify the installation of *Vorto plug-ins* and its dependencies.  
   The **Install** dialog now displays the **Install Details**.  
   ![Software update install details]({{base}}/img/documentation/m2m_tc_vrm_software_updates_install_m2m_details_1.png)  
5. Click **Next**.  
   The **Install** dialog now displays the **Review Licenses**.  
   ![Review licenses]({{base}}/img/documentation/m2m_tc_vrm_software_updates_m2m_review_license_1.png)  
6. Select **I accept the terms of the license agreements** and click **Finish**.  
   The software is being installed.  
7. If the **Security Warning** dialog opens click **OK**.  
   After the installation is complete the **Software Updates** dialog opens.  
   ![Restart VR Modeler]({{base}}/img/documentation/m2m_tc_vrm_software_updates_restart.png)  
8. Click **Yes** to restart the your IDE.  
   After the restart the IDE contains the **Vorto** project group with several new project wizard items (**File > New > Project...**).

   ![New function block model project]({{base}}/img/documentation/m2m_tc_new_vorto_function_block_model_wizard.png)  

<table class="table table-bordered">
   	<tbody>
   		<tr>
   			<td><i class="fa fa-info-circle info-note"></i></td>
         <td>In case the installation is unsuccessful, uninstall the previous version of the Vorto and install it again.</td>
    </tr>
  </tbody>
  </table>

## Upgrading the Vorto Plug-ins

This section lists the steps required to upgrade the Vorto plug-ins.

**Prerequisites**  

- You have a working installation of the previous version of the Vorto plug-ins.  
- This assumes that your IDE version is in the Vorto supported versions, and stable releases of *Xtext* and *M2E* have been installed (for more information refer to [System Requirements]({{base}}/documentation/overview/introduction.html#system-requirements)).
- You have started your IDE.  

**Proceed as follows**  

1. Click **Help > Install new Software...** in the main menu.  
   The **Install** dialog opens displaying the **Available Software**.  
2. In the **Work with** selection list, select the entry with the *Vorto plug-ins* URL.  
   The **Available Software** list is updated.  
3. In the **Available Software** list, select `Vorto`.  
   All containing software parts (features) are checked.
4. Click **Next** to verify the installation of *Vorto plug-ins* and its dependencies.  
   The **Install** dialog now displays the **Install Details**.  
5. If the plug-ins are up-to date the **Finish** button remains inactive. End here by clicking **Cancel**.
5. Otherwise, click **Next**.  
   The **Install** dialog now displays the **Review Licenses**.
6. Select **I accept the terms of the license agreements** and click **Finish**.  
   The software is being installed.  
7. If the **Security Warning** dialog opens click **OK**.  
   After the installation is complete the **Software Updates** dialog opens.  
8. Click **Yes** to restart the your IDE.  
