#!/bin/bash -e
#
# this script is to repackage repository jar with neccessory jars
#
pwd

# copying the mariadb java client
mkdir ./wgetDownload
wget -P ./wgetDownload/ https://downloads.mariadb.com/Connectors/java/connector-java-2.3.0/mariadb-java-client-2.3.0.jar

# setting up artifact uploads to aws
mkdir -p ./aws-upload/{tmp,repo,gen}

# copying the repo and gen to respective folders
cp ./repository/repository-server/target/infomodelrepository.jar ./aws-upload/repo
cp ./generators/generator-runner/target/generator-runner-exec.jar ./aws-upload/gen

# copying the repository artifact to the aws-upload
cp ./aws-upload/repo/infomodelrepository.jar ./aws-upload/tmp
cd aws-upload/tmp
jar -xf infomodelrepository.jar
rm -f infomodelrepository.jar
cp ../../wgetDownload/mariadb-java-client-2.3.0.jar ./BOOT-INF/lib/
jar -cf infomodelrepository-dbclient.jar .
cd ../..
cp ./aws-upload/tmp/infomodelrepository-dbclient.jar ~/aws-upload/${ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar
rm -rf ./aws-upload/tmp/*
ls -l ./aws-upload

exit 0

# copying the official generators to the aws-upload folder
cp ~/generators/generator-runner/target/generator-runner-exec.jar ~/aws-upload/${GEN_ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar

# list the contents of aws-upload folder
ls -al ~/aws-upload

