package com.bosch.iotsuite.console.generator.application.templates.web

import com.bosch.iotsuite.console.generator.application.templates.TemplateUtils
import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingControllerTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''«context.name»Controller.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/web'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».web;
		
		import java.util.List;
		import java.util.concurrent.CompletableFuture;
		import java.util.concurrent.ExecutionException;
		import java.util.stream.Collectors;
				
		import org.springframework.beans.factory.annotation.Autowired;
		import org.springframework.http.HttpStatus;
		import org.springframework.http.MediaType;
		import org.springframework.web.bind.annotation.ExceptionHandler;
		import org.springframework.web.bind.annotation.PathVariable;
		import org.springframework.web.bind.annotation.RequestMapping;
		import org.springframework.web.bind.annotation.RequestMethod;
		import org.springframework.web.bind.annotation.ResponseStatus;
		import org.springframework.web.bind.annotation.RestController;
		
		import com.example.iot.«element.name.toLowerCase».model.«element.name»;
		import com.bosch.iotsuite.management.things.ThingClient;
		import com.bosch.iotsuite.management.things.model.Query;
		import com.bosch.iotsuite.management.things.model.Thing;
		import com.bosch.iotsuite.management.things.model.ThingSearchResult;
		
		@RestController
		@RequestMapping("/rest/devices")
		public class «element.name»Controller {
		
			@Autowired
			private ThingClient thingClient;
					
			@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
			public List<«element.name»> search«element.name»Things() throws ExecutionException, InterruptedException  {
				Query query = Query.newBuilder().withOptions("limit(" + 0 + "," + 50 + ")")
											.withFilter("eq(attributes/_modelId,\"«element.namespace».«element.name»:«element.version»\")").build();
				CompletableFuture<ThingSearchResult> searchResult = thingClient.searchThings(query);
				return searchResult.get().getThings().stream().map(Utils.thingTo«element.name»).collect(Collectors.toList());
			}
		
			@RequestMapping(value = "/count", produces = { MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
			public Integer get«element.name»Count() throws ExecutionException, InterruptedException {
				return thingClient.countThings(
					Query.newBuilder().withFilter("eq(attributes/_modelId,\"«element.namespace».«element.name»:«element.version»\")").build())
				.get();
			}
		
			@RequestMapping(value = "/{thingId:.+}", produces = {
					MediaType.APPLICATION_JSON_VALUE }, method = RequestMethod.GET)
			public «element.name» get«element.name»Thing(@PathVariable("thingId") final String thingId) throws ExecutionException, InterruptedException {
				Thing «element.name.toFirstLower»Thing = thingClient.getThing(thingId).get();
				return Utils.thingTo«element.name».apply(«element.name.toFirstLower»Thing);
			}
			
			@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason = "Problem accessing Bosch IoT Things")
			@ExceptionHandler(ExecutionException.class)
			public void executionError(final ExecutionException ex){
				// handle this error 
			}
				
			@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason = "Problem accessing Bosch IoT Things")
			@ExceptionHandler(InterruptedException.class)
			public void interruptedError(final InterruptedException ex){
				// handle this error
			}
		}
		'''
	}
	
}