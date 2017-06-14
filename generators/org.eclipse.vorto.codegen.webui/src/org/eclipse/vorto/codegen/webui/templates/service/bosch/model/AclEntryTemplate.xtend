package org.eclipse.vorto.codegen.webui.templates.service.bosch.model

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.codegen.webui.templates.TemplateUtils
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class AclEntryTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''AclEntry.java'''
	}
	
	override getPath(InformationModel context) {
		'''«TemplateUtils.getBaseApplicationPath(context)»/service/bosch/model'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
		'''
		package com.example.iot.«element.name.toLowerCase».service.bosch.model;
		
		import com.fasterxml.jackson.annotation.JsonIgnore;
		
		public class AclEntry {
			
			public boolean READ;
			public boolean WRITE;
			public boolean ADMINISTRATE;
			
			public AclEntry(boolean rEAD, boolean wRITE, boolean aDMIN) {
				super();
				READ = rEAD;
				WRITE = wRITE;
				ADMINISTRATE = aDMIN;
			}
			
			public AclEntry() {
				
			}
			
			@JsonIgnore
			public boolean isREAD() {
				return READ;
			}
			public void setREAD(boolean rEAD) {
				READ = rEAD;
			}
			@JsonIgnore
			public boolean isWRITE() {
				return WRITE;
			}
			public void setWRITE(boolean wRITE) {
				WRITE = wRITE;
			}
			@JsonIgnore
			public boolean isADMINISTRATE() {
				return ADMINISTRATE;
			}
			public void setADMINISTRATE(boolean ADMINISTRATE) {
				this.ADMINISTRATE = ADMINISTRATE;
			}
		
			@Override
			public String toString() {
				return "AclEntry [READ=" + READ + ", WRITE=" + WRITE + ", ADMINISTRATE=" + ADMINISTRATE + "]";
			}
		
			@Override
			public int hashCode() {
				final int prime = 31;
				int result = 1;
				result = prime * result + (ADMINISTRATE ? 1231 : 1237);
				result = prime * result + (READ ? 1231 : 1237);
				result = prime * result + (WRITE ? 1231 : 1237);
				return result;
			}
		
			@Override
			public boolean equals(Object obj) {
				if (this == obj)
					return true;
				if (obj == null)
					return false;
				if (getClass() != obj.getClass())
					return false;
				AclEntry other = (AclEntry) obj;
				if (ADMINISTRATE != other.ADMINISTRATE)
					return false;
				if (READ != other.READ)
					return false;
				if (WRITE != other.WRITE)
					return false;
				return true;
			}
			
			
			
		}
		
		'''
	}

}