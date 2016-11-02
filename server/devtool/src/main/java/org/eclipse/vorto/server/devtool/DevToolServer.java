/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.server.devtool;

import java.io.File;

import org.eclipse.vorto.server.devtool.utils.Constants;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.eclipse.vorto.server.devtool"})
public class DevToolServer extends SpringBootServletInitializer {
				
	public static void main(String... args) {
		createUploadZipFileDirectory();
		SpringApplication.run(DevToolServer.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DevToolServer.class);
	}
	
	private static void createUploadZipFileDirectory(){
		File directory = new File(Constants.UPLOAD_ZIP_FILE_DIRECTORY);
		if (!directory.exists()) {
		    try{
		    	directory.mkdir();
		    } 
		    catch(SecurityException se){

		    }        
		}
	}
}
