---
layout: documentation
title: Troubleshooting
---
{% include base.html %}
## Troubleshooting

This section details the following topics:

[Installation Issues](#installation-issues)  

[General Issues](#general-issues)

## Installation Issues

##### I Get an Error During the Installation That it Cannot Perform the Operation and is Trying for Alternative Solutions

You need to uninstall the Vorto Toolset as well as its dependent Xbase & Xtext plug-ins (refer to [Uninstalling the Vorto Toolset]({{base}}/documentation/appendix/uninstallation.html#uninstalling-the-vorto-plug-ins)).

##### Should I Upgrade the Xtext Plug-in?

This is not necessary, but even if you update to the latest version of *Xtext*, you should be able to work smoothly, since we assume that every *Xtext* update is backward compatible.

## General Issues

##### The Function Block Project Wizard Does Not Appear Under New Projects

Please make sure you are running Eclipse with Java 1.7. To check the Java version your IDE run with, proceed as follows

1. In the main menu click **Help > About Eclipse**.  
   The **About Eclipse** window opens.
2. Click the **Installation Details** button.  
   The **Eclipse Installation Details** window opens.
3. Click the **Configuration tab**.
4. Locate the line beginning with java.runtime.version=1.7.*. Make sure the version start with 1.7. Otherwise you can add the line below to your eclipse.ini file to specify your Java version that should be used to launch the IDE (e.g.):

        openFile
        -vm
        C:\application\Java1.7.51\bin\javaw.exe
        --launcher.appendVmargs

### When I build a function block, the system throws an out of memory error, or returns with exit code "-1"

It is likely the build requires more memory. Build internal uses maven to generate java artifacts and first time it usually need to download a lot of dependencies and require more memory than default setting. Please add below lines to your eclipse.ini file if such error occur.

     -vm  
     \<path\_to\_your\_java\_installation\>/bin/javaw.exe
     -vmargs
     -DMAVEN_OPTS=-Xms256m -XX:MaxPermSize=1024m -Xmx1024m
