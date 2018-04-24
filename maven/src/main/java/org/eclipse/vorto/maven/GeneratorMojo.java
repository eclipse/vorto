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
package org.eclipse.vorto.maven;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.vorto.codegen.api.Generated;
import org.eclipse.vorto.codegen.api.IGeneratedWriter;
import org.eclipse.vorto.codegen.api.IGenerationResult;
import org.eclipse.vorto.codegen.api.IVortoCodeGenerator;
import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.ZipContentExtractCodeGeneratorTask;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelType;
import org.eclipse.vorto.server.commons.reader.IModelWorkspace;

@Mojo(name = "generate",defaultPhase= LifecyclePhase.GENERATE_SOURCES)
public class GeneratorMojo extends AbstractMojo {
	
	
	@Parameter(defaultValue="${project}", readonly=true, required=true)
	private MavenProject project;
	
	/**
	 * The class of the generator to invoke
	 */
	@Parameter(required = true)
    private String generatorClass;
	
	/**
	 * Outputs the generated content to this directory
	 */
	@Parameter(defaultValue = "${project.basedir}/src-gen")
    private File outputPath;

	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Executing Generator of class '"+getGeneratorClass());
		
		try {
			final IModelWorkspace workspace = IModelWorkspace.newReader().addZip(new ZipInputStream(new ByteArrayInputStream(loadInformationModels()))).read();
			
			List<MappingModel> mappingModels = workspace.get().stream().filter(p -> p instanceof MappingModel).map(MappingModel.class::cast).collect(Collectors.toList());
			for (Model model : workspace.get().stream().filter(p -> p instanceof InformationModel).collect(Collectors.toList())) {
				InformationModel infomodel = (InformationModel)model;
				IVortoCodeGenerator codeGenerator = (IVortoCodeGenerator)Class.forName(generatorClass).newInstance();
				IGenerationResult result = codeGenerator.generate(infomodel, new InvocationContext(mappingModels,null, new HashMap<String, String>()),null);
				if (result.getMediatype().equalsIgnoreCase("application/zip")) {
					final ZipContentExtractCodeGeneratorTask task = new ZipContentExtractCodeGeneratorTask(result.getContent());
					task.generate(null, InvocationContext.simpleInvocationContext(),new IGeneratedWriter() {
						
						public void write(Generated generated) {
							if (generated.getFileName() == null) {
								File generatedDirectory = new File(outputPath,stripPath(generated.getFolderPath()));
								generatedDirectory.mkdirs();
							} else {
								if (generated.getFileName().equals("pom.xml")) {
									return;
								}
								File generatedDirectory = new File(outputPath,stripPath(generated.getFolderPath()));
								File generatedFile = new File(generatedDirectory,generated.getFileName());
								try {
									FileUtils.writeByteArrayToFile(generatedFile, generated.getContent(), false);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						}

						private String stripPath(String folderPath) {
							final String mavenSourcePath = "src/main/java/";
							if (folderPath.indexOf(mavenSourcePath) > -1) {
								return folderPath.substring(folderPath.indexOf(mavenSourcePath)+mavenSourcePath.length());
							}
							return folderPath;
						}
					});
				} else {
					File generatedFile = new File(outputPath,result.getFileName());
					FileUtils.writeByteArrayToFile(generatedFile, result.getContent(), false);
				}
			}
			
		} catch (InstantiationException e) {
			throw new MojoExecutionException("Could not instantiate vorto code generator from given generatorClass",e);
		} catch (IllegalAccessException e) {
			throw new MojoExecutionException("Error during resolving code generator",e);
		} catch (ClassNotFoundException e) {
			throw new MojoExecutionException("Could not instantiate vorto code generator from given generatorClass",e);
		} catch (Exception e) {
			throw new MojoExecutionException("Problem during code generator invocation",e);
		}
	}
	
	private byte[] loadInformationModels() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ZipOutputStream zaos = new ZipOutputStream(baos);
		
		Files.walk(Paths.get((project.getBasedir().toURI()))).filter(new Predicate<Path>() {

			public boolean test(Path path) {
				return !path.toFile().isDirectory() && 
						(path.getFileName().toString().endsWith(ModelType.InformationModel.getExtension()) ||
						path.getFileName().toString().endsWith(ModelType.Functionblock.getExtension()) || 
						path.getFileName().toString().endsWith(ModelType.Datatype.getExtension()) || 
						path.getFileName().toString().endsWith(ModelType.Mapping.getExtension()));
			}
		}).forEach(new Consumer<Path>() {

			public void accept(Path t) {
				addToZip(zaos, t);
			}
		});
		
		return baos.toByteArray();
	}
	
	private static void addToZip(ZipOutputStream zaos, Path path) {
		try {
			ZipEntry zipEntry = new ZipEntry(path.toFile().getName());
			zaos.putNextEntry(zipEntry);
			zaos.write(IOUtils.toByteArray(new FileInputStream(path.toFile())));
			zaos.closeEntry();
		} catch(Exception ex) {
			// logging ?
		}
	}

	public String getGeneratorClass() {
		return generatorClass;
	}

	public File getOutputPath() {
		return outputPath;
	}
	
	
}
