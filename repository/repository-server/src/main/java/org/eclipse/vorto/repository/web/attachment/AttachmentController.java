package org.eclipse.vorto.repository.web.attachment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.attachment.AttachResult;
import org.eclipse.vorto.repository.api.attachment.Attachment;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest/model")
public class AttachmentController {
	
	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IModelRepository modelRepository;
	
	@ApiOperation(value = "Upload a file attachment for a model")
	@RequestMapping(method = RequestMethod.PUT, value = "/{namespace}/{name}/{version}/attachment", produces = "application/json")
	public AttachResult attach(
			@ApiParam(value = "namespace", required = true) @PathVariable String namespace, 
			@ApiParam(value = "name", required = true) @PathVariable String name, 
			@ApiParam(value = "version", required = true) @PathVariable String version, 
			@ApiParam(value = "The attachment file to upload", required = true) @RequestParam("file") MultipartFile file) {
		
		ModelId modelId = new ModelId(name, namespace, version);
		
		try {
			modelRepository.attachFile(modelId, file.getOriginalFilename(), file.getBytes(), getUserContext());
			
			return AttachResult.success(modelId, file.getOriginalFilename());
		} catch (IOException | FatalModelRepositoryException e) {
			LOGGER.error("Error while uploading []:", e);
			return AttachResult.fail(modelId, file.getOriginalFilename(), e.getMessage());
		}
	}
	
	@ApiOperation(value = "Get a list of attachments for a model")
	@RequestMapping(method = RequestMethod.GET, value = "/{namespace}/{name}/{version}/attachment", produces = "application/json")
	public List<Attachment> getAttachments(
			@ApiParam(value = "namespace", required = true) @PathVariable String namespace, 
			@ApiParam(value = "name", required = true) @PathVariable String name, 
			@ApiParam(value = "version", required = true) @PathVariable String version) {
		
		ModelId modelId = new ModelId(name, namespace, version);
		
		try {
			
			return modelRepository.getAttachments(modelId).stream()
				.map(filename -> Attachment.newInstance(modelId, filename))
				.collect(Collectors.toList());
			
		} catch (FatalModelRepositoryException e) {
			LOGGER.error("Error while getting attachments []:", e);
			return Collections.emptyList();
		}
	}
	
	@ApiOperation(value = "Download an attached file to a model")
	@RequestMapping(method = RequestMethod.GET, value = "/{namespace}/{name}/{version}/attachment/{filename:.+}")
	public void getAttachment(
			@ApiParam(value = "namespace", required = true) @PathVariable String namespace, 
			@ApiParam(value = "name", required = true) @PathVariable String name, 
			@ApiParam(value = "version", required = true) @PathVariable String version,
			@ApiParam(value = "filename", required = true) @PathVariable String filename,
			final HttpServletResponse response) {
		
		ModelId modelId = new ModelId(name, namespace, version);
		
		Optional<byte[]> content = modelRepository.getAttachmentContent(modelId, filename);
		
		try {
			if (content.isPresent()) {
				response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + filename);
				response.setContentType(APPLICATION_OCTET_STREAM);
				
				IOUtils.copy(new ByteArrayInputStream(content.get()), response.getOutputStream());
				response.flushBuffer();
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (IOException e) {
			LOGGER.error("Cannot get model attachment:", e);
		}
	}
	
	private UserContext getUserContext() {
		return UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());
	}
}
