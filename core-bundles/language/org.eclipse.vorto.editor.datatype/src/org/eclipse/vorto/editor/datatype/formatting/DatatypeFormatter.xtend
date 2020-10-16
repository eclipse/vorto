/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.editor.datatype.formatting

import com.google.inject.Inject
import org.eclipse.vorto.editor.datatype.services.DatatypeGrammarAccess
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
class DatatypeFormatter extends AbstractDeclarativeFormatter {

	@Inject extension DatatypeGrammarAccess f
	
	override protected void configureFormatting(FormattingConfig c) {
		
		//Basic information
		c.setLinewrap(1).after(f.modelReferenceAccess.group)
		c.setLinewrap(2).before(f.entityAccess.findKeywords("namespace").get(0)) 
		c.setLinewrap(1).after(f.entityAccess.namespaceAssignment_2)
		c.setLinewrap(1).after(f.entityAccess.versionAssignment_4)
		c.setLinewrap(1).after(f.entityAccess.displaynameAssignment_5_0_1)
		c.setLinewrap(1).after(f.entityAccess.descriptionAssignment_5_1_1)
		c.setLinewrap(1).after(f.entityAccess.categoryAssignment_5_2_1)
			
		c.setLinewrap(2).before(f.enumAccess.findKeywords("namespace").get(0))
		c.setLinewrap(1).after(f.enumAccess.namespaceAssignment_2)
		c.setLinewrap(1).after(f.enumAccess.versionAssignment_4)
		c.setLinewrap(1).after(f.enumAccess.displaynameAssignment_5_0_1)
		c.setLinewrap(1).after(f.enumAccess.descriptionAssignment_5_1_1)
		c.setLinewrap(1).after(f.enumAccess.categoryAssignment_5_2_1)
				
		//Comments
		c.setLinewrap(1, 1, 2).before(SL_COMMENTRule)
		c.setLinewrap(1, 1, 2).before(ML_COMMENTRule)
		c.setLinewrap(1, 1, 1).after(ML_COMMENTRule)
		c.setAutoLinewrap(120)

		//Properties
		c.setLinewrap(1).before(f.findKeywords("mandatory").get(0))
		c.setLinewrap(1).before(f.findKeywords("optional").get(0))
		c.setLinewrap(1).before(f.enumLiteralAccess.nameAssignment_0)
		
		//Block Elements
		findKeywordPairs("{","}").forEach[
			c.setLinewrap(1).after(first)
			c.setLinewrap(1).before(second)
			c.setLinewrap(2).after(second)
			c.setIndentationIncrement().after(first)
			c.setIndentationDecrement().before(second)
		]
		c.setLinewrap(2).before(f.findKeywords("entity").get(0))
		c.setLinewrap(2).before(f.findKeywords("enum").get(0))
		
		//Constraints
		findKeywordPairs("<",">").forEach[
			c.setSpace(" ").before(first)
			c.setNoSpace().after(first)
			c.setNoSpace().before(second)
		]
				
		//Constraint Parameters
		c.setNoSpace().before(f.propertyAccess.commaKeyword_6_3_0)
		c.setNoSpace().after(f.propertyAccess.commaKeyword_6_3_0)	
		
		c.setNoSpace().before(f.enumAccess.commaKeyword_10_1_0)
		
		//Property description
		c.setNoLinewrap().before(f.propertyAccess.descriptionAssignment_8)
		c.setLinewrap(2).after(f.propertyAccess.descriptionAssignment_8)
	}
}
