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
package org.eclipse.vorto.repository.web.importer;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.vorto.repository.account.impl.IUserRepository;
import org.eclipse.vorto.repository.api.ModelInfo;
import org.eclipse.vorto.repository.api.upload.ModelHandle;
import org.eclipse.vorto.repository.api.upload.UploadModelResponse;
import org.eclipse.vorto.repository.api.upload.UploadModelResult;
import org.eclipse.vorto.repository.core.IModelRepository;
import org.eclipse.vorto.repository.core.impl.ITemporaryStorage;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.core.impl.utils.BulkUploadHelper;
import org.eclipse.vorto.repository.importer.FileUpload;
import org.eclipse.vorto.repository.importer.IModelImportService;
import org.eclipse.vorto.repository.importer.IModelImporter;
import org.eclipse.vorto.repository.web.core.exceptions.UploadTooLargeException;
import org.eclipse.vorto.repository.workflow.IWorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author Alexander Edelmann - Robert Bosch (SEA) Pte. Ltd.
 */
@Api(value="/import", description="Import of Information Models and other model types")
@RestController
@RequestMapping(value = "/rest/models/import")
public class ModelImportController {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    
    private final String ERROR_MSG_FORMAT = "Models cannot be imported because there {0,choice,1#is|1<are} {0} {0,choice,1#model|1<models} with {0,choice,1#error|1<errors}.";
	private final String SUCCESS_MSG_FORMAT = "{0,choice,1#The uploaded model is|1<All uploaded models are} valid and ready to import.";
	private final String UPLOAD_VALID = "Uploaded model %s is valid and ready for import.";
	private final String UPLOAD_FAIL = "Uploaded model %s has errors. Cannot import.";
    
	@Autowired
	private IModelImportService importerService;
	
	@Autowired
	private IModelRepository modelRepository;
	
	@Autowired
	private ITemporaryStorage uploadStorage;
	
	@Value("${server.config.maxModelSize}")
	private long maxModelSize;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IWorkflowService workflowService;
	
	@ApiOperation(value = "Upload and validate a single vorto model")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<UploadModelResponse> uploadModel(@ApiParam(value = "The vorto model file to upload", required = true) @RequestParam("file") MultipartFile file,@RequestParam("key") String key) {
		if (file.getSize() > maxModelSize) {
			throw new UploadTooLargeException("model", maxModelSize);
		}
		
		LOGGER.info("uploadModel: [" + file.getOriginalFilename() + "]");
		try {
			IModelImporter importer = importerService.getImporterByKey(key).get();
			UploadModelResult result = importer.upload(FileUpload.create(file.getOriginalFilename(), file.getBytes()), getUserContext());
			if (result.getReport().isValid()) {
				return validResponse(UploadModelResponse.newInstance(String.format(UPLOAD_VALID,file.getOriginalFilename()), result.getReport().isValid(), Arrays.asList(result)));
			} else {
				return validResponse(UploadModelResponse.newInstance(String.format(UPLOAD_FAIL,file.getOriginalFilename()), result.getReport().isValid(), Arrays.asList(result)));
			}
		} catch (IOException e) {
			LOGGER.error("Error upload model." + e.getStackTrace());
			UploadModelResponse errorResponse = new UploadModelResponse("Error during upload. Try again. " + e.getMessage(),
					false, null);
			return new ResponseEntity<UploadModelResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private UserContext getUserContext() {
		return UserContext.user(SecurityContextHolder.getContext().getAuthentication().getName());
	}

	@ApiOperation(value = "Upload and validate multiple vorto models")
	@RequestMapping(value = "multiple", method = RequestMethod.POST)
	public ResponseEntity<UploadModelResponse> uploadMultipleModels(@ApiParam(value = "The vorto model files to upload", required = true) @RequestParam("file") MultipartFile file) {
		if (file.getSize() > maxModelSize) {
			throw new UploadTooLargeException("model", maxModelSize);
		}
		
		LOGGER.info("Bulk upload Models: [" + file.getOriginalFilename() + "]");
		try {
			BulkUploadHelper bulkUploadService = new BulkUploadHelper(this.modelRepository, uploadStorage, userRepository);
			
			List<UploadModelResult> uploadModelResults = bulkUploadService.uploadMultiple(file.getBytes(),file.getOriginalFilename(),SecurityContextHolder.getContext().getAuthentication().getName());
			LOGGER.info("Models Uploaded: [" + uploadModelResults.size() + "]");
			UploadModelResponse serverResponse = (uploadModelResults.size() == 0)
					? new UploadModelResponse("Uploaded file doesn't have any valid models.", false, uploadModelResults)
					: createModelResponse(uploadModelResults);
			return validResponse(serverResponse);
		} catch (Exception e) {
			LOGGER.error("Error bulk upload models.",e);
			return erroredResponse("Error during upload. Try again. " + e.getMessage());
		}
	}

	@ApiOperation(value = "Checkin an uploaded vorto model into the repository")
	@RequestMapping(value = "/{handleId:.+}", method = RequestMethod.PUT)
	public ResponseEntity<List<ModelInfo>> doImport(@ApiParam(value = "The file name of uploaded vorto model", required = true) final @PathVariable String handleId, @RequestParam("key") String key) {
		LOGGER.info("Check in Model " + handleId);
		try {
			
			IModelImporter importer = importerService.getImporterByKey(key).get();
			
			List<ModelInfo> importedModels = importer.doImport(handleId, getUserContext());
			for (ModelInfo modelInfo : importedModels) {
				workflowService.start(modelInfo.getId());
			}
			
			return new ResponseEntity<List<ModelInfo>>(importedModels, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error checkin model. " + handleId, e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Checkin multiple uploaded vorto models into the  repository")
	@RequestMapping(value = "/checkInMultiple", method = RequestMethod.PUT)
	public ResponseEntity<Void> checkInMultiple(@ApiParam(value = "The file name of uploaded vorto model", required=true) final @RequestBody ModelHandle[] modelHandles, @RequestParam("key") String key) {
		LOGGER.info("Bulk check in models.");
		try {
			for (ModelHandle handle : modelHandles) {
				IModelImporter importer = importerService.getImporterByKey(key).get();
				
				List<ModelInfo> importedModels = importer.doImport(handle.getHandleId(), getUserContext());
				for (ModelInfo modelInfo : importedModels) {
					workflowService.start(modelInfo.getId());
				}
			}
			return new ResponseEntity<Void>(HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Error bulk checkin models.", e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "Returns a list of supported importers")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public List<ImporterInfo> getImporters() {
		List<ImporterInfo> importers = new ArrayList<ImporterInfo>();
		this.importerService.getImporters().stream().forEach(importer -> {
			importers.add(new ImporterInfo(importer.getKey(),importer.getSupportedFileExtensions(),importer.getShortDescription()));
		});
		
		return importers;
	}

	private ResponseEntity<UploadModelResponse> validResponse(UploadModelResponse successModelResponse) {
		return new ResponseEntity<UploadModelResponse>(successModelResponse, HttpStatus.OK);
	}

	private ResponseEntity<UploadModelResponse> erroredResponse(String errorMessage) {
		UploadModelResponse errorResponse = new UploadModelResponse("Error during checkin. Try again. " + errorMessage,
				false, null);
		return new ResponseEntity<UploadModelResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	private UploadModelResponse createModelResponse(List<UploadModelResult> uploadModelResults) {
		if(uploadModelResults.size() ==0)
			return new UploadModelResponse("No valid models found. Nothing to checkin",false,null);
		long noOfInvalidModels = uploadModelResults.stream().filter(result -> !result.getReport().isValid()).count();
		
		return (noOfInvalidModels) > 0 ? new UploadModelResponse(resolveMessage(ERROR_MSG_FORMAT, noOfInvalidModels),false,uploadModelResults) : new UploadModelResponse(resolveMessage(SUCCESS_MSG_FORMAT, uploadModelResults.size()),true,uploadModelResults); 
	}

	private String resolveMessage(String messageFormat, long occurences) {
		return MessageFormat.format(messageFormat, occurences);
	}
}
