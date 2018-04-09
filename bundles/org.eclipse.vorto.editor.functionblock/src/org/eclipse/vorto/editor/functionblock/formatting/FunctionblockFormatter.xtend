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

package org.eclipse.vorto.editor.functionblock.formatting

import org.eclipse.vorto.editor.functionblock.services.FunctionblockGrammarAccess
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter
import org.eclipse.xtext.formatting.impl.FormattingConfig
import com.google.inject.Inject

class FunctionblockFormatter extends AbstractDeclarativeFormatter {
	
	@Inject extension FunctionblockGrammarAccess f

	
	override protected void configureFormatting(FormattingConfig c) {
		
		//Basic information
		c.setLinewrap(1).after(f.modelReferenceAccess.group)
		c.setLinewrap(1).after(f.functionblockModelAccess.namespaceAssignment_2)
		c.setLinewrap(1).after(f.functionblockModelAccess.versionAssignment_4)
		c.setLinewrap(1).after(f.functionblockModelAccess.displaynameAssignment_6)
		c.setLinewrap(1).after(f.functionblockModelAccess.descriptionAssignment_7_1)
		c.setLinewrap(1).after(f.functionblockModelAccess.categoryAssignment_8_1)
		
		//Comments
		c.setLinewrap(1, 1, 2).before(SL_COMMENTRule)
		c.setLinewrap(1, 1, 2).before(ML_COMMENTRule)
		c.setLinewrap(1, 1, 1).after(ML_COMMENTRule)
		c.setAutoLinewrap(120)

		//Properties
		c.setLinewrap(1).after(f.configurationAccess.propertiesAssignment_3)
		c.setLinewrap(1).after(f.statusAccess.propertiesAssignment_3)
		c.setLinewrap(1).after(f.faultAccess.propertiesAssignment_3)
		c.setLinewrap(1).after(f.eventAccess.propertiesAssignment_2)
		c.setLinewrap(1).before(f.findKeywords("mandatory").get(0))
		c.setLinewrap(1).before(f.findKeywords("optional").get(0))
		
		//Block Elements
		findKeywordPairs("{","}").forEach[
			c.setLinewrap(1).after(first)
			c.setLinewrap(1).before(second)
			c.setLinewrap(2).after(second)
			c.setIndentationIncrement().after(first)
			c.setIndentationDecrement().before(second)
		]
		c.setLinewrap(2).before(f.findKeywords("functionblock").get(0))

		//Operation Paranthesis
		findKeywordPairs("(",")").forEach[
			c.setNoSpace().before(first)
			c.setNoSpace().after(first)
			c.setNoSpace().before(second)
			c.setIndentationIncrement().after(first)
			c.setIndentationDecrement().before(second)
		]

		//Constraint block
		findKeywordPairs("<",">").forEach[
			c.setSpace(" ").before(first)
			c.setNoSpace().after(first)
			c.setNoSpace().before(second)
		]

		//Operations
		c.setLinewrap(1).after(f.operationAccess.group)
	
		//Constraint parameters.
		c.setNoSpace().before(f.propertyAccess.commaKeyword_5_3_0)
		c.setNoSpace().after(f.propertyAccess.commaKeyword_5_3_0)
		
		//Operation parameters.
		c.setNoSpace().before(f.operationAccess.commaKeyword_4_1_0)
		c.setLinewrap(1).after(f.operationAccess.commaKeyword_4_1_0)
		
	}
}
