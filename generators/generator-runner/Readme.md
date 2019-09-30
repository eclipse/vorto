# Set up local Generator Test Infrastructure

In order to test the generators locally, you can leverage the GeneratorRunner project, which is essentially a SpringBoot application that aggregates 
all available Vorto generator plugins and exposes a Plugin API V1 and V2 interface. 

Here are the steps in more detail:

1. Run the generator runner with ```mvn spring-boot:run``` . This will start the microservice under port 8081
2. Configure the Vorto Repository to point to the local GeneratorRunner for executing genertors
* Open the ```application-local-https.yml``` of the repository-server
* Comment out the ```plugins``` declaration for AWS and activate it for local generator runner, indicated by the comment
* Start the repository - server

Happy Testing! 


