package org.mycompany;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.vorto.mapping.engine.MappingEngine;
import org.eclipse.vorto.mapping.engine.normalized.InfomodelData;

public class Main {

	public static void main(String[] args) throws Exception {
		MappingEngine engine = MappingEngine.createFromInputStream(FileUtils.openInputStream(new File("src/main/resources/mappingspec.json")));
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("distance", "100m");
		InfomodelData result = engine.map(data);
		System.out.println(result);
	}
}
