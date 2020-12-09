# Release Notes

<br />

## 1.0.0

Vorto 1.0.0 is a major release providing both new features, enhancements, bug fixes and additional documentation.
The highlights are:

- Improved performance of the repository
- Improved functionality and usability for model search
- Improved user experience for generators
- Enhanced handling of authentication mechanisms as intermediary step towards Vorto being authentication provider
 agnostics in further releases

 **Bugfixes**

- [#2570](https://github.com/eclipse/vorto/issues/2570) LATEST API fails on /api/v1/models/<modelId>/file
- [#2549](https://github.com/eclipse/vorto/issues/2549) Models found count is not accurate, because it's capped at 1000
- [#2555](https://github.com/eclipse/vorto/issues/2555) Loading model details also triggers diagnostics for that model if the user is sysadmin
- [#2454](https://github.com/eclipse/vorto/issues/2454) Hono Generator doesn't create getter/setter for Maps
- [#2243](https://github.com/eclipse/vorto/issues/2243) users with no "publish" permission can no longer see the "publish" button on a model's details
- [#2057](https://github.com/eclipse/vorto/issues/2057) reliability fixes in "isMultiplicity" property for
 generator API
- [#2114](https://github.com/eclipse/vorto/issues/2114) improved logic for attachment validation
- [#2168](https://github.com/eclipse/vorto/issues/2168) improved handling of Content-Disposition header for lambda generators
- [#2172](https://github.com/eclipse/vorto/issues/2172) entities no longer at risk of generating duplicate mapping keys for OpenAPI generator


**Improvements**

- [#2611](https://github.com/eclipse/vorto/issues/2611), [#2599](https://github.com/eclipse/vorto/issues/2599), [#2589](https://github.com/eclipse/vorto/issues/2589), [#2562](https://github.com/eclipse/vorto/issues/2562), [#2559](https://github.com/eclipse/vorto/issues/2559), [#2552](https://github.com/eclipse/vorto/issues/2552), [#2472](https://github.com/eclipse/vorto/issues/2472), [#2558](https://github.com/eclipse/vorto/issues/2558) Performance improvements
- [#2615](https://github.com/eclipse/vorto/issues/2615) Secure post-login cookie with SameSite attribute
- [#2503](https://github.com/eclipse/vorto/issues/2503) Add a "createdBy" field to the user table
- [#2432](https://github.com/eclipse/vorto/issues/2432) Upgrade jQuery to latest version (CVE-2020-11022, CVE-2020-11023)
- [#2423](https://github.com/eclipse/vorto/issues/2423) Update tomcat-embed-core to 8.5.55
- [#2377](https://github.com/eclipse/vorto/issues/2377) Update Elasticsearch to version 6.8.8
- [#1878](https://github.com/eclipse/vorto/issues/1878) Feature: REST API for models allows accessing latest model version by specifying "latest" instead of a specific version
- [#1735](https://github.com/eclipse/vorto/issues/1735), [#2154](https://github.com/eclipse/vorto/issues/2154) Feature: web UI user is warned when session about to expire, and prevented from performing any non
-applicable operation when session is expired
- [#2269](https://github.com/eclipse/vorto/issues/) Enhancement: "requestBodyOnly" option for the Bosch IoT Suite Generator added to the UI
- [#2028](https://github.com/eclipse/vorto/issues/2028), [#2030](https://github.com/eclipse/vorto/issues/2030), [#2035](https://github.com/eclipse/vorto/issues/) Enhancement: improved tutorial documentation for mapping pipeline
- [#1833](https://github.com/eclipse/vorto/issues/1833), [#2046](https://github.com/eclipse/vorto/issues/2046), [#2217](https://github.com/eclipse/vorto/issues/2217), [#2221](https://github.com/eclipse/vorto/issues/2221), [#2229](https://github.com/eclipse/vorto/issues/2229), [#2132](https://github.com/eclipse/vorto/issues/2132), [#2137](https://github.com/eclipse/vorto/issues/2137), [#2242](https://github.com/eclipse/vorto/issues/2242) Enhancement: improved model search syntax by
 adding
 multiple wildcard support, case-insensitive search and documentation
- [#2206](https://github.com/eclipse/vorto/issues/2206) Feature: added custom error page for unhandled exceptions
- [#2191](https://github.com/eclipse/vorto/issues/2191) Enhancement: presentational improvements in namespace collaborator management
- [#2088](https://github.com/eclipse/vorto/issues/2088), [#2208](https://github.com/eclipse/vorto/issues/2208) Feature: namespace managers can now add non-existing users as technical users, by providing a user ID
 and a subject, and selecting an oauth provider
- [#1930](https://github.com/eclipse/vorto/issues/1930) Enhancement: improved Oauth support for technical users
- [#2058](https://github.com/eclipse/vorto/issues/2058) Enhancement: improved reliability of "event" and "operation" syntactical block handling
- [#1929](https://github.com/eclipse/vorto/issues/1929) Feature: Vorto repository can now run in cluster mode for high availability
- [#2130](https://github.com/eclipse/vorto/issues/2130), [#2157](https://github.com/eclipse/vorto/issues/2157) Feature: added Oauth provider API,JWT provider API for 3rd party technical user authentication, and
 documentation
- [#2106](https://github.com/eclipse/vorto/issues/) Feature: added Vorto generator versioning and caching
- [#2159](https://github.com/eclipse/vorto/issues/2159), [#2219](https://github.com/eclipse/vorto/issues/2219) Support: migration scripts are now provided for self-hosted Vorto repository administrators
- [#2248](https://github.com/eclipse/vorto/issues/2248) Enhancement: function block mapping is now documented

## 0.12.5

**Bugfixes**

- Missing models for attributes in Repository OpenAPI/Swagger [#2116](https://github.com/eclipse/vorto/issues/2116)
- Web Editor creates duplicate fields [#2058](https://github.com/eclipse/vorto/issues/2058)
- Array types, defined in DSL, are not properly serialized to JSON [#2057](https://github.com/eclipse/vorto/issues/2057)

**Improvements**

- Proper error handling and reporting [#2062](https://github.com/eclipse/vorto/issues/2062)

<br />

## 0.12.4

**Bugfixes**

- Code Generator performance due to unneeded mapping resolvings [#2049](https://github.com/eclipse/vorto/issues/2049)

<br />


## 0.12.3

**Bugfixes**

- Fixes bug that array types could not be passed correctly for loop iterations to javascript converter functions [#2029](https://github.com/eclipse/vorto/issues/2029)
- Fixes bug in Mapping Editor UI for displaying rules for nested entities [#2036](https://github.com/eclipse/vorto/issues/2036)


<br />

## 0.12.2

**Bugfixes**

- Fixes security vulnerability in an embedded tomcat core [#2026](https://github.com/eclipse/vorto/issues/2026)
- Fixes bug, that keyword 'multiple' was ignored for generators [#2004](https://github.com/eclipse/vorto/issues/2004)
- Fixes bug, that OpenAPI was missing a few Bosch IoT Things resources [#1964](https://github.com/eclipse/vorto/issues/1964)
- Fixes bug that ProvisioningScript Generator returned a zip archive instead of single script file [#2010](https://github.com/eclipse/vorto/issues/2010)


<br />

## 0.12.1

**Bugfixes**

- Import very slow for zip archives containing 50+ models [#1997](https://github.com/eclipse/vorto/issues/1997)
- Creator and ModifiedBy user ID not anonymized correctly, when user account is deleted [#1976](https://github.com/eclipse/vorto/issues/1976)
- Inheritance not resolved completely during Code Generation [#1954](https://github.com/eclipse/vorto/issues/1954)


<br />

## 0.12

This version primarily focuses on stability, performance as well as UX.

### New Features

**Vorto Repository Features**

* Content - Assist / Auto-completion in Web Editors when writing vortolang models
* Backup & Restore of all models per namespace
* REST API Endpoint to allow the import of Vorto Models (or 3rd party models) in the Repository


**Device Integration Features**

* Support for nested entities inside Function Blocks in the Payload Mapping Engine (Java only). Please take note of the compatibility section.
* Supported Payload Mapping Target Platform format: AWS IoT Shadow and Eclipse Ditto Protocol
* Support of operation return types in OpenAPI Generator

**Compatibility**

With this release, the Vorto Payload Mapping Engine now supports the mapping to nested entities, declared in Function Blocks. This change required us to completely modify the Mapping Specification JSON runtime model, which is required  by the Mapping Engine at configuration time. The old mapping specification runtime-model is no longer supported. Therefore, make sure to download the new Mapping Specification from the Vorto Repository and deploy it in your system, if you upgrade to this new version! This change **does not break** any previous design time mapping models!

<br />

## 0.11.3

**Bugfixes**

- Arduino/C Generator generates invalid code for getters and setters in pojos [#1882](https://github.com/eclipse/vorto/issues/1882)

<br />

## 0.11.2

**Bugfixes**

- Repository session error when saving or importing models [#1876](https://github.com/eclipse/vorto/issues/1876)

<br />

## 0.11.1

**Bugfixes**

- OpenAPI Generator, creating invalid spec for FB with only operations
- Arduino Generator creating invalid code for properties with name "value"
- CORS problem when using the Vorto Repository Swagger API documentation
- Repository UI Layout for browsers with lower resolution
- Vorto Function Block Selector during model creation showing all released functionblocks instead of only org.eclipse.vorto.*
- Fixed security vulnerabilities during file upload

**Minor Improvements**

- Generators generating a single file return file instead of ZIP archive
- UX improvements in all modal dialogs of repository

<br />

## 0.11

With the release of **Eclipse Vorto 0.11**, we are introducing **essential improvements** that guarantee you full control over your models and their release process.

**Fine-grained [Role Management](tutorials/managing_collaborators.md)** allows your team to restrict access to certain features like the creation of models, releasing it to the public, and deprecating them.

Models, by default, are created in **private [namespaces](tutorials/managing_collaborators.md)** for which the access can be controlled in detail using the newly introduced roles.   
In addition to the new, state-of-the-art, Eclipse Vorto Repository, we released the **official [1.0 version of the Vortolang](./vortolang-1.0.md)**, the DSL powering the Vorto Ecosystem.

Due to these substantial changes, we've decided that automatically migrating all existing models to the new system is the improper approach and therefore provided **straight-forward exporters and importers** that will allow you to effortlessly migrate your **models of choice** from the old system.

Enhancements in all areas are complemented with **new and extensive [documentation](./gettingstarted.md)** for not only new features and improvements but also convenient plugins that have easily been missed in the past.

All in all, **this release addresses, tackles, and simplifies** many key pain points for people working with and in IoT and delivers some of the most important improvements to Eclipse Vorto ever.

> Follow our comprehensive guide on how to [migrate your models](../repository/migration/migration_0.11.md) to the new system.

<br />

### New Features

Please refer to our [Getting started guide](https://github.com/eclipse/vorto/blob/master/docs/gettingstarted.md) for details.

**Vorto Repository Features**

- Vortolang 1.0, the IoT language for Digital Twins
- Multi - Tenancy, which lets users manage their own namespace(s) and collaborators via a self-service UI
- Model visibility of Vorto Models to change models access from private to public
- Log-in with Github and Bosch ID OAuth Provider
- Many stabilization and performance fixes

[Click here](https://vorto.eclipseprojects.io) and start using the new official Eclipse Vorto Repository.

**Device Integration Features**

- Payload Mapping Engine (Java and node.js), that lets you convert arbitrary (device) data to normalized Vorto-compliant data structure, fast and reliably.
- Bosch IoT Suite Generator add-on: Generates a script to provision devices in the Bosch IoT Suite from Vorto models
- OpenAPI / Swagger Generator, which generates a device-specific REST - API specification for Digital Twin services, such as Bosch IoT Things.
- JSON - Schema Generator, which creates JSON-Schema files from Vorto models


**Developer SDK Features**

- Plugin SDK Version 2, to build and run serverless Vorto generator- and importer plugins. The SDK provides utilities and many [source code examples](https://github.com/eclipse/vorto-examples)

### Deprecation

- Plugin SDK Version 1 is marked as deprecated in this release. However it is still supported to implement and run generator plugins based on Version 1.0. However we are recommending to migrate them to Plugin Version 2, as it is much easier and faster.

<br />

## 0.10.1

**Bugfixes**

- Enum Datatypes are not properly returned in Java Repository Client
- Eclipse Hono Generator for Arduino creates invalid json for referenced entities
- Bosch IoT Suite Generator creates invalid Eclipse Ditto protocol payload
- Measurement Unit in DSL not working
- OMA/LwM2M to Vorto Models Resolver is broken

**Minor Improvements**

- Repository Search UX
- Bosch IoT Suite Generator for Web Dashboard and Device Provisioning Scripts

<br />

## 0.10

### New Features

**Vorto Repository Features**

- Model Permission Management, that allows users to grant 'READ' permissions of their models to other users.
- Cloud-based Vorto DSL Editors to create Vorto Models directly in the Repository
- Release & Review Workflows for Vorto Models
- Model Attachments, that allows users to add/remove attachments for models, like additional documentation
- Various UX Improvements
- Many stabilization and performance fixes

[Click here](https://vorto.eclipseprojects.io) to try out the Eclipse Vorto Repository with all its new features.

**Device Integration Features**

- Payload Mapping Engine (Java-based) that executes Vorto Mapping Models in order to map device telemetry data to abstract Vorto Function Blocks.
- Mapping Engine support for [Eclipse Ditto](https://www.eclipse.org/ditto) protocol, to automatically convert Vorto compliant data to Eclipse Ditto data structures.
- Payload Mapping Specification Editor UI, that allows device manufacturers to create mapping specifications for mapping arbitrary device payload to abstract, semantic Vorto Models
- Code Generators supporting Arduino, Python and Java - based devices, generating code to send telemetry data to [Eclipse Hono](https://www.eclipse.org/hono) and [Eclipse Ditto](https://www.eclipse.org/ditto) based platforms via [Eclipse Paho MQTT](https://www.eclipse.org/paho)

**Developer SDK Features**

- Generator Plugin SDK, which allows to build and deploy Vorto Generator-plugins as standalone (micro)services and hook them into the Vorto repository.

### Deprecation

- The Eclipse Vorto IoT Toolset Plugins are no longer supported. Instead you can simply use the Cloud-based Editors in the Vorto Repository.
- Changes in the Java Repository Client: The client has a new simplified *org.eclipse.vorto.repository.client.IRepositoryClient* in order to read Vorto Models.

<br />

## 0.10.0 milestone 11

**Fixes and Enhancements**

* Model Confidentiality & Permission Enhancement: By default, all models are private and only visible & modifiable by the respective model owner. However, model owners can grant READ permissions to other users, so that they can review give feedback.
* Simplified Java Repository Client, deprecating the old Java Client.
* Simplified Eclipse Vorto Plugins, allowing to create Vorto Models using the Eclipse IDE.  
* Various Bug-fixes

<br />

## 0.10.0 milestone 10

**Fixes and Enhancements**

* Various Bug-fixes
* Various Stabilization fixes

<br />

## 0.10.0 milestone 9

**Fixes and Enhancements**

* Support for model inheritance during Code Generation
* Model Details and Mapping Editor now gives immediate feedback to developer if model is invalid
* Boost of performance upon model search and retrieval
* Various UI fixes, e.g Pagination in search etc.
* Various Stabilization fixes

<br />

## 0.10.0 milestone 8

**Fixes and Enhancements**

* Email Notifications for Workflow actions, e.g. Pending Reviews, as well as comments
* Added more advanced Vorto DSL Validator Checks when importing Vorto Models or when creating/editing models via the Vorto Web Editor
* Various Bugfixes
* Generator Plugin SDK, which allows to build and deploy Vorto Generator plugins as standalone (micro)services and hook them into the Vorto repository.

<br />

## 0.10.0 milestone 7

**Fixes and Enhancements**

* Anonymous read-access to Vorto Models
* Payload Mapping Specification Editor, which allows to simply create & test device payload mapping specifications

<br />

## 0.10.0 milestone 6

**Fixes and Enhancements**

* Support for Vorto DSL Configuration properties in Bosch IoT Suite Generators (incl. Eclipse Hono)
* Security Fixes, such as XXE when uploading malicious LwM2M XMLs
* UX related Improvements  

<br />

## 0.10.0 milestone 5

**Fixes**

* Fixes bug when generating Eclipse Hono Java Code for a single Function Block
* Fixes bug when requesting image attachments from Repository via REST
* Fixes bug in Payload Mapping Library for retrieving Mapping Specifications from Repository
* Minor Repository UI/UX Improvements

<br />

## 0.10.0 milestone 4

**Fixes and Enhancements**

* Simple Web Editor to create and edit Vorto Models directly from the Vorto Repository
* [Importer API]({{< relref "developerguide.md" >}}) that lets you import and manage other (standardized) device descriptions with the Vorto Repository
	* LwM2M/IPSO Importer that imports [LwM2M Smart Objects](https://github.com/IPSO-Alliance/pub/tree/master/reg%20v1_1)
* Model Attachment Management
* Model State Management
* Various DSL Enhancements. [More details]({{< relref "userguide/quickhelp_dsl.md" >}})
	* _multiple_ keyword in Information Model DSL to express multiplicity of function blocks
	* _extension_ keyword in Function Block DSL to overwrite properties from an inherited Function Block
	* Possibility to declare Function Blocks as either _mandatory_ or _optional_ in Information Models
* Many UI/UX Improvements
* Many Bugfixes

<br />

## 0.10.0 milestone 3

**Fixes and Enhancements**

- EU-GDPR Compliance of Vorto Repository, i.e. Export of user-specific Vorto models.
- Eclipse Hono Generator, supporting the generation of Arduino-, Python- and Java source code.
- Code Generator Configuration UI. Code Generators that are executed from the Eclipse Vorto Toolset do now provide a configuration UI that allows developers to customize the execution of the generator.


**API Changes**

- Vorto Code Generators must now implement `org.eclipse.vorto.codegen.api.IVortoCodeGenerator.getInfo()` that give more meta information about the specific Code Generator. This information is used by Vorto to provide a Generator Configuration UI automatically.
- The Vorto Repository REST - API for publishing models is now secured exclusively with OAuth2 token - based authentication.
	> It is now only possibly to publish models to the Vorto Repository via the Vorto Repository Web UI !

<br />

## 0.10.0 milestone 2
**Fixes and Enhancements**

* Vorto Mapping library that is able to convert arbitrary device payload to specific IoT platform format, e.g. to Eclipse Ditto.
* More code generators, that generate device code that integrates with Eclipse Hono via MQTT.
* Many bugfixes

<br />

## 0.10.0 milestone 1
We are proud to announce Eclipse Vorto 0.10.0 milestone 1.

Here are a few major highlights:

* **Web Editor** that allows to create information-, function block- and datatype models via a web-based editor.
* More **Code Generators** have been added to the list of existing generators.
* **Repository Java client** API
* Many bug fixes and improvements

### Web Editor
With the **Web Editor**, users can describe devices online and publish these as information models to the Vorto Repository.

![WebEditor](/images/release_notes/0.10.0-m2/webeditor_details.png)

### Code Generators

We have a few new Code Generators for easier device integration. Those are

*   Alexa Skill-set Generator for Bosch IoT Suite
*   Device Simulator Generator for Bosch IoT Suite
*   Eclipse Kura Generator
*   Web-based UI Dashboard Generator

* * *

### Alexa Skill-set & Device Simulator for Bosch IoT Suite Generators

The Alexa Skill set Generator for Bosch IoT Suite generates an Alexa Skill Interaction model and Lambda function that fetches the device data from Bosch IoT Suite.

The Device Simulator Generator for Bosch IoT Suite generates a simulator that creates test data for a given information model and publishes the data to the Bosch IoT Suite.

![Generators](/images/release_notes/0.10.0-m2/webeditor_details.png)

* * *

### Eclipse Kura Generator

Eclipse Kura generator supports the generation of a Kura gateway OSGI bundle that reads device data via bluetooth LE technology and sends the data to either Kura built-in Cloud Services or the Bosch IoT Suite.

![Generators](/images/release_notes/0.10.0-m2/webeditor_details.png)

* * *

### Web-based Device Dashboard Generator

The Web - based Device Dashboard generator creates a Spring Boot Web application that is able to visualise the device data with various UI components as well as consume the data from a IoT Cloud backend, e.g. Bosch IoT Suite.

![Generators](/images/release_notes/0.10.0-m2/webeditor_details.png)

* * *

### Repository Java Client

With the Repository Java Client you can find information models and process its content as well as generate code for various IoT platforms. [Click here for more info](https://github.com/eclipse/vorto/tree/development/server/repo/repository-java-client/Readme.md).

<br />

## 0.9.x

We are proud to announce the release of 0.9. Here are some of the major highlights:

*   **Eclipse IoT Toolset** that lets you describe the characteristics and functionalities of devices as Information Models
*   **Information Model Repository**, acting as a device description dictionary to lookup and find device cababilities by various criteria
*   **Generator API** that lets you define the translation rules from Information Models into executable, IoT platform specific source code
*   Many **Code Generators** to inspire you to build your own generator as well as illustrate the usage to build an IoT Application.
*   **Command Line Interface** that lets you lookup, find and publish information models as well as generate code for a selected IoT platform

### Eclipse IoT Toolset

With the **Eclipse IoT Toolset**, you can describe the characteristics and functionality of devices as Information Models, publish them to the Information Model Repository and generate code with the help of Code Generators.

![Vorto Features Models](/images/release_notes/0.9.x/vorto_features_models.png)

### Information Model Repository

The **Information Model Repository** manages your device descriptions in a central place and exposes its functionality as a clean REST-API. Users are able to lookup and search device cababilities, checkin new models as well as translate Information Models into a selected IoT Platform source code.

![Search page](/images/release_notes/0.9.x/vorto_repository_search.png)

### Generator API

Vorto Code Generators convert an Information Model into executable IoT platform specific source code. The **Generator API** lets you plug in your own code generator into the toolset. Toolset wizards help you through this process.

### Code Generators

In order to inspire you to build you own generator for your platform, we implemented many **Code Generators**, for example Bosch IoT Things, AWS, ThingWorx and many more. They illustrate the many possibilities of how code generators can be effectively used and act as a blueprint for other generator implementations.

![Generators](/images/release_notes/0.9.x/vorto_repository_generators.png)

### Command Line Interface (CLI)

A **Command Line Interface Tool** lets you explore information models, checkin new information models as well as generate code for a selected IoT platform.

![Vorto CLI Tool](/images/release_notes/0.9.x/vorto_features_cli_tool.png)
