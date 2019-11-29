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
            // Notify GitHub that checks are now in progress
            
            githubNotify context: 'Repository - Compliance Checks', description: 'Checks In Progress',  status: 'PENDING', targetUrl: "https://s3.eu-central-1.amazonaws.com/vorto-pr-artifacts/avscans"
           
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

          stage("CLMScan Vorto-repository"){
            steps{              
                withMaven(
                    maven: 'maven-latest',
                    mavenLocalRepo: '.repository') {
                  withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                  	script {
						try {
							def policyEvaluation = nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: selectedApplication('vorto-repository'), iqScanPatterns: [[scanPattern: 'repository/repository-server/target/**/*.jar']], iqStage: 'build', jobCredentialsId: 'CLMScanUser'
						    githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Completed',  status: 'SUCCESS', targetUrl: "https://latest.nexusiq.bosch-si.com/assets/index.html#/applicationReport/vorto-repository"
							
						} catch (error) {
							def policyEvaluation = error.policyEvaluation
							throw error
						}
					}
                  }
                  catchError {
                    githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Failed',  status: 'FAILURE', targetUrl: "https://latest.nexusiq.bosch-si.com/assets/index.html#/applicationReport/vorto-repository"
                  }
                }
            }
            post{
              failure{
                githubNotify context: 'Repository - Compliance Checks', description: 'Compliance Checks Failed',  status: 'FAILURE', targetUrl: ""
              }
            }
          }

        }
      }
      
    }
}

