import argparse
import json
import sys
import yaml
from os import getenv
from pathlib import Path
from subprocess import check_call


def proxy_url_to_java(proxy_url):
    user = None
    password = None
    if 'http://' in proxy_url:
        proxy_url = proxy_url.replace('http://', '')
    elif 'https://' in proxy_url:
        proxy_url = proxy_url.replace('https://', '')
    if '@' in proxy_url:
        user_and_password, host_and_port = proxy_url.split("@")
        user, password = user_and_password.split(":")
    else:
        host_and_port = proxy_url
    host, port = host_and_port.split(":")
    return host, port, user, password


def main():
    # Read in arguments from cli
    parser = argparse.ArgumentParser(description='Python script to convert a docker enviroment to spring boot application data')
    group = parser.add_mutually_exclusive_group()
    group.add_argument('--generator', help='Handle the enviroment for a generator', action='store_true', default=False)
    group.add_argument('--repository', help='Handle the enviroment for a generator', action='store_true', default=True)
    args = parser.parse_args()
    # argparse takes care of the two arguments not being passed at the same time
    is_generator = args.generator
    if not is_generator:
        is_repository = args.repository
    else:
        is_repository = False


    config_yaml_file = Path("./config/application.yml")
    java_args = ["java","-cp",".:lib/*:infomodelrepository.jar"]
    args = {}
    profile = getenv("PROFILE", "local")
    args.update({"spring.profiles.active": profile})
    datasource = getenv("DATASOURCE", "mysql")
    authprovider = getenv("AUTH_PROVIDER", "github")
    spring_application = {}

    # generell options
    if config_yaml_file.is_file():
        print("Using application.yml")
        with open("./config/application.yml") as yaml_file:
            yaml_data = yaml.safe_load(yaml_file.read())
        spring_application.update(yaml_data)
    #Things that can be defaulted
    args.update({"server.port": getenv("VORTO_PORT", 8080)})
    args.update({"server.contextPath": getenv("CONTEXT_PATH", "/")})
    args.update({"server.config.generatorUser": getenv("GENERATOR_USER", "vorto_generators")})
    args.update({"suite_clientid": "123"})
    args.update({"suite_clientSecret": "123"})
    args.update({"server.config.generatorUser": getenv("GENERATOR_USER", "vorto_generators")})
    if getenv("GENERATOR_PASSWORD"):
        args.update({"server.config.generatorPassword": getenv("GENERATOR_PASSWORD")})
    else:
        print("No Generator Password set, generators won't work")

    # repository settings
    if is_repository:
        if getenv("http_proxy") or getenv("https_proxy"):
            # www.gnu.org/software/wget/manual/html_node/Proxies.html
            # No proxy list for http and https
            if getenv("no_proxy"):
                no_proxy_hosts = getenv("no_proxy").replace(",","|")
                args.update({"http.nonProxyHosts": no_proxy_hosts})
                args.update({"https.nonProxyHosts": no_proxy_hosts})
            # http proxy
            http_proxy = getenv("http_proxy")
            if http_proxy:
                http_host, http_port, http_user, http_password = proxy_url_to_java(http_proxy)
                args.update({"http.proxyHost": http_host})
                args.update({"http.proxyPort": http_port})
                if http_user:
                    args.update({"http.proxyUser": http_user})
                if http_password:
                    args.update({"http.proxyPassword": http_password})

            # https proxy
            https_proxy = getenv("https_proxy")
            if https_proxy:
                https_host, https_port, https_user, https_password = proxy_url_to_java(https_proxy)
                args.update({"https.proxyHost": https_host})
                args.update({"https.proxyPort": https_port})
                if https_user:
                    args.update({"https.proxyUser": https_user})
                if https_password:
                    args.update({"https.proxyPassword": https_password})
        if getenv("ADMIN_USER"):
            args.update({"server.admin": getenv("ADMIN_USER")})
        else:
            print("No admin user defined, use ADMIN_USER")
            sys.exit(1)
        if authprovider == "github":
            if getenv("GITHUB_CLIENT_ID"):
                args.update({"github_clientid": getenv("GITHUB_CLIENT_ID")})

                print("Warning defaulting eidp clientid to an empty string")
                args.update({"eidp_clientid": getenv("EIDP_CLIENT_ID", " ")})
            else:
                print("Github client id is missing")
                sys.exit(1)
            if getenv("GITHUB_CLIENT_SECRET"):
                args.update({"github_clientSecret": getenv("GITHUB_CLIENT_SECRET")})
                print("Warning defaulting eidp client secret to an empty string")
                args.update({"eidp_secret": getenv("EIDP_CLIENT_SECRET", " ")})
            else:
                print("Github client secret is missing")
                sys.exit(1)
        elif authprovider == "bosch":
            if getenv("EIDP_CLIENT_ID"):
                args.update({"eidp_clientid": getenv("EIDP_CLIENT_ID")})
            else:
                print("eidp client id is missing")
                sys.exit(1)
            if getenv("EIDP_CLIENT_SECRET"):
                args.update({"eidp_secret": getenv("EIDP_CLIENT_SECRET")})
            else:
                print("EIDP client secret is missing")
                sys.exit(1)

        if datasource == "mysql":
            with open("./config/application.yml") as yaml_file:
                yaml_data = yaml.safe_load(yaml_file.read())
            spring_application.update(yaml_data)
            args.update({"spring.datasource.url": getenv("MYSQL_URL", "jdbc:mysql://db:3306/vorto")})
            args.update({"spring.datasource.username": getenv("MYSQL_USER", "root")})
            args.update({"spring.datasource.password": getenv("MYSQL_ROOT_PASSWORD")})
            args.update({"spring.datasource.driver-class-name": "org.mariadb.jdbc.Driver"})
    #generator settings
    if is_generator:
        if getenv("SERVICE_URL"):
            args.update({"server.serviceUrl": getenv("SERVICE_URL")})
        else:
            print("please pass the SERVICE_URL to the repository")
        if getenv("VORTO_URL"):
            args.update({"vorto.serverUrl": getenv("VORTO_URL")})
        else:
            print("please pass the VORTO_URL to the repository")

    #run java
    args.update({"spring.application.json": json.dumps(spring_application)})
    for key, item in args.items():
        java_args.append("-D{}={}".format(key,item))
    if is_generator:
        java_args.append("-jar")
        java_args.append("generators.jar")
    else:
        #java_args.append("infomodelrepository.jar")
        java_args.append("org.springframework.boot.loader.JarLauncher")
    print(java_args)
    check_call(java_args)

main()
