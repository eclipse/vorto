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
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.attachment.dto.AttachResult;
import org.eclipse.vorto.repository.web.attachment.dto.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/rest/attachments")
public class AttachmentController {
	
	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IModelRepository modelRepository;
	
	@RequestMapping(method = RequestMethod.PUT, value = "/{modelId:.+}", produces = "application/json")
	@PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') or hasPermission(T(org.eclipse.vorto.repository.api.ModelId).fromPrettyFormat(#modelId),'model:owner'))")
	public AttachResult attach(
			@ApiParam(value = "modelId", required = true) @PathVariable String modelId, 
			@ApiParam(value = "The attachment file to upload", required = true) @RequestParam("file") MultipartFile file) {
		
		ModelId modelID = ModelId.fromPrettyFormat(modelId);
		
		try {
			modelRepository.attachFile(modelID, new FileContent(file.getOriginalFilename(), file.getBytes()), getUserContext());
			
			return AttachResult.success(modelID, file.getOriginalFilename());
		} catch (IOException | FatalModelRepositoryException e) {
			LOGGER.error("Error while uploading []:", e);
			return AttachResult.fail(modelID, file.getOriginalFilename(), e.getMessage());
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}", produces = "application/json")
	public List<Attachment> getAttachments(
			@ApiParam(value = "modelId", required = true) @PathVariable String modelId) {
		
		ModelId modelID = ModelId.fromPrettyFormat(modelId);
		
		try {
			
			return modelRepository.getAttachmentFilenames(modelID).stream()
				.map(filename -> Attachment.newInstance(modelID, filename))
				.collect(Collectors.toList());
			
		} catch (FatalModelRepositoryException e) {
			LOGGER.error("Error while getting attachments []:", e);
			return Collections.emptyList();
		}
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}/files/{filename:.+}")
	public void getAttachment(
			@ApiParam(value = "modelId", required = true) @PathVariable String modelId,
			@ApiParam(value = "filename", required = true) @PathVariable String filename,
			final HttpServletResponse response) {
		
		ModelId modelID = ModelId.fromPrettyFormat(modelId);
		
		Optional<FileContent> content = modelRepository.getAttachmentContent(modelID, filename);
		
		try {
			if (content.isPresent()) {
				response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + filename);
				response.setContentType(APPLICATION_OCTET_STREAM);
				
				IOUtils.copy(new ByteArrayInputStream(content.get().getContent()), response.getOutputStream());
				response.flushBuffer();
			} else {
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
		} catch (IOException e) {
			LOGGER.error("Cannot get model attachment:", e);
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value = "/{modelId:.+}/files/{filename:.+}")
	public ResponseEntity<Void> deleteAttachment(@ApiParam(value = "modelId", required = true) @PathVariable String modelId,
			@ApiParam(value = "filename", required = true) @PathVariable String filename) {
		
		ModelId modelIdObject = ModelId.fromPrettyFormat(modelId);
		
		if (!modelRepository.deleteAttachment(modelIdObject, filename)) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private UserContext getUserContext() {
		return UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());
	}
}
