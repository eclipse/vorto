pipeline {
  agent any  
    // Options covers all other job properties or wrapper functions that apply to entire Pipeline.
  	options {
	    buildDiscarder(logRotator(numToKeepStr:'5'))
	}
    stages{
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

