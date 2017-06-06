package com.bosch.iotsuite.console.generator.application.templates.web

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class IdentityControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''IdentityController.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/web'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».web;
		
		import java.security.Principal;
		import java.util.HashMap;
		import java.util.Map;
		
		import org.springframework.http.MediaType;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.bind.annotation.RestController;
		
		import com.bosch.iotsuite.data.permissions.model.UserInfo;
		
		@RestController
		@RequestMapping("/rest/identities")
		public class IdentityController {
		
			@RequestMapping(value = "/user", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
			public Map<String, String> user(Principal user) {
				UserInfo userToken = Utils.getUserToken();
				Map<String, String> principal = new HashMap<String, String>();
				principal.put("userId", userToken.getFirstName());
				
				return principal;
			}
		}
		'''
	}
	
}