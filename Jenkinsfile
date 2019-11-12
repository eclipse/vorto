pipeline {
  agent any  
    // Options covers all other job properties or wrapper functions that apply to entire Pipeline.
  	options {
	    buildDiscarder(logRotator(numToKeepStr:'5'))
	}
    stages{
      stage("Build"){
        steps{
            sh 'printenv'
            sh 'echo Proxy Host = $PROXY_HOST'
            sh 'echo Proxy Port = $PROXY_PORT'
            sh 'echo Proxy User = $PROXY_USER'
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
              githubNotify context: 'Repository - Compliance Checks', description: 'Checks In Progress',  status: 'PENDING', targetUrl: "https://s3.eu-central-1.amazonaws.com/vorto-pr-artifacts/avscans"
                withMaven(
                    maven: 'maven-latest',
                    mavenLocalRepo: '.repository') {
                  catchError { //Todo remove as soon as nexus is fixed
                    withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                      nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: selectedApplication('vorto-repository'), iqScanPatterns: [[scanPattern: 'repository/repository-server/target/**/*.jar']], iqStage: 'build', jobCredentialsId: 'CLMScanUser'
                        // add s3upload of nexus reports
                        //      s3Upload(file:'file.txt', bucket:'vorto-pr-artifacts', path:'repository/repository-server/target/**/*.pdf')
                    }
                  }
                }
              githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Completed',  status: 'SUCCESS', targetUrl: "https://s3.eu-central-1.amazonaws.com/vorto-pr-artifacts/avscans"
            }
            post{
              failure{
                githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Failed',  status: 'FAILURE', targetUrl: "https://s3.eu-central-1.amazonaws.com/vorto-pr-artifacts/avscans"
              }
            }
          }
          stage("AVScan infomodelrepository"){
            when {
              allOf {
                expression { env.CHANGE_ID != null }
              }
            }
            steps{
              githubNotify context: 'Repository - Virus Scan', description: 'Scan In Progress',  status: 'PENDING', targetUrl: "https://s3.eu-central-1.amazonaws.com/vorto-pr-artifacts/avscans"
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
                githubNotify context: 'Repository - Virus Scan', description: 'Scan Failed',  status: 'FAILURE', targetUrl: "https://s3.eu-central-1.amazonaws.com/vorto-pr-artifacts/avscans/${CHANGE_ID}/${BUILD_NUMBER}/infomodelrepository_report.html"
              }
            }
          }
        }
      }
      
    }
}

