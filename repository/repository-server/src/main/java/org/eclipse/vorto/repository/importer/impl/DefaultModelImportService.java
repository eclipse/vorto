package org.eclipse.vorto.repository.importer.impl;

import java.util.List;
import java.util.Optional;

import org.eclipse.vorto.repository.importer.IModelImportService;
import org.eclipse.vorto.repository.importer.IModelImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DefaultModelImportService implements IModelImportService {

	@Autowired
	private List<IModelImporter> modelImporters;
	
	@Override
	public List<IModelImporter> getImporters() {
		return modelImporters;
	}

	@Override
	public Optional<IModelImporter> getImporterByKey(final String key) {
		return modelImporters.stream().filter(p -> p.getKey().equals(key)).findFirst();
	}

}
