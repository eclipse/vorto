# Migrating from 0.10 to 0.11 Vorto Repository
This guide will walk you through the process of migrating your existing Vorto models from the old repository into the new Vorto repository. Let's get started.

<br />

## Prerequisites
- [GitHub](https://github.com/login) or [Bosch-Id](https://accounts.bosch-iot-suite.com/)
- Existing Vorto models in the old repository which you would like to migrate

<br />

## Steps
**1.** Log in to old [Vorto Repository](https://vorto-old.eclipse.org)

**2.** Select the model you would like to migrate
> **Note:** Select the checkbox *Only My Models* to view models created by you

**3.** Download your *Model File* using the `ZIP archive` link

![download model](../docs/images/migrate_model/download_model.png)

**4.** Log in to new [Vorto Repository](https://vorto.eclipse.org)

**5.** [Create and manage Namespace](../../docs/tutorials/managing_namespaces.md), by default all new namespaces will be prefixed with *vorto.private*. To claim for an official namespace, [reach out to the Vorto Team](../../docs/tutorials/managing_namespaces.md#claiming-an-official-namespace).

**6.** Click **Browse** and select the zip file you downloaded in Step-3.   
Make sure to select the target namespace from the dropdown in which your model should be imported in.

**7.** Click **Upload** 

**8.** If all goes well you will receive a success message *model is valid and ready for import*   
In addition to that, you will also see all of the submodels that will be imported and which ones already exist.

![successful upload](../docs/images/migrate_model/successful_upload.png)

**9.** Click **Import** to finish uploading.   

> **Note**: Pictures have to be downloaded and uploaded separately. Simply **Right-Click and Save** the image in the old Vorto Repository and **re-upload** it in the new one. 

<br />

**You're now ready to use the models.**



