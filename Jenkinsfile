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
                maven: 'mvn3',
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
                    maven: 'mvn3',
                    mavenLocalRepo: '.repository') {
                  withCredentials([string(credentialsId: 'sonarcloud-vortodev', variable: 'TOKEN')]) {
                    sh 'mvn -P coverage -Dsonar.projectKey=vorto -Dsonar.organization=vortodev -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$TOKEN -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.coverage.jacoco.xmlReportPaths=jacoco-coverage/target/site/jacoco-aggregate/jacoco.xml -Dsonar.language=java -Dsonar.pullrequest.branch=$BRANCH_NAME -Dsonar.pullrequest.key=$CHANGE_ID -Dsonar.pullrequest.base=development sonar:sonar'
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
                    maven: 'mvn3',
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
        }
      }
      
    }
}

