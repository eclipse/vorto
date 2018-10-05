package org.eclipse.vorto.service.mapping.spec;

import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.eclipse.vorto.repository.api.AbstractModel;
import org.eclipse.vorto.repository.api.ModelId;
import org.eclipse.vorto.repository.api.ModelType;
import org.eclipse.vorto.repository.api.content.BooleanAttributeProperty;
import org.eclipse.vorto.repository.api.content.EnumAttributeProperty;
import org.eclipse.vorto.repository.api.content.FunctionblockModel;
import org.eclipse.vorto.repository.api.content.IPropertyAttribute;
import org.eclipse.vorto.repository.api.content.IReferenceType;
import org.eclipse.vorto.repository.api.content.Infomodel;
import org.eclipse.vorto.repository.api.content.PrimitiveType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
			}).registerTypeAdapter(Map.class, new JsonDeserializer<Map<Object,Object>>() {
				
				public HashMap<Object, Object> createInstance(final Type type) {
					return new HashMap<>();
				}

				public Map<Object, Object> deserialize(JsonElement jsonElement, Type type,
						JsonDeserializationContext context) throws JsonParseException {
					final Type kt = ((ParameterizedType) type).getActualTypeArguments()[0];

					if (kt == ModelId.class) {
						final JsonObject p = (JsonObject) jsonElement;
						final HashMap<Object, Object> r = createInstance(type);
						for (final Map.Entry<String, JsonElement> e : p.entrySet()) {
							r.put(ModelId.fromPrettyFormat(e.getKey()),deserializeModel(e.getValue(),context));
						}
						return r;
					} else {
						return null;
					}
				}

				private AbstractModel deserializeModel(JsonElement value, JsonDeserializationContext context) {
					final JsonObject model  = (JsonObject) value;
					if (model.get("type").getAsString().equals(ModelType.InformationModel)) {
						return context.deserialize(model, Infomodel.class);
					} else if (model.get("type").getAsString().equals(ModelType.Functionblock)) {
						return context.deserialize(model, FunctionblockModel.class);
					}
					return null;
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
