package org.eclipse.vorto.service.mapping.serializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.vorto.repository.api.content.ModelProperty;
import org.eclipse.vorto.service.mapping.internal.serializer.FunctionblockMappingSerializer;
import org.eclipse.vorto.service.mapping.internal.serializer.InformationModelMappingSerializer;
import org.eclipse.vorto.service.mapping.spec.IMappingSpecification;

public class MappingSpecificationSerializer  {

	private IMappingSpecification specification;
	private String targetPlatform;
	
	private MappingSpecificationSerializer(IMappingSpecification specification, String targetPlatform) {
		this.specification = specification;
		this.targetPlatform = targetPlatform;
	}
	
	public static MappingSpecificationSerializer create(IMappingSpecification specification, String targetPlaform) {
		return new MappingSpecificationSerializer(specification,targetPlaform);
	}
	
	public Iterator<IMappingSerializer> iterator() {
		List<IMappingSerializer> serializers = new ArrayList<IMappingSerializer>();
		for (ModelProperty fbProperty : specification.getInfoModel().getFunctionblocks()) {
			serializers.add(new FunctionblockMappingSerializer(specification,targetPlatform, fbProperty.getName()));
		}
		serializers.add(new InformationModelMappingSerializer(specification,targetPlatform));
		return serializers.iterator();
	}
	
}
