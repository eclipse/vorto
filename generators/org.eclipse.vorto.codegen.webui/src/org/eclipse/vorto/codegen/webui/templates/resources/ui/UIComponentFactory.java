package org.eclipse.vorto.codegen.webui.templates.resources.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.vorto.codegen.api.InvocationContext;
import org.eclipse.vorto.codegen.api.mapping.IMapped;
import org.eclipse.vorto.codegen.webui.templates.resources.ui.components.BarChartUITemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.ui.components.DefaultUITemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.ui.components.GaugeUITemplate;
import org.eclipse.vorto.codegen.webui.templates.resources.ui.components.LocationMapUITemplate;
import org.eclipse.vorto.core.api.model.datatype.Property;
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

public class UIComponentFactory {

	private static final String STEREOTYPE_COMPONENT = "UIComponent";
	private static final String ATTRIBUTE_TYPE = "type";
	
	public static IFunctionBlockUITemplate getByModelId(FunctionblockModel fbm, InvocationContext ctx) {
		IMapped<FunctionblockModel> mappedElement = ctx.getMappedElement(fbm, STEREOTYPE_COMPONENT);
		
		String uiType = mappedElement.getAttributeValue(ATTRIBUTE_TYPE, "default");
		
		if (uiType.equalsIgnoreCase("barchart")) {
			List<String> properties = new ArrayList<String>();
			for (Property property : fbm.getFunctionblock().getStatus().getProperties()) {
				IMapped<Property> uiAttribute = ctx.getMappedElement(property, STEREOTYPE_COMPONENT);
				String uiProperty = uiAttribute.getAttributeValue(ATTRIBUTE_TYPE, "");
				if (uiProperty.equalsIgnoreCase("value")) {
					properties.add(property.getName());
				}
			}
			return new BarChartUITemplate(properties);
			
		} else if (uiType.equalsIgnoreCase("map")) {
			String latitude = null;
			String longitude = null;
			for (Property property : fbm.getFunctionblock().getStatus().getProperties()) {
				IMapped<Property> uiAttribute = ctx.getMappedElement(property, STEREOTYPE_COMPONENT);
				String uiProperty = uiAttribute.getAttributeValue(ATTRIBUTE_TYPE, "");
				if (uiProperty.equalsIgnoreCase("longitude")) {
					longitude = property.getName();
				} else if (uiProperty.equalsIgnoreCase("latitude")) {
					latitude = property.getName();
				}
			}
			return new LocationMapUITemplate(longitude, latitude);
			
		} else if (uiType.equalsIgnoreCase("gauge")) {
			String value = null;
			String min = null;
			String max = null;
			String symbol = null;
			for (Property property : fbm.getFunctionblock().getStatus().getProperties()) {
				IMapped<Property> uiAttribute = ctx.getMappedElement(property, STEREOTYPE_COMPONENT);
				String uiProperty = uiAttribute.getAttributeValue(ATTRIBUTE_TYPE, "");
				if (uiProperty.equalsIgnoreCase("value")) {
					value = property.getName();
				} else if (uiProperty.equalsIgnoreCase("min")) {
					min = property.getName();
				} else if (uiProperty.equalsIgnoreCase("max")) {
					max = property.getName();
				} else if (uiProperty.equalsIgnoreCase("symbol")) {
					symbol = property.getName();
				}
			}
			
			return new GaugeUITemplate(symbol,min,max,value);
		} else {
			return new DefaultUITemplate();
		}
	}
}
