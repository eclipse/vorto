To run the ThingWorx Code Generator from Eclipse:
  * Configure Eclipse Luna for Vorto.
    * See https://www.eclipse.org/vorto/ for more information.
    * Instead of installing the stable version of the Vorto plugin, install the version from nightly builds.
    	* In, "Preferences ... Install/Update ... Available Software Sites," add http://download.eclipse.org/vorto/update/nightly-snapshots/.
    	* Call the new entry, "Vorto Nightly Snapshots."
    	* If you see an entry for http://download.eclipse.org/vorto/update/milestones/0.4.0_M1/, uncheck it.
    	* Go to "Help ... Check for Updates," in the Eclipse menu to install the latest nightly build.
    * Verify the installation by making sure there's a Vorto view available in Eclipse.
  * Run the ThingWorx Code Generator
    * Switch to the Java view.
    * Import the ThingWorxCodeGenerator project into your workspace.
    * Select the ThingWorxCodeGenerator project.
    * Go to, "Run ... Run As ... Eclipse Application."
    * A new instance of Eclipse will start. Switch to the Vorto view, and work with your Vorto entities.
    * When you are ready to generate a ThingWorx JSON file, right click on an Information Model and select, "Generate Code ... Invoke ThingWorx Code Generator."