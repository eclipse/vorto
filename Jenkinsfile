pipeline {
  agent any  
    // Options covers all other job properties or wrapper functions that apply to entire Pipeline.
    options {
        buildDiscarder(logRotator(numToKeepStr:'5'))
        disableConcurrentBuilds()
    }
    stages{
      stage("Build"){
        steps{
            // Notify GitHub that checks are now in progress
            githubNotify context: 'SonarCloud', description: 'SonarCloud Scan In Progress',  status: 'PENDING'
            githubNotify context: 'Repository - Compliance Checks', description: 'Checks In Progress',  status: 'PENDING'
            githubNotify context: 'Repository - Virus Scan', description: 'Scan In Progress',  status: 'PENDING'
            // Maven installation declared in the Jenkins "Global Tool Configuration"
            withMaven(
                maven: 'maven-latest',
                mavenLocalRepo: '.repository') {

                    sh 'mvn -P coverage clean install'

            }
        }
        post{
              failure{
                 // Notify GitHub that checks could not proceed due to build failure
                githubNotify context: 'SonarCloud', description: 'Aborted due to build failure',  status: 'ERROR'
                githubNotify context: 'Repository - Compliance Checks', description: 'Aborted due to build failure',  status: 'ERROR'
                githubNotify context: 'Repository - Virus Scan', description: 'Aborted due to build failure',  status: 'ERROR'
                error('Aborted due to failure of Build stage')
              }
        }
      }
      stage('Run compliance checks') {
        parallel {
          stage("SonarCloud"){
            steps{
                withMaven(
                    // Maven installation declared in the Jenkins "Global Tool Configuration"
                    maven: 'maven-latest',
                    mavenLocalRepo: '.repository') {
                  withCredentials([string(credentialsId: 'sonarcloud', variable: 'TOKEN')]) {
                    sh 'mvn -P coverage -Dsonar.projectKey=vorto -Dsonar.organization=vortodev -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$TOKEN -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.coverage.jacoco.xmlReportPaths=jacoco-coverage/target/site/jacoco-aggregate/jacoco.xml -Dsonar.language=java sonar:sonar'
                  }
                }
              githubNotify context: 'SonarCloud', description: 'SonarCloud Scan Completed',  status: 'SUCCESS', targetUrl: "https://sonarcloud.io/project/issues?id=vorto&pullRequest=${CHANGE_ID}&resolved=false"
            }
            post{
              failure{
                githubNotify context: 'SonarCloud', description: 'SonarCloud Scan Failed',  status: 'FAILURE', targetUrl: "https://sonarcloud.io/project/issues?id=vorto&pullRequest=${CHANGE_ID}&resolved=false"
              }
            }
          }
          stage("CLMScan Vorto-repository"){
            steps{
                withMaven(
                    maven: 'maven-latest',
                    mavenLocalRepo: '.repository') {
                  withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    script {
                        try {
                            def policyEvaluation = nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: selectedApplication('vorto-repository'), iqScanPatterns: [[scanPattern: 'repository/repository-server/target/**/*.jar']], iqStage: 'build', jobCredentialsId: 'CLMScanUser'
                            if (policyEvaluation.criticalComponentCount > 0) {
                              githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Failed, Policy Issues Detected',  status: 'FAILURE', targetUrl: "${policyEvaluation.applicationCompositionReportUrl}"         
                            } else {
                              githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Completed',  status: 'SUCCESS', targetUrl: "${policyEvaluation.applicationCompositionReportUrl}"      
                            }
                        } catch (error) {
                            def policyEvaluation = error.policyEvaluation
                            githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Failed',  status: 'FAILURE', targetUrl: "${policyEvaluation.applicationCompositionReportUrl}"
                            throw error
                        }
                    }
                  }
                }
            }
            post{
              failure{
                githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Failed',  status: 'FAILURE'
              }
            }
          }
          stage("AVScan infomodelrepository"){
            steps{
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
                      s3Upload(file:'avscan_infomodel/target/inl-releng-avsupport/avscan_report.html', bucket:'vorto-pr-artifacts', path:"avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html")
                  }
              }
              githubNotify context: 'Repository - Virus Scan', description: 'Scan Completed',  status: 'SUCCESS', targetUrl: "https://s3.eu-central-1.amazonaws.com/vorto-pr-artifacts/avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html"
            }
            post{
              failure{
                githubNotify context: 'Repository - Virus Scan', description: 'Scan Failed',  status: 'FAILURE'
              }
            }
          }
        }
      }
      
    }
}

