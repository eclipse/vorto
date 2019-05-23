#!/bin/bash -e
#
# this script is to repackage generator jar with neccessory images
#
pwd

# copying the official generators to the aws-upload tmp folder
echo "process to extract and include images from s3 img/official folder"
cp ./aws-upload/gen/generator-runner-exec.jar ./aws-upload/tmp
cd aws-upload/tmp
jar -xvf generator-runner-exec.jar
rm -f generator-runner-exec.jar
aws s3 cp s3://$VORTO_S3_BUCKET/img/official ./BOOT-INF/classes/img --recursive
jar -cvmf0 META-INF/MANIFEST.MF generator-runner-exec-withimg.jar .

echo "copying the jar with images to aws-upload folder"
cp generator-runner-exec-withimg.jar ../${GEN_ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar
cd ../..

echo "pwd $(pwd)"
ls -l ./aws-upload

# uploading to s3
echo "uploading to s3 bucket"
aws s3 cp ./aws-upload/${GEN_ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar s3://$VORTO_S3_BUCKET --acl "private" --storage-class "STANDARD_IA" --only-show-errors --no-guess-mime-type

# updating the application-version
echo "versioning the artifact in EBS"
aws elasticbeanstalk create-application-version --application-name "Vorto-Dev-Environment" --no-auto-create-application --version-label "build-job_${ELASTIC_BEANSTALK_LABEL}_official_gen" --description "Build ${TRAVIS_JOB_NUMBER} - Git Revision ${TRAVIS_COMMIT_SHORT} for offical generators" --source-bundle S3Bucket="$VORTO_S3_BUCKET",S3Key="${GEN_ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar"

# updating  environment
echo "updating environment in EBS"
aws elasticbeanstalk update-environment --application-name "Vorto-Dev-Environment" --environment-name "vorto-official-generators-dev" --version-label "build-job_${ELASTIC_BEANSTALK_LABEL}_official_gen"

echo "finished running repackage-deploy-gen.sh"

