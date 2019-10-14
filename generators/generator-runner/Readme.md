# Set up local Generator Test Infrastructure

In order to test the generators locally, you can leverage the GeneratorRunner project, which is essentially a SpringBoot application that aggregates 
all available Vorto generator plugins and exposes a Plugin API V1 and V2 interface. 

Here are the steps in more detail:

1. Clone this repository
1. Download and start `Eclipse IDE for Java and DSL Developers`
1. In Eclipse click `File -> Import`
1. Select wizard: `Maven -> Existing Maven Project` then click Next
1. Browse to `vorto project directory`
1. Select project: `generators/pom.xml` then click Finish
1. In Eclipse click `Run -> Run Configurations` and add the following two run configurations:

## 1. Package Generator
Click `Maven Build -> New Configuration` and enter the following configuration, depending on which generator you want to work with: 
* Base directory: Choose location of generator you want to work with.
* Goals: `package`
* User settings: This variable should point to your maven user settings.
* (Optional) Check `Offline` to skip downloads.

Apply and run the configuration. Once you see a 'BUILD SUCCESS' in the console you can proceed with the following steps.

## 2. Configure and start the Generator Runner
Click `Maven Build -> New Configuration` and enter the following configuration to start the generator runner:
* Base directory: e.g. `${workspace_loc:/generators/generator-runner}`
* Goals: `spring-boot:run`
* User settings: This variable should point to your maven user settings.
* Check `Resolve Workspace artifacts`
* (Optional) Check `Offline` to skip downloads

Apply and run the configuration. Once you see `Started GeneratorRunner in <x> seconds ...` you can continue to configure and to start the repository server locally. 

## 3. Configure and start the Repository Server
1. Edit [application-local-https.yml](https://github.com/eclipse/vorto/blob/development/repository/repository-server/src/main/resources/application-local-https.yml "application-local-https.yml") of the repository-server. Comment out the ```plugins``` declaration for AWS and activate it for local generator runner, indicated by the comment
1.  Start the repository-server as described here: [https://github.com/eclipse/vorto/tree/development/repository/repository-server](https://github.com/eclipse/vorto/tree/development/repository/repository-server)

Happy Testing! 