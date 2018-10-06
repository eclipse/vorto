package org.eclipse.vorto.service.mapping.spec;

import java.io.InputStream;
import java.lang.reflect.Type;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.content.BooleanAttributeProperty;
import org.eclipse.vorto.repository.api.content.EnumAttributeProperty;
import org.eclipse.vorto.repository.api.content.IPropertyAttribute;
import org.eclipse.vorto.repository.api.content.IReferenceType;
import org.eclipse.vorto.repository.api.content.PrimitiveType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class JsonMappingSpecificationReader implements IMappingSpecificationReader {

	protected Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
			.registerTypeAdapter(IReferenceType.class, new JsonDeserializer<IReferenceType>() {
				public IReferenceType deserialize(JsonElement jsonElement, Type type,
						JsonDeserializationContext context) throws JsonParseException {
					if (jsonElement.isJsonPrimitive()) {
						return PrimitiveType.valueOf(jsonElement.getAsString());
					}
					return context.deserialize(jsonElement, ModelId.class);
				}
			}).registerTypeAdapter(IPropertyAttribute.class, new JsonDeserializer<IPropertyAttribute>() {
				public IPropertyAttribute deserialize(JsonElement jsonElement, Type type,
						JsonDeserializationContext context) throws JsonParseException {
					if (jsonElement.getAsJsonObject().get("value").isJsonPrimitive()) {
						return context.deserialize(jsonElement, BooleanAttributeProperty.class);
					} else {
						return context.deserialize(jsonElement, EnumAttributeProperty.class);
					}
				}
			}).create();

	@Override
	public IMappingSpecification read(InputStream input) {
		try {
			return gson.fromJson(IOUtils.toString(input), MappingSpecification.class);
		} catch (Exception e) {
			throw new MappingSpecReadProblem(e);
		}
	}

}
