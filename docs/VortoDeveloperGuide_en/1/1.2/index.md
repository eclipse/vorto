## System Requirements

For creating Eclipse extensions for Bosch M2M Toolchain code generator extension point the following software requirements must be met:

- Eclipse Kepler or higher  
  Please update your `eclipse.ini` file with the lines below to set the Java version and file encoding used for Xtext.  

        # Mandatory settings:

        # to set the Java version in osgi
        -Dosgi.requiredJavaVersion=1.7

        # Optional settings:    

        # to set file encoding when using Xtext   
        -Dfile.encoding=UTF-8   
        # to set the initial heap size used by Eclipse
        -Xms256m
        # to set the maximum size to which the Eclipse heap can grow   
        -Xmx1024m   
        # to set the PermGen space   
        -XX:PermSize=128m   
        
- [Eclipse Xtext plug](http://download.eclipse.org/modeling/tmf/xtext/updates/composite/releases/) 2.7.2

- [Eclipse M2E plug-in](http://download.eclipse.org/technology/m2e/releases) 1.5.0 (can already be packaged with Eclipse Luna)  

- [Oracle Java SE 7](http://www.oracle.com/technetwork/java/javase/downloads/)

<table class="note">
  <tr>
    <td class="notesign"><img src="../../images/Note_32.png", alt="Note"></td>
    <td>Validity period will endure at the most as long as the version of infrastructure software of third party manufacturers defined in this document (Operating Systems, Java, etc.) is publicly and officially supported.</br>
    We support the most recent patch releases of the respective software product version.</td>
  </tr>
</table>
