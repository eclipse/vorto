package org.eclipse.vorto.repository.importer;




public class DetailedReport {

	public enum MODEL_OWNER {
		OWNER(0),
		NOT_OWNER(1);
		
		private int value;
		private MODEL_OWNER(int value) {
		      this.value = value;
		}
		
		public int getValue() {
		      return value;   
		}
	}
	
	public enum USER_ROLE {
		USER(0),
		ADMIN(1);
		
		private int value;
		private USER_ROLE(int value) {
		      this.value = value;
		}
		
		public int getValue() {
		      return value;   
		}
	}
	
	public enum REPORT_MESSAGE_TYPE {
		ERROR,
		WARNING;
	}
	
	private MODEL_OWNER modelOwner;
	private USER_ROLE currentUserRole;
	private boolean isModelExists;
	private String message;
	private REPORT_MESSAGE_TYPE messageType;
	
   

	public DetailedReport(MODEL_OWNER modelOwner, USER_ROLE currentUserRole, boolean isModelExists, String message,
			REPORT_MESSAGE_TYPE messageType) {
		super();
		this.modelOwner = modelOwner;
		this.currentUserRole = currentUserRole;
		this.isModelExists = isModelExists;
		this.message = message;
		this.messageType = messageType;
	}

	public MODEL_OWNER getModelOwner() {
		return modelOwner;
	}

	public void setModelOwner(MODEL_OWNER modelOwner) {
		this.modelOwner = modelOwner;
	}

	public USER_ROLE getCurrentUserRole() {
		return currentUserRole;
	}

	public void setCurrentUserRole(USER_ROLE currentUserRole) {
		this.currentUserRole = currentUserRole;
	}

	public boolean isModelExists() {
		return isModelExists;
	}

	public void setModelExists(boolean isModelExists) {
		this.isModelExists = isModelExists;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public REPORT_MESSAGE_TYPE getMessageType() {
		return messageType;
	}

	public void setMessageType(REPORT_MESSAGE_TYPE messageType) {
		this.messageType = messageType;
	}
	
	
	
}
