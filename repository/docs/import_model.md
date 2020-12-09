# Importing a Vorto Model 

If you had created an external / 3rd party tool to create Vorto Models, you can easily import it into the Repository.

## Prerequisite

* [BoschID](https://accounts.bosch-iot-suite.com/) Account or [GitHub](https://github.com/) 
* Existing Vorto Model(s)
* You are a collaborator/owner of a namespace

## Steps to Import any type of model

**1.** Log in to the [Vorto Repository](https://vorto.eclipseprojects.io)

**2.** [Create and manage Namespace](../../docs/tutorials/managing_namespaces.md), by default all new namespaces will be prefixed with *vorto.private*. To claim for an official namespace, [reach out to the Vorto Team](../../docs/tutorials/managing_namespaces.md#claiming-an-official-namespace).

**3.** Click **Browse** and select the zip file you downloaded in Step-3.   
Make sure to select the target namespace from the dropdown in which your model should be imported in.

**4.** Click **Upload** 

**5.** If all goes well you will receive a success message *model is valid and ready for import*   
In addition to that, you will also see all of the submodels that will be imported and which ones already exist.

![successful upload](../docs/images/migrate_model/successful_upload.png)

**6.** Click **Import** to finish uploading.   

**You're now ready to use the models.**

