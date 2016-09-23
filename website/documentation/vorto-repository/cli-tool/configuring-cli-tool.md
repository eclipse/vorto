---
layout: documentation
title: Configuring the Vorto CLI Tool
---
{% include base.html %}


##Configuring the Vorto CLI Tool

Before you can use the CLI Tool as a REST Client, you need to initialize basic preferences. By default the preferences are stored in the file `.vortocli.yaml.file`, which is located in your home directory.

For Windows,

	C:\Users\{$username}\.vortocli.yaml.file

For Mac,  

	/Users/{$username}/Documents/.vortocli.yaml.file

For Linux, 

	/usr/{$username}/Documents/.vortocli.yaml.file

You have to setup the CLI Tool in one of the following ways:

* Use the `vorto config` command.  
  To initialize vorto with additional values (or change them afterwards) like username, password, proxy or repository, use the command flags `-username`, `-password`, `-proxy` or `-repo`.  
  Example: `vorto config -username <username> -password <password> -repo http://vorto.eclipse.org`
* Edit the `.vortocli.yaml` file with a text editor.  
  ![CLI Yaml file]({{base}}/img/documentation/yamlfile.jpg)

<table class="table table-bordered">
    <tbody>
        <tr>
            <td><i class="fa fa-info-circle info-note"></i></td>
            <td>Enable the proxy in the configuration file <code>.vortocli.yaml</code> (Line 4) if you are accessing internet via proxy server.</td>
        </tr>
    </tbody>
</table>
