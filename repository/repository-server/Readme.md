# Vorto Repository

The Vorto repository manages your device descriptions (Information Models). Users are able to look up device characteristics and services as well as convert the information models into executable, platform - specific code with the help of Code Generators. 

If you would like to find out more about available code generators, to set them up and use them directly from your repository, visit our [Vorto Code Generator Overview](../../generators/Readme.md).

## GitHub OAuth App
- Open https://github.com/settings/developers
- Under Settings->Developer Settings->OAuth Apps click `Register a new application`
- Fill in the `Application name` with what ever you want, we are using `Vorto Local`
- Fill in the `Homepage URL` with `https://localhost:8443/infomodelrepository/`
- Fill in the `Authorization callback URL` with `https://localhost:8443/infomodelrepository`
- Copy the shown `Client ID` and `Client Secret` for use in the next section

## Build & Run

#### In-memory H2 database (Default):

By default, the Vorto repository is configured to run with in-memory hsql database. Open your command line client and navigate to /vorto/repository/repository-server. To start the Vorto Repository locally run the below command.

    mvn spring-boot:run -Dspring.profiles.active=local-https -Dgithub_clientid=<YOUR_CLIENT_ID> -Dgithub_clientSecret=<YOUR_CLIENT_SECRET> -Deidp_clientid=ciamids_12345 -Deidp_clientSecret=12345

If you are behind a corporate proxy add additional proxy parameters 

    -Dhttps.proxyHost=<PROXY_HOST_NAME> -Dhttps.proxyPort=<PROXY_PORT> -Dhttps.proxyUser=<PROXY_USER> -Dhttps.proxyPassword=<PROXY_PASSWORD>


Once you see the message `Started VortoRepository in xx.xxx seconds`. Open your browser and navigate to:

	 https://localhost:8443/infomodelrepository 

----------
Back to [Vorto Server Overview](../../Readme.md)

