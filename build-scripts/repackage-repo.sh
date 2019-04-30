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
echo "copying both repo and gen artifacts to specific folders"
cp ./repository/repository-server/target/infomodelrepository.jar ./aws-upload/repo
cp ./generators/generator-runner/target/generator-runner-exec.jar ./aws-upload/gen

# copying the repository artifact to the aws-upload
echo "copying the repo artifact to tmp"
cp ./aws-upload/repo/infomodelrepository.jar ./aws-upload/tmp
cd aws-upload/tmp
jar -xvf infomodelrepository.jar
rm -f infomodelrepository.jar
cp ../../wgetDownload/mariadb-java-client-2.3.0.jar ./BOOT-INF/lib/
jar -cvf infomodelrepository-dbclient.jar .
cd ../..
echo "pwd : $(pwd)"
ls -l
cp ./aws-upload/tmp/infomodelrepository-dbclient.jar ./aws-upload/${ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar
rm -rf ./aws-upload/tmp/*
ls -l ./aws-upload

exit 1

# copying the official generators to the aws-upload folder
cp ~/generators/generator-runner/target/generator-runner-exec.jar ~/aws-upload/${GEN_ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar

# list the contents of aws-upload folder
ls -al ~/aws-upload

