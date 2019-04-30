#!/bin/bash -e
#
# this script is to repackage generator jar with neccessory images
#
pwd

# copying the official generators to the aws-upload tmp folder
cp ./aws-upload/gen/generator-runner-exec.jar ./aws-upload/tmp
cd aws-upload/tmp
jar -xvf generator-runner-exec.jar
rm -f generator-runner-exec.jar
aws s3 cp s3://$VORTO_S3_BUCKET/img/official ./BOOT-INF/classes/img --recursive
jar -cvf generator-runner-exec-withimg.jar .
cp generator-runner-exec-withimg.jar ../${GEN_ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar
cd ../..

