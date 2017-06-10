# Vorto Devtool

The Vorto devtool is a web editor that can be used to create, edit, store and publish technology agnostic, abstract device descriptions, so called information models. The devtool has been designed to offer functionality similar to the [Vorto Toolset](http://www.eclipse.org/vorto/documentation/installation/installation.html#installing-the-vorto-toolset) and can be used as an alternative.

The devtool can also be configured to interact with the Vorto Repository. This not only enables the user to use the existing function blocks and datatypes from the repository while describing new devices but also publish these device descriptions to the repository.

Once published the user can convert the information models into executable, platform - specific code with the help of Code Generators.

## Setup

#### Step 1. Create a Github OAuth Application

The devtool uses Github OAuth to authenticate users. Follow the instructions [here](https://developer.github.com/apps/building-integrations/setting-up-and-registering-oauth-apps/registering-oauth-apps) to create an OAuth application on Github.

#### Step 2. Configure the Github clientId and clientSecret.

Use the Github clientId and clientSecret from the previous step to configure your setup (local/cloud). Update these credentials in either the **application-local.yml** or  **application-cloud.yml** file.

    github:
      client:
        clientId: ${github_clientid}
        clientSecret: ${github_secret}


#### Note

If you would like to use the default configuration for the devtool, you can directly jump to [Build and Run](#build-and-run).

#### Step 3. Configure the Vorto Repository url.

By default, the local setup points to the Vorto Repository hosted on localhost. You can update the Vorto Repository to the new url in the appropriate **.yml** file.

    vorto:
      repository:
        base:
          path: ${vorto_repository_url}


#### Step 4. Configure the Devtool project location

The devtool uses file based persistence to store the files on the server. The location can be configured at **application.properties** file.

    project.repository.path = ${new_location_path}

## Build and Run

In the command prompt, invoke

    mvn clean install spring-boot:run

Open your browser and open the URL:

    https://localhost:9443
