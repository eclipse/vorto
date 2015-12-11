/*******************************************************************************
* Copyright (c) 2015 Bosch Software Innovations GmbH and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* and Eclipse Distribution License v1.0 which accompany this distribution.
*
* The Eclipse Public License is available at
* http://www.eclipse.org/legal/epl-v10.html
* The Eclipse Distribution License is available at
* http://www.eclipse.org/org/documents/edl-v10.php.
*
* Contributors:
* Bosch Software Innovations GmbH - Please refer to git log
*******************************************************************************/
package org.eclipse.vorto.editor.mapping.formatting

import com.google.inject.Inject
import org.eclipse.vorto.editor.mapping.services.MappingGrammarAccess
import org.eclipse.xtext.Keyword
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter
import org.eclipse.xtext.formatting.impl.FormattingConfig

/**
* This class contains custom formatting description.
*
* see : http://www.eclipse.org/Xtext/documentation.html#formatting
* on how and when to use it
*
* Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
*/
public class MappingFormatter extends AbstractDeclarativeFormatter {
	@Inject extension MappingGrammarAccess f

	override protected void configureFormatting(FormattingConfig c) {

		//Basic information
		c.setLinewrap(1).after(f.modelReferenceAccess.group)	
			
		c.setLinewrap(1).after(f.infoModelMappingModelAccess.namespaceAssignment_2);
		c.setLinewrap(1).after(f.infoModelMappingModelAccess.versionAssignment_4);
		c.setLinewrap(1).after(f.infoModelMappingModelAccess.displaynameAssignment_5_1)
		c.setLinewrap(1).after(f.infoModelMappingModelAccess.descriptionAssignment_6_1)
		c.setLinewrap(1).after(f.infoModelMappingModelAccess.categoryAssignment_7_1)		
		
		c.setLinewrap(1).after(f.functionBlockMappingModelAccess.namespaceAssignment_2);
		c.setLinewrap(1).after(f.functionBlockMappingModelAccess.versionAssignment_4);
		c.setLinewrap(1).after(f.functionBlockMappingModelAccess.displaynameAssignment_5_1)
		c.setLinewrap(1).after(f.functionBlockMappingModelAccess.descriptionAssignment_6_1)
		c.setLinewrap(1).after(f.functionBlockMappingModelAccess.categoryAssignment_7_1)	
				
		c.setLinewrap(1).after(f.entityMappingModelAccess.namespaceAssignment_2);
		c.setLinewrap(1).after(f.entityMappingModelAccess.versionAssignment_4);	
		c.setLinewrap(1).after(f.entityMappingModelAccess.displaynameAssignment_5_1)
		c.setLinewrap(1).after(f.entityMappingModelAccess.descriptionAssignment_6_1)
		c.setLinewrap(1).after(f.entityMappingModelAccess.categoryAssignment_7_1)	
				
		c.setLinewrap(1).after(f.enumMappingModelAccess.namespaceAssignment_2);
		c.setLinewrap(1).after(f.enumMappingModelAccess.versionAssignment_4);
		c.setLinewrap(1).after(f.enumMappingModelAccess.displaynameAssignment_5_1)
		c.setLinewrap(1).after(f.enumMappingModelAccess.descriptionAssignment_6_1)
		c.setLinewrap(1).after(f.enumMappingModelAccess.categoryAssignment_7_1)	
				
		c.setLinewrap(1).after(f.modelReferenceAccess.versionAssignment_3);	
									
		//Comments
		c.setLinewrap(0, 1, 2).before(SL_COMMENTRule)
		c.setLinewrap(0, 1, 2).before(ML_COMMENTRule)
		c.setLinewrap(0, 1, 1).after(ML_COMMENTRule)

		//Mapping related key words
		for (Keyword k : findKeywords(",")) {
			c.setNoSpace.before(k)
			c.setNoLinewrap.after(k)
		}
		for (Keyword k : findKeywords(".")) {
			c.setNoSpace.before(k)
			c.setNoSpace.after(k)
		}
		for (Keyword k : findKeywords(":")) {
			c.setNoLinewrap.after(k)
			c.setNoLinewrap.before(k)
		}
		
		for (Keyword k : findKeywords("infomodelmapping")) {
			c.setLinewrap(2).before(k)
			c.setNoLinewrap.after(k)
		}			
					
		for (Keyword k : findKeywords("functionblockmapping")) {
			c.setLinewrap(2).before(k)
			c.setNoLinewrap.after(k)
		}
		
		for (Keyword k : findKeywords("entitymapping")) {
			c.setLinewrap(2).before(k)
			c.setNoLinewrap.after(k)
		}		
		
		for (Keyword k : findKeywords("enummapping")) {
			c.setLinewrap(2).before(k)
			c.setNoLinewrap.after(k)
		}			
				
		for (Keyword k : findKeywords("from")) {
			c.setLinewrap(2).before(k)
			c.setNoLinewrap.after(k)
		}
		for (Keyword k : findKeywords("to")) {
			c.setLinewrap(1).before(k)
			c.setNoLinewrap.after(k)
		}
		
				
				
		for (Keyword k : f.stereoTypeTargetAccess.findKeywords("with")) {
			c.setNoLinewrap.before(k)
			c.setNoLinewrap.after(k)
		}

		//Block elements in stereo types environment
		stereoTypeTargetAccess.findKeywordPairs("{", "}").forEach [
			c.setNoLinewrap.after(first)
			c.setNoLinewrap.before(second)
		]

		//Remaining block elements
		findKeywordPairs("{", "}").forEach [
			c.setLinewrap(1).after(first)
			c.setLinewrap(1).before(second)
			c.setLinewrap(1).after(second)
			c.setIndentationIncrement().after(first)
			c.setIndentationDecrement().before(second)
		]
	}
}
