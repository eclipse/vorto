# Running Lambda Functions on your local Machine

The Vorto generators plugins are Lambdas that can be executed on AWS. When developing or testing 
generators, it might be cumbersome to only be able to test your changes directly on AWS. Testing on 
your local machine can be much faster and easier, especially if you are trying something new and don't 
want to repeatedly deploy to AWS. 

AWS SAM allows you to run Lambda functions on your local machine. This guide shows how you can run 
your Vorto generators locally using AWS SAM on a Linux machine. 

## Prerequisites: 
1) You have already installed Docker
2) You have cloned the Vorto Github repository and you are able to build using Maven.

## Steps: 
1) Install Homebrew and the AWS SAM CLI following the 
[official guide](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-install-linux.html).
You only need to do steps 4 and 5 to run Lambdas locally.  
2) Open your terminal and navigate to <Vorto Git Root>/generators/generator-lambda. There you will
find the sam-local.template file. This file has the generator Lambdas and their APIs preconfigured. 

The generators consist of two separate Lambda functions - one for retrieving the plugin information 
(VortoGeneratorsPluginInfo) and one for the execution of a generator (VortoGeneratorsExecutor). 
You can invoke both functions directly using the AWS SAM CLI and you can use the local Lambda
functions directly in your Vorto Repository. 

## Invoking a generator from the CLI: 
Navigate to <Vorto Git Root>/generators/generator-lambda in your terminal. Then you can run the 
command to invoke a Lambda once: 

    sam local invoke -t sam-local.template "VortoGeneratorsExecutor"

The output should look like this: 

    Invoking org.eclipse.vorto.plugins.generator.lambda.executor.GeneratorExecutionHandler (java8)
    Decompressing /home/kevin/git-repos/bsinno/vorto/generators/generator-lambda/generator-lambda-executor/target/generator-lambda-executor-1.0.0-SNAPSHOT.jar
    
    Fetching lambci/lambda:java8 Docker container image......
    Mounting /tmp/tmp0xbu5rb6 as /var/task:ro,delegated inside runtime container
    START RequestId: c372a100-a6c0-120f-806b-3fa65f44dc1f Version: $LATEST

You can see that the JAR file containing the Lambda function is decompressed, a docker container is 
started and the function starts executing in the docker container. For the Vorto generators to work, 
you need to pass an event containing the generator info and in case of the VortoGeneratorsExecutor 
also the model data. Events can be passed using the -e option with the sam local invoke command. 
Just create a JSON file and reference it with the -e option.  

### Debugging 

To debug the local Lambda functions, just add the debug parameter to the invocation command and
specify a port for the debugger to connect to. The execution will halt and wait for a debugger to
connect.

    sam local invoke -t sam-local.template "VortoGeneratorsExecutor" -d 5858

## Using a local generator in the Vorto Repository: 

To use the local generator in your Vorto Repository, you need to run the Lambda functions 
VortoGeneratorsExecutor and VortoGeneratorsPluginInfo together on the same port - that is possible 
using an API Gateway. The sam-local.template file is preconfigured to do that. To start the API 
Gateway and the Lambda functions execute the start-api command in your 
<Vorto Git Root>/generators/generator-lambda directory: 

    sam local start-api -t sam-local.template 

The output should look like this: 

    Mounting VortoGeneratorsExecutor at http://127.0.0.1:3000/api/2/plugins/generators/{pluginkey} [PUT]
    Mounting VortoGeneratorsPluginInfo at http://127.0.0.1:3000/api/2/plugins/generators/{pluginkey}/info [GET]
    You can now browse to the above endpoints to invoke your functions. You do not need to restart/reload SAM CLI while working on your functions, changes will be reflected instantly/automatically. You only need to restart SAM CLI if you update your AWS SAM template
    2020-01-08 16:44:03  * Running on http://127.0.0.1:3000/ (Press CTRL+C to quit)
    
Now you can access the Lambda function through the API Gateway on the URL and port shown in the 
console output. To configure your Vorto Repository to use the local generators, the "plugins"
SpringBoot property needs to be adjusted, to point to the URL and port shown on the console
 (in this example http://127.0.0.1:3000). When that is done, the Vorto Repository can be started - 
 it uses the local generators now. 
 
### Debugging

The debug option also works with the start-api command.

    sam local start-api -t sam-local.template -d 5858
 
### Caution: 

The performance of local generators can be significantly slower - for each call to a 
 Lambda function, a Docker container will be started, this will take longer than on AWS - especially
 when there are multiple consecutive calls to Lambdas, as when loading the details page of a model, 
 with multiple generator plugins configured. If you only want to work with one generator, remove the 
 others from the "plugins" property and restart the Vorto Repository.

