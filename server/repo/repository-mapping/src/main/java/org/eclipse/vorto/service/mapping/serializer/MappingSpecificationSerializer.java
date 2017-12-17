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
	
	private MappingSpecificationSerializer(IMappingSpecification specification) {
		this.specification = specification;
	}
	
	public static MappingSpecificationSerializer create(IMappingSpecification specification) {
		return new MappingSpecificationSerializer(specification);
	}
	
	public Iterator<IMappingSerializer> iterator() {
		List<IMappingSerializer> serializers = new ArrayList<IMappingSerializer>();
		for (ModelProperty fbProperty : specification.getInfoModel().getFunctionblocks()) {
			serializers.add(new FunctionblockMappingSerializer(specification, fbProperty.getName()));
		}
		serializers.add(new InformationModelMappingSerializer(specification));
		return serializers.iterator();
	}
	
}
