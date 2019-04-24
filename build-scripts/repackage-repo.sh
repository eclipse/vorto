#!/bin/bash -e
#
# this script is to repackage repository jar with neccessory jars
#
echo "downloading mariadb using curl"
curl -o curlDownload/mariadb-connector-java-2.3.0.jar https://downloads.mariadb.com/Connectors/java/connector-java-2.3.0/mariadb-java-client-2.3.0.jar

echo "downloading mariadb using wget"
wget -P wgetDownload/ https://downloads.mariadb.com/Connectors/java/connector-java-2.3.0/mariadb-java-client-2.3.0.jar

exit 1
