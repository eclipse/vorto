pipeline {
  agent any
    stages{
//      stage("Build"){
//        steps{
//          githubNotify context: 'Building PR', description: 'Building pull request',  status: 'PENDING'
//            // Maven installation declared in the Jenkins "Global Tool Configuration"
//            withMaven(
//                maven: 'maven-latest',
//                mavenLocalRepo: '.repository') {
//              sh 'mvn -P coverage clean install'
//            }
//          githubNotify context: 'Building PR', description: 'Building pull request',  status: 'SUCCESS'
//        }
//        post{
//          failure{
//            githubNotify context: 'Building PR', description: 'Building pull request',  status: 'FAILURE'
//          }
//        }
//      }
//      stage('Run compliance checks') {
//        parallel {
//          stage("SonarCloud"){
//            steps{
//              githubNotify context: 'SonarCloud', description: 'Running SonarCloud Scan',  status: 'PENDING', targetUrl: "https://sonarcloud.io/project/issues?id=org.eclipse.vorto%3Aparent&pullRequest=${CHANGE_ID}&resolved=false"
//                withMaven(
//                    // Maven installation declared in the Jenkins "Global Tool Configuration"
//                    maven: 'maven-latest',
//                    mavenLocalRepo: '.repository') {
//                  withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'TOKEN')]) {
//                    sh 'mvn -P coverage -Dsonar.projectKey=org.eclipse.vorto:parent -Dsonar.organization=vorto  -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$TOKEN -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.jacoco.reportPaths=target/jacoco.exec -Dsonar.language=java sonar:sonar -Dsonar.pullrequest.branch=$BRANCH_NAME -Dsonar.pullrequest.key=$CHANGE_ID -sonar.pullrequest.base=development'
//                  }
//                }
//              githubNotify context: 'SonarCloud', description: 'Running SonarCloud Scan',  status: 'SUCCESS', targetUrl: "https://sonarcloud.io/project/issues?id=org.eclipse.vorto%3Aparent&pullRequest=${CHANGE_ID}&resolved=false"
//            }
//            post{
//              failure{
//                githubNotify context: 'SonarCloud', description: 'Running SonarCloud Scan',  status: 'FAILURE', targetUrl: "https://sonarcloud.io/project/issues?id=org.eclipse.vorto%3Aparent&pullRequest=${CHANGE_ID}&resolved=false"
//              }
//            }
//          }
//          stage("CLMScan"){
//            steps{
//              githubNotify context: 'CLMScan', description: 'Running CLMScan',  status: 'PENDING'
//                withMaven(
//                    maven: 'maven-latest',
//                    mavenLocalRepo: '.repository') {
//                  catchError { //Todo remove as soon as nexus is fixed
//                    withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
//                      nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: selectedApplication('vorto-repository'), iqStage: 'build', jobCredentialsId: 'CLMScanUser'
//                        // add s3upload of nexus reports
//                        //      s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'repository/repository-server/target/**/*.pdf')
//                        //      s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'generators/generator-runner/target/**/*.pdf')
//                    }
//                  }
//                }
//              githubNotify context: 'CLMScan', description: 'Running CLMScan',  status: 'SUCCESS'
//            }
//            post{
//              failure{
//                githubNotify context: 'CLMScan', description: 'Running CLMScan',  status: 'FAILURE'
//              }
//            }
//          }
//          stage("AVScan infomodelrepository"){
//            steps{
//              githubNotify context: 'AVScan Infomodel', description: 'Running AntiVirus Scan on infomodelrepository.jar',  status: 'PENDING', targetUrl: ""
//                // Get Bosch pom files to run in an extra folder to keep the open source project clean and because the Bosch maven plugins can not be licensed under EPL
//                dir('avscan_infomodel') {
//                  //copy files over to the new maven folder to run AntiVirus Scans
//                  git url: "https://github.com/eclipsevorto-jenkins/vorto_compliance_jenkins.git"
//                  sh 'cp ../repository/repository-server/target/infomodelrepository.jar ./'
//                }
//              withMaven(
//                  maven: 'maven-latest',
//                  mavenLocalRepo: '.repository') {
//                  sh 'mvn verify -Dbosch.avscan.fileToScan=infomodelrepository.jar -f avscan_infomodel/pom_bosch.xml'
//                  withAWS(region:'eu-central-1',credentials:'aws-s3-vorto-jenkins-technical-user') {
//                    sh "sed -i -e \"s/http://sgpvmc0309.apac.bosch.com:8080//g\" avscan_infomodel/target/inl-releng-avsupport/avscan_report.html"
//                    s3Upload(file:'avscan_infomodel/target/inl-releng-avsupport/avscan_report.html', bucket:'pr-vorto-documents', path:"avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html")
//                  }
//              }
//              githubNotify context: 'AVScan Infomodel', description: 'Running AntiVirus Scan on infomodelrepository.jar',  status: 'SUCCESS', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html"
//            }
//            post{
//              failure{
//                githubNotify context: 'AVScan Infomodel', description: 'Running AntiVirus Scan on infomodelrepository.jar',  status: 'FAILURE', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html"
//              }
//            }
//          }
//          stage("AVScan generator-runner"){
//            steps{
//              githubNotify context: 'AVScan Generators', description: 'Running AntiVirus Scan on generator-runner-exec.jar',  status: 'PENDING', targetUrl: ""
//                // Get Bosch pom files to run in an extra folder to keep the open source project clean and because the Bosch maven plugins can not be licensed under EPL
//                dir('avscan_generator') {
//                  //copy files over to the new maven folder to run AntiVirus Scans
//                  git url: "https://github.com/eclipsevorto-jenkins/vorto_compliance_jenkins.git"
//                  sh 'cp ../generators/generator-runner/target/generator-runner-exec.jar ./'
//                }
//              withMaven(
//                  maven: 'maven-latest',
//                  mavenLocalRepo: '.repository') {
//                sh 'mvn clean verify -Dbosch.avscan.fileToScan=generator-runner-exec.jar -f avscan_generator/pom_bosch.xml'
//                  withAWS(region:'eu-central-1',credentials:'aws-s3-vorto-jenkins-technical-user') {
//                    sh "sed -i -e \"s/http://sgpvmc0309.apac.bosch.com:8080//g\" avscan_generator/target/inl-releng-avsupport/avscan_report.html"
//                    s3Upload(file:'avscan_generator/target/inl-releng-avsupport/avscan_report.html', bucket:'pr-vorto-documents', path:"avscans/${CHANGE_ID}/${BUILD_NUMBER}/generator-runner_report.html")
//                  }
//              }
//              githubNotify context: 'AVScan Generators', description: 'Running AntiVirus Scan on generator-runner-exec.jar',  status: 'SUCCESS', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/avscans/${CHANGE_ID}/${BUILD_NUMBER}/generator-runner_report.html"
//            }
//            post{
//              failure{
//                githubNotify context: 'AVScan Generators', description: 'Running AntiVirus Scan on generator-runner-exec.jar',  status: 'FAILURE', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/avscans/${CHANGE_ID}/${BUILD_NUMBER}/generator-runner_report.html"
//              }
//            }
//          }
//        }
//      }
      stage('Deploy'){
        steps{
          input message: "Continue with deployment?"
            script {

              if ("${env.BRANCH_NAME}" == "PR-18"){
                // build docker containers

                withCredentials([string(credentialsId: 'http-proxy-url', variable: 'URL')]) {
                sh "docker build -f docker/Generators_Dockerfile --tag eclipsevorto/vorto-generators:${env.BRANCH_NAME} --build-arg JAR_FILE=generators/generator-runner/target/generator-runner-exec.jar --build-arg http_proxy=$URL ./"
                sh "docker build -f docker/Repository_Dockerfile --tag eclipsevorto/vorto-repo:${env.BRANCH_NAME} --build-arg JAR_FILE=repository/repository-server/target/infomodelrepository.jar --build-arg http_proxy=$URL ./"
				}
                // push docker containers
                //sh "/usr/local/bin/docker-compose -f docker-compose-build.yml push"
                // run ansible
                //dir('ansible') {
                //  git(branch: 'master',
                //    credentialsId: 'eclipsevorto-jenkins',
                //    url: 'https://github.com/bsinno/vorto-ansible.git')
                //    ansiblePlaybook(
                //        playbook: 'deploy_eclipse.yml'
                //        )
                //}
              }
            }
        }
      }
       // post {
       //   always {
       //     cleanWs(patterns: [[pattern: '.repository', type: 'EXCLUDE']])
       //   }
       // }
    }
}
