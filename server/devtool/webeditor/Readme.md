# Vorto Web Editor

The Vorto web editor is a web application that can be used to create, edit, store and publish technology agnostic, abstract device descriptions, so called information models. The web editor has been designed to offer functionality similar to the [Vorto Toolset](http://www.eclipse.org/vorto/documentation/installation/installation.html#installing-the-vorto-toolset) and can be used as an alternative.

The web editor can also be configured to interact with the Vorto Repository. This not only enables the user to use the existing function blocks and datatypes from the repository while describing new devices but also publish these device descriptions to the repository.

Once published the user can convert the information models into executable, platform - specific code with the help of Code Generators.

## Contents

#### 1. [Setup](#setup)
#### 2. [Build and Run](#build-and-run)
#### 3. [Tutorial](#tutorial)

## Setup

#### Step 1. Create a Github OAuth Application

The web editor uses Github OAuth to authenticate users. Follow the instructions [here](https://developer.github.com/apps/building-integrations/setting-up-and-registering-oauth-apps/registering-oauth-apps) to create an OAuth application on Github.

You can use the following configuration for your development setup.

    Homepage URL:
      http://localhost:9080

    Authorization Callback URL:
      http://localhost:9080

#### Step 2. Configure the Github clientId and clientSecret.

Use the Github clientId and clientSecret from the previous step to configure your setup (local/cloud). Update these credentials in either the **application-local.yml** or  **application-cloud.yml** file.

    github:
      client:
        clientId: ${github_clientid}
        clientSecret: ${github_secret}


#### Note

If you would like to use the default configuration for the web editor, you can directly jump to [Build and Run](#build-and-run).

#### Step 3. Configure the Vorto Repository URL.

By default, the local setup points to the Vorto Repository hosted on localhost. You can update the Vorto Repository to the new URL in the appropriate **.yml** file.

    vorto:
      repository:
        base:
          path: ${vorto_repository_url}


#### Step 4. Configure the web editor project location

The web editor uses file based persistence to store the files on the server. The location can be configured at **application.properties** file.

    project.repository.path = ${new_location_path}

## Build and Run

In the command prompt, invoke

    mvn clean install spring-boot:run

Use your browser to open the URL:

    http://localhost:9080

## Tutorial
You can refer to the tutorial [here](https://github.com/eclipse/vorto/blob/development/tutorials/tutorial-create_and_publish_with_web_editor.md).
