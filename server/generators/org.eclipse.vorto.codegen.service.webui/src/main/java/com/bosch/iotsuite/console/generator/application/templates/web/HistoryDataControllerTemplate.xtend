package com.bosch.iotsuite.console.generator.application.templates.web

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class HistoryDataControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''HistoryDataController.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/web'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».web;
		
		import java.util.ArrayList;
		import java.util.List;
		
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.http.MediaType;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.bind.annotation.RestController;
		
		import com.example.iot.«element.name.toLowerCase».dao.«element.name»Repository;
		import com.example.iot.«element.name.toLowerCase».model.«element.name»;
		
		@RestController
		@RequestMapping("/rest/history/devices")
		public class HistoryDataController {
			
			@Autowired
			private «element.name»Repository repository;
					
			@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
			public List<«element.name»> findAll() {
				List<«element.name»> target = new ArrayList<>();
				repository.findAll().forEach(target::add);
				return target;
			}
		}
		'''
	}
	
}