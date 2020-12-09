
# Vorto Docker Images

## Quickstart

Eager to try out the vorto model repository? This section gives a good introduction on howto set up your repository in a couple of minutes.
It assumes you already have `docker-compose` installed and created an GitHub OAuth App. If you do not, head over to the [docker installation](https://docs.docker.com/compose/install/).

Also a valid oauth token is required, follow the GitHub OAuth App section lower down on this page to obtain one.

Everything read? Great lets get you started:

To get the docker-compose file clone the github repository by running:
`git clone https://github.com/eclipse/vorto.git vorto`

After thats done we need to update the `vorto-variables.env` its located under `docker/vorto-variables.env`

Update all the placeholders and save your progress, it should look something like this:

```bash
# Generell settings
GENERATOR_PASSWORD=<your_generator_password>

# Repository
ADMIN_USER=<your_github_username>
GITHUB_CLIENT_ID=<your_github_client_id>
GITHUB_CLIENT_SECRET=<your_github_client_secret>

# Generators
VORTO_URL=http://repository:8080

#proxy
#http_proxy=http://user:password@proxy.com:port
#https_proxy=http://user:password@proxy.com:port
#no_proxy=localhost,generators,db

# database
MYSQL_ROOT_PASSWORD=<your_db_password>
```

Do not forget to uncomment the proxy variables, if you need them.
See the table below for all options.

After we configured our enviroment we are ready to run `docker-compose up`.
This should launch make the repository reachable under [http://localhost:8080/](http://localhost:8080).

To get some models to play around with, login

![login view](docs/inital_view.png)

Go to import

![import button](docs/import_highlight.png)

And upload `docker/MyFirstModel_1.0.0.zip`

![import view](docs/import_view.png)

Thats it you now should have a working instance of the Vorto Repository.

## Configure 3rd party generators

Generators using APIv2 are registered via a base64 encoded string, containing a JSON object, stored in the environment variable `PLUGINS`. The stored array of plugins looks like the following.

```json
[
    {
        "key": "mygenerator",
        "pluginType": "generator",
        "apiVersion": "2",
        "endpoint": "http://3rd-party-generator:8080"
    }
]
```

The generator might be provided via AWS, Docker or anything else you can imagine.

Some plugins that are also used on [vorto.eclipseprojects.io](https://vorto.eclipseprojects.io) can be found under `/repository/repository-server/target/classes/application-local-https.yml`.

## Configuration environment variables

| Variable name          | default/required             | used in repository       | used in generator runner | description                                                                                                                                                                                                                          |
|------------------------|------------------------------|--------------------------|--------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `ADMIN_USER`           | :heavy_check_mark:           | :heavy_check_mark:       | :heavy_multiplication_x: | The username of the admin user                                                                                                                                                                                                       |
| `GITHUB_CLIENT_ID`     | :heavy_check_mark:           | :heavy_check_mark:       | :heavy_multiplication_x: | GitHub Oauth client id.  Only required when using `github` as auth provider.                                                                                                                                                         |
| `GITHUB_CLIENT_SECRET` | :heavy_check_mark:           | :heavy_check_mark:       | :heavy_multiplication_x: | GitHub Oauth client secret. Only required when using `github` as auth provider.                                                                                                                                                      |
| `EIDP_CLIENT_ID`       | :heavy_check_mark:           | :heavy_check_mark:       | :heavy_multiplication_x: | eidp Oauth client id.  Only required when using `eidp` as auth provider.                                                                                                                                                             |
| `EIDP_CLIENT_SECRET`   | :heavy_check_mark:           | :heavy_check_mark:       | :heavy_multiplication_x: | eidp Oauth client secret. Only required when using `eidp` as auth provider.                                                                                                                                                          |
| `VORTO_URL`            | :heavy_check_mark:           | :heavy_multiplication_x: | :heavy_check_mark:       | Url the repository is listing under, for the generators to register to.                                                                                                                                                              |
| `SERVICE_URL`          | :heavy_check_mark:           | :heavy_multiplication_x: | :heavy_check_mark:       | The url the generators are running under to provide a callback method for the repository                                                                                                                                             |
| `PLUGINS`              | :heavy_multiplication_x:     | :heavy_check_mark:       | :heavy_multiplication_x: | A base64 encoded string of a JSON array containing all registered plugins since APIv2                                                                                                                                                |
| `GENERATOR_PASSWORD`   | :heavy_check_mark:           | :heavy_check_mark:       | :heavy_check_mark:       | The password a generator uses to authenticate against the repository.                                                                                                                                                                |
| `GENERATOR_USER`       | `vorto_generators`           | :heavy_check_mark:       | :heavy_check_mark:       | The user a generator uses to authenticate against the repository.                                                                                                                                                                    |
| `VORTO_PORT`           | `8080`                       | :heavy_check_mark:       | :heavy_check_mark:       | The port used for the application running in the docker container.                                                                                                                                                                   |
| `CONTEXT_PATH`         | `/`                          | :heavy_check_mark:       | :heavy_check_mark:       | The context path used for the application running in the docker container.                                                                                                                                                           |
| `DATASOURCE`           | `mysql`                      | :heavy_check_mark:       | :heavy_multiplication_x: | The datasource used, possible values are `mysql` and `h2`                                                                                                                                                                            |
| `AUTH_PROVIDER`        | `github`                     | :heavy_check_mark:       | :heavy_multiplication_x: | The authentication provider used, possible values are `github` and `eidp`                                                                                                                                                            |
| `MYSQL_URL`            | `jdbc:mysql://db:3306/vorto` | :heavy_check_mark:       | :heavy_multiplication_x: | URL where the database is running                                                                                                                                                                                                    |
| `MYSQL_USER`           | `root`                       | :heavy_check_mark:       | :heavy_multiplication_x: | mysql user used                                                                                                                                                                                                                      |
| `MYSQL_ROOT_PASSWORD`  | :heavy_check_mark:           | :heavy_check_mark:       | :heavy_multiplication_x: | mysql password used                                                                                                                                                                                                                  |
| `MYSQL_DATABASE`       | `vorto`                      | :heavy_check_mark:       | :heavy_multiplication_x: | mysql database used                                                                                                                                                                                                                  |
| `http_proxy`           | :heavy_multiplication_x:     | :heavy_check_mark:       | :heavy_check_mark:       | Proxy variable that takes in an http proxy to tunnle requests through. This is required for any repository that runs behind a proxy to contact the oauth provider. Format: `http://user:password@proxy:port` or `http://proxy:port`  |
| `https_proxy`          | :heavy_multiplication_x:     | :heavy_check_mark:       | :heavy_check_mark:       | Proxy variable that takes in an http proxy to tunnle requests through. This is required for any repository that runs behind a proxy to contact the oauth provider. Format: `https://user:password@proxy:port` or `http://proxy:port` |
| `no_proxy`             | :heavy_multiplication_x:     | :heavy_check_mark:       | :heavy_check_mark:       | A comma seperated list of host for which to bypass the configured proxy. Keep in mind that the container have to talk to each other.                                                                                                 |

## GitHub OAuth App

* Open [the github developers page](https://github.com/settings/developers)
* Under Settings->Developer Settings->OAuth Apps click `Register a new application`
* Fill in the `Application name` with what ever you want, we are using `Vorto Local`
* Fill in the `Homepage URL` with `http://localhost:8080/`
* Fill in the `Authorization callback URL` with `http://localhost:8080/github/login`
* Copy the shown `Client ID` and `Client Secret` to your config.json

----------
Back to [Vorto Server Overview](../Readme.md)
