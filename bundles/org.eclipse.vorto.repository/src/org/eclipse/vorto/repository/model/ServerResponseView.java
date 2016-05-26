package org.eclipse.vorto.repository.model;

import java.util.List;

public class ServerResponseView {

	private String message;
	private Boolean isSuccess;
	private List<UploadResultView> obj;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Boolean getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public List<UploadResultView> getObj() {
		return obj;
	}
	public void setObj(List<UploadResultView> obj) {
		this.obj = obj;
	}
	
	
}
