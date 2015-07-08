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
		// formatting for Comments
		c.setLinewrap(1, 1, 2).before(SL_COMMENTRule)
		c.setLinewrap(1, 1, 2).before(ML_COMMENTRule)
		c.setLinewrap(1, 1, 1).after(ML_COMMENTRule)
		c.setAutoLinewrap(120)

		c.setLinewrap(1).after(f.modelReferenceAccess.group)

		c.setLinewrap(1).after(f.entityAccess.nameAssignment_6)
		c.setLinewrap(1).after(f.entityAccess.namespaceAssignment_1)
		c.setLinewrap(1).after(f.entityAccess.versionAssignment_3)
		
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

		//Formatting for constraint block
		findKeywordPairs("<",">").forEach[
			c.setSpace(" ").before(first)
			c.setNoSpace().after(first)
			c.setNoSpace().before(second)
		]
				
		//Remove extra spaces between constraint parameters.
		c.setNoSpace().before(f.propertyAccess.commaKeyword_5_2_0)
		c.setNoSpace().after(f.propertyAccess.commaKeyword_5_2_0)	
	}
}
