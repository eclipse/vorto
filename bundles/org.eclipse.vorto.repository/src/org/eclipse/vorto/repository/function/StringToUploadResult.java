/*******************************************************************************
 * Copyright (c) 2015,2016 Bosch Software Innovations GmbH and others.
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
 *******************************************************************************/
package org.eclipse.vorto.repository.function;

import java.util.List;

import org.eclipse.vorto.repository.model.ServerResponseView;
import org.eclipse.vorto.repository.model.UploadResultView;

import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class StringToUploadResult implements Function<String, UploadResultView> {

	private Gson gson = new GsonBuilder().create();

	@Override
	public UploadResultView apply(String input) {
		List<UploadResultView> result = gson.fromJson(input, ServerResponseView.class).getObj();
		//This is temporary fix to make it compatible with older interfaces
		//This check would be removed on latest repository deployments.
		if(result == null) {
			return handlePreviousVersionUploadResult(input);
		}
		return result.get(0);
	}

	/**
	 * Temporarily handle the Upload result to make it compatible 
	 * with older interfaces.
	 * @param input json string with older format
	 * @return uploadresultview 
	 */
	private UploadResultView handlePreviousVersionUploadResult(String input) {
		UploadResultView uploadResult = null;
		try {
			uploadResult = gson.fromJson(input, UploadResultView.class);
			if (uploadResult != null) {
				return uploadResult;
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		return emptyUploadResult();
	}

	
	/**
	 * Return empty error view incase of invalid json or null values.
	 * @return Empty Upload result with error message.
	 */
	private UploadResultView emptyUploadResult() {
		UploadResultView errorResult = new UploadResultView();
		errorResult.setErrorMessage("Error while uploading to remote repository.");
		errorResult.setValid(false);
		return errorResult;
	}

}
