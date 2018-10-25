package org.eclipse.vorto.service.mapping.spec;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.FunctionLibrary;
import org.eclipse.vorto.mapping.engine.functions.IScriptEvalProvider;
import org.eclipse.vorto.mapping.engine.model.spec.IMappingSpecification;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.ModelProperty;

public abstract class AbstractTestSpec implements IMappingSpecification {
	
	private static Map<String, FunctionblockModel> FBS = new HashMap<String, FunctionblockModel>(2);
	
	private Infomodel infomodel = new Infomodel(ModelId.fromPrettyFormat("devices:AWSIoTButton:1.0.0"),
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
		FBS.put(name, fbm);
		ModelProperty prop = new ModelProperty();
		prop.setName(name);
		prop.setType(fbm.getId());
		infomodel.getFunctionblocks().add(prop);
	}

	@Override
	public FunctionblockModel getFunctionBlock(String name) {
		return FBS.get(name);
	}

	@Override
	public FunctionLibrary getScriptFunctions(IScriptEvalProvider provider) {
		return new FunctionLibrary();
	}
}
