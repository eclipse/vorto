package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class DockerfileTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'Dockerfile'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		FROM java:8
		# Add folder to hold jar file
		RUN mkdir /gen
		WORKDIR /gen
		ARG JAR_FILE
		RUN apt-get update && apt-get install -y jq sed
		ADD target/${JAR_FILE} /gen/generators.jar
		ADD ./docker/wait-for-it.sh /gen
		ADD ./docker/run_generators.sh /gen/run_generators.sh
		RUN chmod +x run_generators.sh wait-for-it.sh
		#Wait for repository to become avaliable
		# Read mounted config file, remove newlines from file and write to variable, run spring boot after that
		CMD ["/bin/bash", "/gen/wait-for-it.sh", "-t", "20", "localhost:8080",  "--", "/bin/bash", "/gen/run_generators.sh"]
		'''
	}
	
}
