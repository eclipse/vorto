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

package org.eclipse.vorto.editor.infomodel.formatting

import com.google.inject.Inject
import org.eclipse.vorto.editor.infomodel.services.InformationModelGrammarAccess
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter
import org.eclipse.xtext.formatting.impl.FormattingConfig

/**
 * This class contains formatting for information model dsl.
 */
class InformationModelFormatter extends AbstractDeclarativeFormatter {

	@Inject extension InformationModelGrammarAccess dsl
	
	override protected void configureFormatting(FormattingConfig c) {
			
		//Basic information
		c.setLinewrap(1).after(dsl.modelReferenceAccess.group)
		c.setLinewrap(1).after(dsl.informationModelAccess.namespaceAssignment_2)
		c.setLinewrap(1).after(dsl.informationModelAccess.versionAssignment_4)
		c.setLinewrap(1).after(dsl.informationModelAccess.displaynameAssignment_6)
		c.setLinewrap(1).after(dsl.informationModelAccess.descriptionAssignment_7_1)
		c.setLinewrap(1).after(dsl.informationModelAccess.categoryAssignment_8_1)	
		
		//Comments
		c.setLinewrap(1, 1, 2).before(SL_COMMENTRule)
		c.setLinewrap(1, 1, 2).before(ML_COMMENTRule)
		c.setLinewrap(1, 1, 1).after(ML_COMMENTRule)
		c.setAutoLinewrap(120)
		
		//Functionblocks
		c.setLinewrap(1).before(dsl.functionblockPropertyAccess.nameAssignment_2)
		
		//Block Elements
		findKeywordPairs("{","}").forEach[
			c.setLinewrap(1).after(first)
			c.setLinewrap(1).before(second)
			c.setLinewrap(1).after(second)
			c.setIndentationIncrement().after(first)
			c.setIndentationDecrement().before(second)
		]
		c.setLinewrap(2).before(dsl.findKeywords("functionblocks").get(0))
		c.setLinewrap(2).before(dsl.findKeywords("infomodel").get(0))
	}
}