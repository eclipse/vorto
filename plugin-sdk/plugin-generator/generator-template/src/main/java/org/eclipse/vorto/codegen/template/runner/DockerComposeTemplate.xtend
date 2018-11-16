package org.eclipse.vorto.codegen.template.runner

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class DockerComposeTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'docker-compose.yml'
	}
	
	override getPath(InformationModel context) {
		'generator-parent/generator-runner'
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		version: '3'
		services:
		  repository:
		    image: eclipsevorto/vorto-repo
		    ports:
		      - "8080:8080"
		    volumes:
		      - "./docker:/code/config"
		    environment:
		    - USE_PROXY=0
		    networks:
		      - backend
		  generators:
		    depends_on: ["repository"]
		    build:
			  context: .
			  args:
				JAR_FILE: generator-runner-0.0.1-SNAPSHOT-exec.jar
		    volumes:
		      - "./docker:/gen/config"
		    networks:
		      - backend
		networks:
		  backend:
		    driver: bridge
		'''
	}
	
}
