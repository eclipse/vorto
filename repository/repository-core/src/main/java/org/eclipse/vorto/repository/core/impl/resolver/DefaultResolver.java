package org.eclipse.vorto.repository.core.impl.resolver;

import java.util.Optional;

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingRule;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.core.ModelFileContent;
import org.eclipse.vorto.repository.core.ModelInfo;
import org.eclipse.vorto.repository.web.core.dto.ResolveQuery;
import org.springframework.stereotype.Service;

@Service
public class DefaultResolver extends AbstractResolver {

	@Override
	protected ModelId doResolve(ModelInfo mappingModelResource, ResolveQuery query) {
		ModelFileContent content = this.repository.getModelContent(mappingModelResource.getId());
		MappingModel mappingModel = (MappingModel)content.getModel();
		Optional<MappingRule> objectRule = mappingModel.getRules().stream().filter(rule -> rule.getTarget() instanceof StereoTypeTarget && ((StereoTypeTarget)rule.getTarget()).getName().equals(query.getStereoType())).findFirst();							
		
		if (objectRule.isPresent()) {
			Optional<Attribute> objectIdAttribute = ((StereoTypeTarget)objectRule.get().getTarget()).getAttributes().stream().filter(attribute -> attribute.getName().equals(query.getAttributeId())).findFirst();
			if (objectIdAttribute.isPresent() && objectIdAttribute.get().getValue().equals(query.getAttributeValue())) {
				return ModelId.fromReference(mappingModel.getReferences().get(0).getImportedNamespace(),mappingModel.getReferences().get(0).getVersion());
			}
		}
		return null;
	}

}
