package org.eclipse.vorto.service.mapping.spec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.jxpath.Functions;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.service.mapping.IMappingSpecification;

public abstract class AbstractTestSpec implements IMappingSpecification {
	
	private static Map<ModelId, FunctionblockModel> FBS = new HashMap<ModelId, FunctionblockModel>(2);
	
	private Infomodel infomodel = new Infomodel(ModelId.fromPrettyFormat("devices.AWSIoTButton:1.0.0"),
			ModelType.InformationModel);

	public AbstractTestSpec() {
		createFBSpec();
	}

	@Override
	public Infomodel getInfoModel() {
		return infomodel;
	}
	
	protected abstract void createFBSpec();
	
	protected void addFunctionblockProperty(final String name, final FunctionblockModel fbm) {
		FBS.put(fbm.getId(), fbm);
		ModelProperty prop = new ModelProperty();
		prop.setName(name);
		prop.setType(fbm.getId());
		infomodel.getFunctionblocks().add(prop);
	}

	@Override
	public FunctionblockModel getFunctionBlock(ModelId modelId) {
		return FBS.get(modelId);
	}

	@Override
	public Optional<Functions> getCustomFunctions() {
		return Optional.empty();
	}
}
