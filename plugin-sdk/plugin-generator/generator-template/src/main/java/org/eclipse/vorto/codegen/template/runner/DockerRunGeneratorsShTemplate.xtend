package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class DockerRunGeneratorsShTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'run_generators.sh'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner/docker'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
SPRING_APPLICATION_JSON=$(jq .generators /gen/config/config.json | sed $'s/\r//' | tr -d '\n');
export SPRING_APPLICATION_JSON
java -jar /gen/generators.jar;
		'''
	}
	
}
