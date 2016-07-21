---
layout: documentation
title: Installation
---
{% include base.html %}

##Installation

This section details the following topics:

[Installing the Vorto Toolset](#installing-the-vorto-toolset)  

[Upgrading the Vorto Toolset](./upgrade.html)  

##Installing the Vorto Toolset

This section lists the steps required to install the Vorto Toolset.

Install the Eclipse Vorto Toolset in one of the following three ways:

* [Installing from the Eclipse Vorto download page](#installing-from-the-eclipse-vorto-download-page)  
* [Installing from the Eclipse Marketplace](#installing-from-the-eclipse-marketplace)  
* [Installing from the Vorto Toolset Upadte Site](#installing-from-the-vorto-toolset-upadte-site)  

### Installing from the Eclipse Vorto Download Page

**Proceed as follows**

1. Start Eclipse.
2. With your browser, go to the [Eclipse Vorto Download Page](https://eclipse.org/vorto/downloads).  
   The Eclipse Vorto download page opens.  
   ![Eclipse Vorto Download Page]({{base}}/img/documentation/vorto_eclipse_vorto_download_page.png)
2. Do one of the following:  
   * Drag and drop the Install button ![Install Button]({{base}}/img/documentation/vorto_eclipse_vorto_download_install_button.png) into your running Eclipse application.  
     The **Confirm Selected Features dialog** of the **Eclipse Marketplace** opens.  
     ![Eclipse Marketplace Confirm Selected Features Dialog]({{base}}/img/documentation/vorto_eclipse_vorto_download_marketplace_confirm_selected_features_dialog.png)  
     Click the **Confirm >** button and proceed accepting the license (step 6 in [Installing](#installing)).  
   * Click the **Vorto Update Site** button (**(Stable)** or **(Nightly)** according to your needs).  
     An Eclipse standard page opens showing instructions how to use the download links in Eclipse.  
     ![Eclipse Standard Download Info Page]({{base}}/img/documentation/vorto_eclipse_vorto_download_info_page.png)  
     Follow the instructions.  
   * Click the **Vorto zip** button (**(Stable)** or **(Nightly)** according to your needs).  
     The download dialog of your browser opens.  
     Download the zip file containing the Vorto Toolset archive to a location of your choice and install the archive in the standard way.

### Installing from the Eclipse Marketplace.

**Proceed as follows**

1. Start Eclipse.
2. In the main menu, click **Help > Eclipse Marketplace...**.  
   The **Eclipse Marketplace** dialog opens.
2. In the **Find** entry field enter `Vorto` and click the **Go** button.  
   The Vorto Toolset is displayed.  
   ![Eclipse Marketplace Install Vorto]({{base}}/img/documentation/vorto_eclipse_vorto_download_marketplace_install_dialog.png)
3. Click the **Install** button.  
   The Vorto Toolset is installed.

### Installing from the Vorto Toolset Upadte Site

The Vorto Toolset is available on the Eclipse Vorto update site server. The URL to the server is `http://download.eclipse.org/vorto/update/milestones` (*Vorto Toolset update site*). You must add this repository to Eclipse or, alternatively, import the Vorto Toolset zip archive to install the Vorto Toolset.

#### Setting the Vorto Toolset Update Site

**Proceed as follows**

1. Start Eclipse.  
2. In the main menu, click **Help > Install new Software...**.  
   The **Install** dialog opens displaying the **Available Software**.
3. Click the selection button of the **Work with** selection list field to check if the *Vorto Toolset update site* is already present.  
   ![Software update repository]({{base}}/img/documentation/m2m_tc_vrm_software_updates_install_vorto_repository_present.png)  
   If the *Vorto Toolset update site* URL is displayed in the expanding list, you can end the procedure here.  
   Otherwise proceed with the next step.
4. Add the *Vorto Toolset update site* URL:  
   a. Click the **Add...** button beside the **Work with** field. The **Add Repository** dialog opens.  
   b. Enter a Name (optionally, e.g., *Vorto*).  
   c. Enter the *Vorto Toolset update site* URL `http://download.eclipse.org/vorto/update/milestones` into the **Location** field.  
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

#### Installing

**Prerequisites**  

- You have started Eclipse.  
- You have set the *Vorto Toolset update site* in Eclipse (refer to [Setting the Vorto Toolset Update Site](#setting-the-vorto-toolset-update-site)).

**Proceed as follows**  

1. If not yet done click **Help > Install new Software...** in the main menu.  
   The **Install** dialog opens displaying the **Available Software**.  
2. In the **Work with** selection list, select the entry with the *Vorto Toolset* URL.  
   ![Software update repository]({{base}}/img/documentation/m2m_tc_vrm_software_updates_install_vorto_repository_present.png)  
   The **Available Software** list is updated.  
3. In the **Available Software** list, select **Vorto**.  
   All containing software parts are checked.  
   ![Software updates selected]({{base}}/img/documentation/m2m_tc_vrm_software_updates_selected_m2m_plugin_1.png)  
4. Click **Next** to verify the installation of the Vorto Toolset and its dependencies.  
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
8. Click **Yes** to restart Eclipse.  
   After the restart, Eclipse contains the **Vorto** project group with several new project wizard items (**File > New > Project...**).

   ![New function block model project]({{base}}/img/documentation/m2m_tc_new_vorto_function_block_model_wizard.png)  

<table class="table table-bordered">
   	<tbody>
   		<tr>
   			<td><i class="fa fa-info-circle info-note"></i></td>
         <td>In case the installation is unsuccessful, uninstall the previous version of the Vorto Toolset and install it again.</td>
    </tr>
  </tbody>
  </table>

## Upgrading the Vorto Toolset

This section lists the steps required to upgrade the Vorto Toolset.

**Prerequisites**  

- You have a working installation of the previous version of the Vorto Toolset.  
- This assumes that your Eclipse version is in the Vorto supported versions, and stable releases of *Xtext* has been installed (for more information refer to [System Requirements]({{base}}/documentation/overview/introduction.html#system-requirements)).

**Proceed as follows**  

1. Start Eclipse.
2. In the main menu, click **Help > Install new Software...**.  
   The **Install** dialog opens displaying the **Available Software**.  
2. In the **Work with** selection list, select the entry with the *Vorto Toolset update site* URL.  
   The **Available Software** list is updated.  
3. In the **Available Software** list, select **Vorto**.  
   All containing software parts are checked.
4. Click **Next** to verify the installation of *Vorto Toolset* and its dependencies.  
   The **Install** dialog now displays the **Install Details**.  
5. If the toolset are up-to date the **Finish** button remains inactive. End here by clicking **Cancel**.
5. Otherwise, click **Next**.  
   The **Install** dialog now displays the **Review Licenses**.
6. Select **I accept the terms of the license agreements** and click **Finish**.  
   The software is being installed.  
7. If the **Security Warning** dialog opens click **OK**.  
   After the installation is complete the **Software Updates** dialog opens.  
8. Click **Yes** to restart the Eclipse.  
