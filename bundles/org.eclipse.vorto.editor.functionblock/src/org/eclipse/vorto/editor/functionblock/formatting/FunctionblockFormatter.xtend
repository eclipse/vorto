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
		// formatting for Comments
		c.setLinewrap(1, 1, 2).before(SL_COMMENTRule)
		c.setLinewrap(1, 1, 2).before(ML_COMMENTRule)
		c.setLinewrap(1, 1, 1).after(ML_COMMENTRule)
		c.setAutoLinewrap(120)

		// Line wrap for all basic function block info, like displayname, description etc.
		c.setLinewrap(1).after(f.modelReferenceAccess.group)

		c.setLinewrap(1).after(f.functionblockModelAccess.namespaceAssignment_2)
		c.setLinewrap(1).after(f.functionblockModelAccess.versionAssignment_4)
		c.setLinewrap(1).after(f.functionBlockAccess.displaynameAssignment_2)
		c.setLinewrap(1).after(f.functionBlockAccess.descriptionAssignment_3_1)
		c.setLinewrap(1).after(f.functionBlockAccess.categoryAssignment_5)

		//Line wrap for every property entries in all function block constructs like configuration, status etc. 
		c.setLinewrap(1).after(f.configurationAccess.propertiesAssignment_3)
		c.setLinewrap(1).after(f.statusAccess.propertiesAssignment_3)
		c.setLinewrap(1).after(f.faultAccess.propertiesAssignment_3)
		c.setLinewrap(1).after(f.eventAccess.propertiesAssignment_2)
		c.setLinewrap(1).before(f.findKeywords("mandatory").get(0))
		c.setLinewrap(1).before(f.findKeywords("optional").get(0))
		
		// Line wrap and indentation between function block sub elements.
		findKeywordPairs("{","}").forEach[
			c.setLinewrap(1).after(first)
			c.setLinewrap(1).before(second)
			c.setLinewrap(2).after(second)
			c.setIndentationIncrement().after(first)
			c.setIndentationDecrement().before(second)
		]

		//Formatting for operation paranthesis
		findKeywordPairs("(",")").forEach[
			c.setNoSpace().before(first)
			c.setNoSpace().after(first)
			c.setNoSpace().before(second)
		]

		//Formatting for constraint block
		findKeywordPairs("<",">").forEach[
			c.setSpace(" ").before(first)
			c.setNoSpace().after(first)
			c.setNoSpace().before(second)
		]

		//Line wrap for each operation.
		c.setLinewrap(1).after(f.operationAccess.group)
	
		
		//Remove extra spaces between constraint parameters.
		c.setNoSpace().before(f.propertyAccess.commaKeyword_5_2_0)
		
		c.setNoSpace().after(f.propertyAccess.commaKeyword_5_2_0)
		
		//Remove extra spaces between method parameters.
		c.setNoSpace().before(f.operationAccess.commaKeyword_2_1_0)
		c.setNoSpace().after(f.operationAccess.commaKeyword_2_1_0)
		
	}
}
