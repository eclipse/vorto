node {
	stage("GitHub Checkout"){
		checkout scm
	}
	stage("SonarCloud"){
//		withMaven(
//			// Maven installation declared in the Jenkins "Global Tool Configuration"
//			maven: 'maven-latest',
//			mavenLocalRepo: '.repository') {
//				sh 'mvn -P coverage clean install'
//				withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'TOKEN')]) {
//					sh 'mvn -P coverage -Dsonar.projectKey=Scriptkiddi_vorto -Dsonar.organization=scriptkiddi-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$TOKEN -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.jacoco.reportPaths=/home/fritz/code/vorto/target/jacoco.exec -Dsonar.language=java sonar:sonar -Dsonar.pullrequest.branch=$BRANCH_NAME -Dsonar.pullrequest.key=$CHANGE_ID -sonar.pullrequest.base=development'
//				}
//			}
	}
//	stage("CLMScan"){
//		withMaven(
//			maven: 'maven-latest',
//			mavenLocalRepo: '.repository') {
//				withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
//					nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: selectedApplication('vorto-repository'), iqStage: 'build', jobCredentialsId: 'CLMScanUser'
//					//ToDo test amazon upload
//					//sh 'mvn clean package tina:analyze -Denv.clmusername=$USERNAME -Denv.clmpassword=$PASSWORD -DskipTests'
//					//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'repository/repository-server/target/**/*.pdf')
//					//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'generators/generator-runner/target/**/*.pdf')
//				}
//			}
//	}
	stage("AVScan"){
		withMaven(
			maven: 'maven-latest',
			mavenSettingsConfig: '90979119-b8cf-4c8f-a5a9-35db29e38a39',
			mavenLocalRepo: '.repository') {
				sh 'mvn clean verify'
				//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'repository/repository-server/target/**/*.pdf')
				//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'generators/generator-runner/target/**/*.pdf')
			}
		withMaven(
			maven: 'maven-latest',
			mavenSettingsConfig: 'bbf31159-c406-4667-848c-40b2bdc0022e',
			mavenLocalRepo: '.repository') {
				sh 'mvn clean verify'
				//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'repository/repository-server/target/**/*.pdf')
				//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'generators/generator-runner/target/**/*.pdf')
			}

	}
}
