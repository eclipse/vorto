pipeline {
  agent any
    stages{
      stage("Build"){
        steps{
            // Maven installation declared in the Jenkins "Global Tool Configuration"
            withMaven(
                maven: 'maven-latest',
                mavenLocalRepo: '.repository') {
              sh 'mvn -P coverage clean install'
            }
        }
      }
      stage('Run compliance checks') {
        parallel {
          stage("SonarCloud"){
            when {
              allOf {
                expression { env.CHANGE_ID != null }
              }
            }
            steps{
              githubNotify context: 'SonarCloud', description: 'SonarCloud Scan In Progress',  status: 'PENDING', targetUrl: "https://sonarcloud.io/project/issues?id=org.eclipse.vorto%3Aparent&pullRequest=${CHANGE_ID}&resolved=false"
                withMaven(
                    // Maven installation declared in the Jenkins "Global Tool Configuration"
                    maven: 'maven-latest',
                    mavenLocalRepo: '.repository') {
                  withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'TOKEN')]) {
                    sh 'mvn -P coverage -Dsonar.projectKey=org.eclipse.vorto:parent -Dsonar.organization=vorto  -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$TOKEN -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.jacoco.reportPaths=target/jacoco.exec -Dsonar.language=java sonar:sonar -Dsonar.pullrequest.branch=$BRANCH_NAME -Dsonar.pullrequest.key=$CHANGE_ID -sonar.pullrequest.base=development'
                  }
                }
              githubNotify context: 'SonarCloud', description: 'SonarCloud Scan Completed',  status: 'SUCCESS', targetUrl: "https://sonarcloud.io/project/issues?id=org.eclipse.vorto%3Aparent&pullRequest=${CHANGE_ID}&resolved=false"
            }
            post{
              failure{
                githubNotify context: 'SonarCloud', description: 'SonarCloud Scan Failed',  status: 'FAILURE', targetUrl: "https://sonarcloud.io/project/issues?id=org.eclipse.vorto%3Aparent&pullRequest=${CHANGE_ID}&resolved=false"
              }
            }
          }
          stage("CLMScan Vorto-repository"){
            steps{
              githubNotify context: 'Repository - Compliance Checks', description: 'Checks In Progress',  status: 'PENDING', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/clmscans/${CHANGE_ID}/${BUILD_NUMBER}/"
                withMaven(
                    maven: 'maven-latest',
                    mavenLocalRepo: '.repository') {
                  catchError { //Todo remove as soon as nexus is fixed
                    withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                      nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: selectedApplication('vorto-repository'), iqScanPatterns: [[scanPattern: 'repository/repository-server/target/**/*.jar']], iqStage: 'build', jobCredentialsId: 'CLMScanUser'
                        // add s3upload of nexus reports
                        //      s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'repository/repository-server/target/**/*.pdf')
                    }
                  }
                }
              githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Completed',  status: 'SUCCESS', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/clmscans/${CHANGE_ID}/${BUILD_NUMBER}/"
            }
            post{
              failure{
                githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Failed',  status: 'FAILURE', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/clmscans/${CHANGE_ID}/${BUILD_NUMBER}/"
              }
            }
          }
          stage("CLMScan Vorto-generators"){
            steps{
              githubNotify context: 'Generators - Compliance Checks', description: 'Checks In Progress',  status: 'PENDING', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/clmscans/${CHANGE_ID}/${BUILD_NUMBER}/"
                withMaven(
                  maven: 'maven-latest',
                  mavenLocalRepo: '.repository') {
                    withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                      nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: selectedApplication('vorto-generators'), iqScanPatterns: [[scanPattern: 'generators/generator-runner/target/**/*exec.jar']], iqStage: 'build', jobCredentialsId: 'CLMScanUser'
                        //      s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'generators/generator-runner/target/**/*.pdf')
                    }
                }
              githubNotify context: 'Generators - Compliance Checks', description: 'Compliance Checks Completed',  status: 'SUCCESS', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/clmscans/${CHANGE_ID}/${BUILD_NUMBER}/"
            }
            post{
              failure{
                githubNotify context: 'Generators - Compliance Checks', description: 'Compliance Checks Failed,  status: 'FAILURE', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/clmscans/${CHANGE_ID}/${BUILD_NUMBER}/"
              }
            }
          }
          stage("Virus Scan Vorto-Repository"){
            when {
              allOf {
                expression { env.CHANGE_ID != null }
              }
            }
            steps{
              githubNotify context: 'Repository - Virus Scan', description: 'Scan In Progress',  status: 'PENDING', targetUrl: ""
                // Get Bosch pom files to run in an extra folder to keep the open source project clean and because the Bosch maven plugins can not be licensed under EPL
                dir('avscan_infomodel') {
                  //copy files over to the new maven folder to run AntiVirus Scans
                  git url: "https://github.com/eclipsevorto-jenkins/vorto_compliance_jenkins.git"
                    sh 'cp ../repository/repository-server/target/infomodelrepository.jar ./'
                }
              withMaven(
                  maven: 'maven-latest',
                  mavenLocalRepo: '.repository') {
                sh 'mvn verify -Dbosch.avscan.fileToScan=infomodelrepository.jar -f avscan_infomodel/pom_bosch.xml'
                  withAWS(region:'eu-central-1',credentials:'aws-s3-vorto-jenkins-technical-user') {
                  withCredentials([string(credentialsId: 'hide-server-url', variable: 'TOKEN')]) {
                    sh "sed -i -e \"s,$TOKEN,,g\" avscan_infomodel/target/inl-releng-avsupport/avscan_report.html"
                  }
                      s3Upload(file:'avscan_infomodel/target/inl-releng-avsupport/avscan_report.html', bucket:'pr-vorto-documents', path:"avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html")
                  }
              }
              githubNotify context: 'Repository - Virus Scan', description: 'Scan Completed',  status: 'SUCCESS', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html"
            }
            post{
              failure{
                githubNotify context: 'Repository - Virus Scan', description: 'Scan Failed',  status: 'FAILURE', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html"
              }
            }
          }
          stage("Virus Scan Vorto-Generators"){
            when {
              allOf {
                expression { env.CHANGE_ID != null }
              }
            }
            steps{
              githubNotify context: 'Generators - Virus Scan', description: 'Scan In Progress',  status: 'PENDING', targetUrl: ""
                // Get Bosch pom files to run in an extra folder to keep the open source project clean and because the Bosch maven plugins can not be licensed under EPL
                dir('avscan_generator') {
                  //copy files over to the new maven folder to run AntiVirus Scans
                  git url: "https://github.com/eclipsevorto-jenkins/vorto_compliance_jenkins.git"
                    sh 'cp ../generators/generator-runner/target/generator-runner-exec.jar ./'
                }
              withMaven(
                  maven: 'maven-latest',
                  mavenLocalRepo: '.repository') {
                sh 'mvn clean verify -Dbosch.avscan.fileToScan=generator-runner-exec.jar -f avscan_generator/pom_bosch.xml'
                  withAWS(region:'eu-central-1',credentials:'aws-s3-vorto-jenkins-technical-user') {
                  withCredentials([string(credentialsId: 'hide-server-url', variable: 'TOKEN')]) {
                    sh "sed -i -e \"s,$TOKEN,,g\" avscan_generator/target/inl-releng-avsupport/avscan_report.html"
                  }
                      s3Upload(file:'avscan_generator/target/inl-releng-avsupport/avscan_report.html', bucket:'pr-vorto-documents', path:"avscans/${CHANGE_ID}/${BUILD_NUMBER}/generator-runner_report.html")
                  }
              }
              githubNotify context: 'Generators - Virus Scan', description: 'Scan Completed',  status: 'SUCCESS', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/avscans/${CHANGE_ID}/${BUILD_NUMBER}/generator-runner_report.html"
            }
            post{
              failure{
                githubNotify context: 'Generators - Virus Scan', description: 'Scan Failed',  status: 'FAILURE', targetUrl: "https://s3.eu-central-1.amazonaws.com/pr-vorto-documents/avscans/${CHANGE_ID}/${BUILD_NUMBER}/generator-runner_report.html"
              }
            }
          }
        }
      }
      stage('Deploy'){
        steps{
            script {
              //todo add developmetn branch for deployment to aws
              if ("${env.BRANCH_NAME}" == "master"){
                input message: "Continue with deployment?"
                // build docker containers and load http proxy
                withCredentials([string(credentialsId: 'http-proxy-url', variable: 'TOKEN')]) {
                  // set +x because the url contains $@ which is otherwise parsed by bash so its escaped but jenkins will only to string matching to censor secrets
                  sh "set +x; docker build -f docker/Generators_Dockerfile --tag eclipsevorto/vorto-generators:${env.BRANCH_NAME} --build-arg JAR_FILE=generators/generator-runner/target/generator-runner-exec.jar --build-arg http_proxy=$TOKEN ./"
                    sh "set +x; docker build -f docker/Repository_Dockerfile --tag eclipsevorto/vorto-repo:${env.BRANCH_NAME} --build-arg JAR_FILE=repository/repository-server/target/infomodelrepository.jar --build-arg http_proxy=$TOKEN ./;"
                }
                // push docker containers
                sh "docker push eclipsevorto/vorto-generators:${env.BRANCH_NAME}"
                sh "docker push eclipsevorto/vorto-repo:${env.BRANCH_NAME}"
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
