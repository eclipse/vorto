#!/bin/bash -e
#
# this script is to repackage repository jar with neccessory jars
#
pwd
export GIT_BRANCH=$TRAVIS_BRANCH
echo "this is branch name: $GIT_BRANCH"

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

if [[ "$GIT_BRANCH" == "master" ]]
then
   aws s3 cp s3://$VORTO_S3_BUCKET/files_for_infinispan/prod ./BOOT-INF/classes --recursive
elif [[ "$GIT_BRANCH" == "development" ]]
then
  aws s3 cp s3://$VORTO_S3_BUCKET/files_for_infinispan/dev ./BOOT-INF/classes --recursive
else
  echo "no extra files are include from S3Bucket"
fi

jar -cvmf0 META-INF/MANIFEST.MF infomodelrepository-dbclient.jar .
cd ../..
echo "pwd : $(pwd)"
ls -l
cp ./aws-upload/tmp/infomodelrepository-dbclient.jar ./aws-upload/${ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar
rm -rf ./aws-upload/tmp/*

# list the contents of aws-upload folder
ls -l ./aws-upload

if [[ "$GIT_BRANCH" == "master" ]]
then
  # uploading to s3 bucket
  echo "uploading to s3 bucket"
  aws s3 cp ./aws-upload/${ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar s3://$VORTO_S3_BUCKET --acl "private" --storage-class "STANDARD_IA" --only-show-errors --no-guess-mime-type

  # versioning the artifact in EBS
  echo "versioning the artifact at EBS"
  aws elasticbeanstalk create-application-version --application-name "Vorto-Prod-Environment" --no-auto-create-application --version-label "build-job_${ELASTIC_BEANSTALK_LABEL}_repo" --description "Build ${TRAVIS_JOB_NUMBER} - Git Revision ${TRAVIS_COMMIT_SHORT} for repository in prod" --source-bundle S3Bucket="$VORTO_S3_BUCKET",S3Key="${ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar"

  # updating environment in EBS
  echo "update environment in EBS"
  aws elasticbeanstalk update-environment --application-name "Vorto-Prod-Environment" --environment-name "vorto-prod" --version-label "build-job_${ELASTIC_BEANSTALK_LABEL}_repo"
elif [[ "$GIT_BRANCH" == "development" ]]
then
  # uploading to s3 bucket
  echo "uploading to s3 bucket"
  aws s3 cp ./aws-upload/${ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar s3://$VORTO_S3_BUCKET --acl "private" --storage-class "STANDARD_IA" --only-show-errors --no-guess-mime-type

  # versioning the artifact in EBS
  echo "versioning the artifact at EBS"
  aws elasticbeanstalk create-application-version --application-name "Vorto-Dev-Environment" --no-auto-create-application --version-label "build-job_${ELASTIC_BEANSTALK_LABEL}_repo" --description "Build ${TRAVIS_JOB_NUMBER} - Git Revision ${TRAVIS_COMMIT_SHORT} for repository in dev" --source-bundle S3Bucket="$VORTO_S3_BUCKET",S3Key="${ARTIFACT_NAME}_${ELASTIC_BEANSTALK_LABEL}.jar"

  # updating environment in EBS
  echo "update environment in EBS"
  aws elasticbeanstalk update-environment --application-name "Vorto-Dev-Environment" --environment-name "vorto-dev" --version-label "build-job_${ELASTIC_BEANSTALK_LABEL}_repo"
else
  echo "the artifact is not deployed to either production or development environment in AWS"
fi
echo "finished running repackage-deploy-repo.sh"

