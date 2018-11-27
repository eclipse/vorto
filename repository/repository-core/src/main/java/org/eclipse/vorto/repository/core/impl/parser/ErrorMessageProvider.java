package org.eclipse.vorto.repository.core.impl.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

@Component
public class ErrorMessageProvider {
	
	private Map<String, String> translationTable = getTranslationTable();

    private Map<String, String> getTranslationTable() {
        Type typeOfHashMap = new TypeToken<Map<String, String>>() { }.getType();
    	Gson gson = new GsonBuilder().create();
    	try {
			return gson.fromJson(new InputStreamReader(new ClassPathResource("error-messages.json").getInputStream()), typeOfHashMap);
		} catch (JsonIOException | JsonSyntaxException | IOException e) {
			throw new RuntimeException("Error loading error translation file", e);
		}
    }
	
	public String convertError(String message) {
		for(String key : translationTable.keySet()) {
			Pattern pattern = Pattern.compile(key);
			Matcher matcher = pattern.matcher(message);
			if (matcher.find()) {
				String[] groups = new String[matcher.groupCount()];
				for(int i=1; i <= matcher.groupCount(); i++) {
					groups[i-1] = matcher.group(i);
				}
				return String.format(translationTable.get(key), (Object[]) groups);
			}
	    }
		
		return message;
	}
	
}
