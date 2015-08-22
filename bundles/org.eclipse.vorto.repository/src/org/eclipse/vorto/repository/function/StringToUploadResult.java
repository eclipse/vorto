package org.eclipse.vorto.repository.function;

import org.eclipse.vorto.repository.model.UploadResult;

import com.google.common.base.Function;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StringToUploadResult implements Function<String, UploadResult> {

	private Gson gson = new GsonBuilder().create();

	@Override
	public UploadResult apply(String input) {
		return gson.fromJson(input, UploadResult.class);
	}

}
