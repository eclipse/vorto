package org.eclipse.vorto.repository.model;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.vorto.core.api.model.datatype.Type;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;
import org.eclipse.vorto.core.api.model.model.Model;
import org.eclipse.vorto.core.api.model.model.ModelReference;

public class ModelEMFResource extends ModelResource {

	private Model model;
	
	public ModelEMFResource(Model model) {
		super(new ModelId(model.getName(), model.getNamespace(), model.getVersion()), createModelType(model));
		this.model = model;
	}
	
	private static ModelType createModelType(Model model) {
		if (model instanceof Type) {
			return ModelType.Datatype;
		} else if (model instanceof FunctionblockModel) {
			return ModelType.Functionblock;
		} else if (model instanceof InformationModel) {
			return ModelType.InformationModel;
		} else  {
			throw new UnsupportedOperationException("Model of type "+model.getClass()+ " cannot be parsed");
		}
	}
	
	public ModelId getId() {
		return new ModelId(model.getName(), model.getNamespace(), model.getVersion());
	}
	
	public String getDisplayName() {
		return model.getDescription() != null ?model.getDisplayname() : getId().getName();
	}

	public String getDescription() {
		return model.getDescription();
	}

	public List<ModelId> getReferences() {
		List<ModelId> references = new ArrayList<ModelId>(model.getReferences().size());
		for (ModelReference source : model.getReferences()) {
			references.add(ModelId.fromReference(source.getImportedNamespace(), source.getVersion()));
		}
		return references;
	}
	
	public byte[] toXMI() throws Exception {
		Resource resource = model.eResource().getResourceSet().createResource(URI.createURI(model.getName()+".xmi"));
		resource.getContents().add(model);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		resource.save(baos, null);
		return baos.toByteArray();
	}
	
}
