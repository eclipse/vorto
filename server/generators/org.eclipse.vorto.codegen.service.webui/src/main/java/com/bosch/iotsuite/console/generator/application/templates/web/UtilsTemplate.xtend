package com.bosch.iotsuite.console.generator.application.templates.web

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class UtilsTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''Utils.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/web'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».web;
		
		import java.util.Map;
		import java.util.function.Function;
		
		import com.bosch.iotsuite.management.thingtype.api.ModelId;
		import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
		import org.springframework.security.core.Authentication;
		import org.springframework.security.core.context.SecurityContextHolder;
		
		import com.example.iot.«element.name.toLowerCase».model.«element.name»;
		«FOR fbProperty : element.properties»
		import com.example.iot.«element.name.toLowerCase».model.«fbProperty.type.name»;
		«ENDFOR»
		import com.bosch.iotsuite.data.permissions.model.UserInfo;
		import com.bosch.iotsuite.management.things.model.Thing;
		import com.fasterxml.jackson.databind.ObjectMapper;
		
		public class Utils {
		
			private static final String APP_ID = "«element.name.toLowerCase»solution";
			
			public static String getMyAuthorationSubject(String solutionId) {
				return solutionId+":"+APP_ID;
			}
				
			public static String getThingId(String namespace, String name, String deviceId) {
				return namespace + ":"+name+"-" + deviceId;
			}
			
			public static UserInfo getUserToken() {
				Authentication autentication = SecurityContextHolder.getContext().getAuthentication();
				UserInfo userToken = null;
				if (autentication instanceof UsernamePasswordAuthenticationToken) {
					UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) autentication;
					userToken = (UserInfo) token.getDetails();
					return userToken;
				}
				return null;
			}
			
			public static Function<Thing, «element.name»> thingTo«element.name» = (thing) -> {
					«element.name» «element.name.toLowerCase» = new «element.name»(thing.getId(),
									thing.getName(),
									ModelId.fromPrettyFormat(thing.getThingTypeId())
									);
					«FOR fbProperty : element.properties»
					«element.name.toLowerCase».set«fbProperty.name.toFirstUpper»(convert(thing,"«fbProperty.name»",«fbProperty.type.name».class));
					«ENDFOR»
					return «element.name.toLowerCase»;
			};
			
			private static <T> T convert(Thing thing, String propertyName, Class<T> expectedClass) {
				Map<String, Object> statusProperty = thing.listFeatures().get(propertyName).getProperties();
				statusProperty.remove("_modelId");
				final ObjectMapper mapper = new ObjectMapper();
				mapper.setDateFormat(Thing.JSON_DATE_FORMAT);
				return mapper.convertValue(statusProperty, expectedClass);
			}
		
		}
		'''
	}
	
}