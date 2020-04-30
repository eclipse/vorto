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
		c.setLinewrap(2).before(dsl.findKeywords("namespace").get(0))
		c.setLinewrap(1).after(dsl.informationModelAccess.namespaceAssignment_4)
		c.setLinewrap(1).after(dsl.informationModelAccess.versionAssignment_6)
		c.setLinewrap(1).after(dsl.informationModelAccess.displaynameAssignment_7_0_1)
		c.setLinewrap(1).after(dsl.informationModelAccess.descriptionAssignment_7_1_1)
		c.setLinewrap(1).after(dsl.informationModelAccess.categoryAssignment_7_2_1)
		
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