package org.eclipse.vorto.codegen.api.utils

import org.eclipse.vorto.core.api.model.mapping.Attribute
import org.eclipse.vorto.core.api.model.mapping.Target
import org.eclipse.vorto.core.api.model.mapping.StereoTypeTarget

final class MappingRuleUtils {
	
	public static def boolean getAttributeBoolValue(Target target, String attributeName, boolean defaultResult) {
		if (target != null && target instanceof StereoTypeTarget) {
			for (Attribute attribute : (target as StereoTypeTarget).attributes) {
				if (attribute.name.equalsIgnoreCase(attributeName)) {
					return Boolean.parseBoolean(attribute.value);
				}
			}
		}
		
		return defaultResult;
	}
	
	public static def String getAttributeValue(Target target, String attributeName, String defaultResult) {
		if (target != null) {
			for (Attribute attribute : (target as StereoTypeTarget).attributes) {
				if (attribute.name.equalsIgnoreCase(attributeName)) {
					return attribute.value;
				}
			}
		}
		
		return defaultResult;
	}
}