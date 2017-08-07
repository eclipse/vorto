[![Build Status](https://travis-ci.org/eclipse/vorto.svg?branch=development)](https://travis-ci.org/eclipse/vorto)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/569649bfe2594bedae2cd172e5ee0741)](https://www.codacy.com/app/alexander-edelmann/vorto?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=eclipse/vorto&amp;utm_campaign=Badge_Grade)
[![Quality Gate](https://sonarqube.com/api/badges/gate?key=org.eclipse.vorto%3Aorg.eclipse.vorto.parent%3Adevelopment)](https://sonarqube.com/dashboard/index/org.eclipse.vorto:org.eclipse.vorto.parent:development) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/org.eclipse.vorto.parent/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.eclipse.vorto/org.eclipse.vorto.parent)

# Getting started with Vorto

[Vorto](http://www.eclipse.org/vorto) is an IoT development infrastructure that lets you describe devices using a simple language and publish and manage these descriptions as Information Models in a centralized [Vorto Repository](http://vorto.eclipse.org). Vorto provides many [generators](http://vorto.eclipse.org/#/generators) that let you easily create device-specific source code in order to integrate the device into IoT platforms.
 
Wanna learn more? Browse through the [tutorials](tutorials/Readme.md).

## Connect a device with Vorto 


1. Open the [Vorto Repository](http://vorto.eclipse.org)
2. Search for [_Bosch GLM_](http://vorto.eclipse.org/#/details/com.bosch/BoschGLM100C/1.0.0)
3. Select _Kura Generator_, choose your configuration (e.g. BLE) and confirm with _Generate_.
4. Download the generated Kura bundle and import it into your Eclipse
4. **That's it**! You just created a device-specific OSGI Kura bundle to connect the device for a selected IoT cloud backend.

## Describe a new device 

The [Vorto web editor](http://vorto.eclipse.org/editor) makes it very easy to describe a device and publish it as an information model to the Vorto Repository. Just follow the [tutorial](tutorials/tutorial-create_and_publish_with_web_editor.md) to learn more.

# Vorto Repository Server

The [Eclipse Vorto Repository](http://vorto.eclipse.org) manages many different information models as well as Code Generators. 

However, if you would like to setup your own local repository including generators, you can do that in just a couple of minutes:

 - Set up [Vorto Repository](server/repo/repository-server/Readme.md)
 - Set up [Vorto Code Generators](server/generators/Readme.md)
 - Access models and generate code via the [Java Client API ](server/repo/repository-java-client/Readme.md)

# Documentation

Read our [Vorto Documentation](http://www.eclipse.org/vorto/documentation/overview/introduction.html)

# Contact us
 - You want to chat with us ? [![Join the chat at https://gitter.im/eclipse/vorto](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/eclipse/vorto?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
 - You have problems with Vorto ? Open a [GitHub issue](https://github.com/eclipse/vorto/issues)
 - Find out more about the project on our [Vorto Homepage](http://www.eclipse.org/vorto)
 - Reach out to our developers on our [Discussion Forum](http://eclipse.org/forums/eclipse.vorto) 

# Contribute to the Project

Make sure, that you have installed [Vorto for contributors](tutorials/tutorial_vortosetup_contributors.md)

When you create a Pull Request, make sure:

1. You have a valid CLA signed with Eclipse
2. All your commits are signed off (git commit -s)
3. Your commit message contains "Fixes #`<Github issue no>`
4. Target to merge your fix is development branch
