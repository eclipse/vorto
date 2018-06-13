package org.eclipse.vorto.repository.api.upload;

public class StagingResult {
	private String importer;
	private boolean valid;
	private String errorMessage;
	private String stagingId;
	private Object stagingDetails;

	public static StagingResult success(String importer, String stagingId, Object stagingDetails) {
		StagingResult result = new StagingResult();
		
		result.setImporter(importer);
		result.setStagingId(stagingId);
		result.setValid(true);
		result.setStagingDetails(stagingDetails);
		
		return result;
	}
	
	public static StagingResult fail(String importer, String errorMessage, Object stagingDetails) {
		StagingResult result = new StagingResult();
		
		result.setImporter(importer);
		result.setValid(false);
		result.setErrorMessage(errorMessage);
		result.setStagingDetails(stagingDetails);
		
		return result;
	}
	
	private StagingResult() {
		
	}
	
	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getStagingId() {
		return stagingId;
	}

	public void setStagingId(String stagingId) {
		this.stagingId = stagingId;
	}

	public Object getStagingDetails() {
		return stagingDetails;
	}

	public void setStagingDetails(Object stagingDetails) {
		this.stagingDetails = stagingDetails;
	}

}
