node {
//	stage("SonarCloud"){
//		checkout scm
//		withMaven(
//			// Maven installation declared in the Jenkins "Global Tool Configuration"
//			maven: 'maven-latest',
//			mavenLocalRepo: '.repository') {
//				sh 'mvn -P coverage clean install'
//				withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'TOKEN')]) {
//					sh 'mvn -P coverage -Dsonar.projectKey=Scriptkiddi_vorto -Dsonar.organization=scriptkiddi-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$TOKEN -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.jacoco.reportPaths=/home/fritz/code/vorto/target/jacoco.exec -Dsonar.language=java sonar:sonar -Dsonar.pullrequest.branch=$BRANCH_NAME -Dsonar.pullrequest.key=$CHANGE_ID -sonar.pullrequest.base=development'
//				}
//			}
//	}
	stage("CLMScan"){
		withMaven(
			maven: 'maven-latest',
			mavenLocalRepo: '.repository') {
				sh 'mvn -P coverage clean install'
				withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
					sh 'mvn clean package tina:analyze -Denv.clmusername=$USERNAME -Denv.clmpassword=$PASSWORD -DskipTests'
					//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'repository/repository-server/target/**/*.pdf')
					//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'generators/generator-runner/target/**/*.pdf')

				}
			}
	}
}
