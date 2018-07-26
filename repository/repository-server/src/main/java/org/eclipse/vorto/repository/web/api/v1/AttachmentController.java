package org.eclipse.vorto.repository.web.api.v1;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.attachment.Attachment;
import org.eclipse.vorto.repository.core.FatalModelRepositoryException;
import org.eclipse.vorto.repository.core.FileContent;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.web.api.v1.dto.AttachResult;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value="/attachments", description="Attach files to models")
@RestController
@RequestMapping(value = "/api/v1/attachments")
public class AttachmentController {
	
	private static final String ATTACHMENT_FILENAME = "attachment; filename = ";
	private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
	private static final String CONTENT_DISPOSITION = "content-disposition";
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IModelRepository modelRepository;
	
	@ApiOperation(value = "Upload a file to be attached to a model")
	@RequestMapping(method = RequestMethod.PUT, value = "/{modelId:.+}", produces = "application/json")
	@PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') or hasPermission(T(org.eclipse.vorto.repository.api.ModelId).fromPrettyFormat(#modelId),'model:owner'))")
	public AttachResult attach(
			@ApiParam(value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany.MagneticSensor:1.0.0", required = true) @PathVariable String modelId, 
			@ApiParam(value = "The file to be uploaded as attachmment", required = true) @RequestParam("file") MultipartFile file) {
		
		ModelId modelID = ModelId.fromPrettyFormat(modelId);
			
		try {
			String fileName = URLDecoder.decode(file.getOriginalFilename(), "UTF-8");
			
			if (fileName.length() > 100) {
				return AttachResult.fail(modelID, fileName, "Name of File exceeds 100 Characters");
			}		
			modelRepository.attachFile(modelID, new FileContent(fileName, file.getBytes()), getUserContext());
			
			return AttachResult.success(modelID, fileName);
		} catch (IOException | FatalModelRepositoryException e) {
			LOGGER.error("Error while uploading []:", e);
			return AttachResult.fail(modelID, file.getOriginalFilename(), e.getMessage());
		}
	}
	
	@ApiOperation(value = "Get the list of file attachments for a model")
	@RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}", produces = "application/json")
	public List<Attachment> getAttachments(
			@ApiParam(value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany.MagneticSensor:1.0.0", required = true) @PathVariable String modelId) {
		
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
	
	@ApiOperation(value = "Get a specific file attachment for a model")
	@RequestMapping(method = RequestMethod.GET, value = "/{modelId:.+}/files/{filename:.+}")
	public void getAttachment(
			@ApiParam(value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany.MagneticSensor:1.0.0", required = true) @PathVariable String modelId,
			@ApiParam(value = "The name of the attached file that you want to retrieve", required = true) @PathVariable String filename,
			final HttpServletResponse response) {
		
		ModelId modelID = ModelId.fromPrettyFormat(modelId);
		
		try {
			String fileName = URLDecoder.decode(filename, "UTF-8");
			Optional<FileContent> content = modelRepository.getAttachmentContent(modelID, fileName);
			
			if (content.isPresent()) {
				response.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + fileName);
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
	
	@ApiOperation(value = "Delete a file attachment for a model")
	@RequestMapping(method = RequestMethod.DELETE, value = "/{modelId:.+}/files/{filename:.+}")
	@PreAuthorize("isAuthenticated() && (hasRole('ROLE_ADMIN') or hasPermission(T(org.eclipse.vorto.repository.api.ModelId).fromPrettyFormat(#modelId),'model:owner'))")
	public ResponseEntity<Void> deleteAttachment(@ApiParam(value = "The ID of the vorto model in namespace.name:version format, e.g. com.mycompany.MagneticSensor:1.0.0", required = true) @PathVariable String modelId,
			@ApiParam(value = "The name of the attached file that you want to delete", required = true) @PathVariable String filename) {
		
		ModelId modelIdObject = ModelId.fromPrettyFormat(modelId);
		
		try {
			String fileName = URLDecoder.decode(filename, "UTF-8");
			
			if (!modelRepository.deleteAttachment(modelIdObject, fileName)) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Cannot decode name of attachment:", e);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	private UserContext getUserContext() {
		return UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());
	}
}
