node {
	stage("SonarCloud"){
		checkout scm
		withMaven(
			// Maven installation declared in the Jenkins "Global Tool Configuration"
			maven: 'maven-latest',
			mavenLocalRepo: '.repository') {
				sh 'mvn -P coverage clean install'
				withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'TOKEN')]) {
					sh 'mvn -P coverage -Dsonar.projectKey=Scriptkiddi_vorto -Dsonar.organization=scriptkiddi-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$TOKEN -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.jacoco.reportPaths=/home/fritz/code/vorto/target/jacoco.exec -Dsonar.language=java sonar:sonar'
				}
			}
	}
	stage("BlackDuck"){
	}
}
