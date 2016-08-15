package org.eclipse.vorto.server.devtool.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

@RestController
@RequestMapping(value = "/repository")
public class RepositoryController {

	@Value("${vorto.repository.base.path}")
	String repositoryBasePath;
	
	@RequestMapping(value = "/basepath", method = RequestMethod.GET)
	public String getRepoUrl() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("basepath", repositoryBasePath);
		String json = jsonObject.toString();
		return json;
	}	
}
