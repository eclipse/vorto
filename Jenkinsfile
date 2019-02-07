node {
	stage("GitHub Checkout"){
		checkout scm
	}
	stage("Build"){
		withMaven(
			// Maven installation declared in the Jenkins "Global Tool Configuration"
			maven: 'maven-latest',
			mavenLocalRepo: '.repository') {
				sh 'mvn -P coverage clean install'
			}
	}
	stage("SonarCloud"){
		withMaven(
			// Maven installation declared in the Jenkins "Global Tool Configuration"
			maven: 'maven-latest',
			mavenLocalRepo: '.repository') {
				withCredentials([string(credentialsId: 'sonarcloud-token', variable: 'TOKEN')]) {
					//sh 'mvn -P coverage -Dsonar.projectKey=Scriptkiddi_vorto -Dsonar.organization=scriptkiddi-github -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=$TOKEN -Dsonar.dynamicAnalysis=reuseReports -Dsonar.java.coveragePlugin=jacoco -Dsonar.jacoco.reportPaths=/home/fritz/code/vorto/target/jacoco.exec -Dsonar.language=java sonar:sonar -Dsonar.pullrequest.branch=$BRANCH_NAME -Dsonar.pullrequest.key=$CHANGE_ID -sonar.pullrequest.base=development'
				}
			}
	}
	stage("CLMScan"){
//		withMaven(
//			maven: 'maven-latest',
//			mavenLocalRepo: '.repository') {
//				withCredentials([usernamePassword(credentialsId: 'CLMScanUser', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
//					nexusPolicyEvaluation failBuildOnNetworkError: false, iqApplication: selectedApplication('vorto-repository'), iqStage: 'build', jobCredentialsId: 'CLMScanUser'
//					//ToDo test amazon upload
//					//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'repository/repository-server/target/**/*.pdf')
//					//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'generators/generator-runner/target/**/*.pdf')
//				}
//			}
	}
	stage("AVScan"){
		// Get Bosch pom files to run in an extra folder to keep the open source project clean and because the Bosch maven plugins can not be licensed under EPL
		dir('tmp') {
			//copy files over to the new maven folder to run AntiVirus Scans
			sh 'cp ../repository/repository-server/target/infomodelrepository.jar ./'
			sh 'cp ../generators/generator-runner/target/generator-runner-exec.jar ./'
			git url: "https://github.com/Scriptkiddi/vorto_bosch_jenkins.git"
		}
		withMaven(
			maven: 'maven-latest',
			mavenLocalRepo: '.repository') {
				sh 'mvn clean verify -Dbosch.avscan.fileToScan=infomodelrepository.jar -f tmp/pom_bosch.xml'
					s3Upload(file:'tmp/target/inl-releng-avsupport/avscan_report.html', bucket:'pr-vorto-documents', path:'avscans/$CHANGE_ID/$BUILD_NUMBER/infomodelrepository_report.html')
				sh 'mvn clean verify -Dbosch.avscan.fileToScan=generator-runner-exec.jar -f tmp/pom_bosch.xml'
				//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'repository/repository-server/target/**/*.pdf')
				//s3Upload(file:'file.txt', bucket:'pr-vorto-documents', path:'generators/generator-runner/target/**/*.pdf')
			}
	}
}
