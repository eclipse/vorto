package org.eclipse.vorto.codegen.webui.templates.service.bosch.internal.model

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class ThingSearchResultImplTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''ThingSearchResultImpl.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/internal/model'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.internal.model;
		
		import java.util.ArrayList;
		import java.util.Collection;
		import java.util.List;
		import java.util.stream.Collectors;
		
		import org.apache.commons.lang3.builder.ToStringBuilder;
		
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.Thing;
		import com.example.iot.«element.name.toLowerCase».service.bosch.model.ThingSearchResult;
		
		public class ThingSearchResultImpl implements ThingSearchResult {
		
		    private List<ThingImpl> items = null;
		    
		    private int nextPageOffset = -1;
		
		    public ThingSearchResultImpl() {  	
		    	this.items = new ArrayList<ThingImpl>(0);
		    }
		    
		    public ThingSearchResultImpl(List<ThingImpl> items, int nextPageOffset) {
		    	this.items = items;
		    	this.nextPageOffset = nextPageOffset;
		    }
		    
		    /**
		     * 
		     * @return
		     *     The items
		     */
		    public List<ThingImpl> getItems() {
		        return items;
		    }
		
		    /**
		     * 
		     * @param items
		     *     The items
		     */
		    public void setItems(List<ThingImpl> items) {
		        this.items = items;
		    }
		    
		
		    public int getNextPageOffset() {
				return nextPageOffset;
			}
		
			public void setNextPageOffset(int nextPageOffset) {
				this.nextPageOffset = nextPageOffset;
			}
		
			@Override
		    public String toString() {
		        return ToStringBuilder.reflectionToString(this);
		    }
		
			@Override
			public Collection<Thing> getThings() {
				return this.items.stream().map(result -> ((Thing)result)).collect(Collectors.toList());
			}
		
		}
		
		'''
	}
	
}
