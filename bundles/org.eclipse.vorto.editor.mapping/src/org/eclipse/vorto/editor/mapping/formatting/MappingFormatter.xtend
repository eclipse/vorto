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
import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter
import org.eclipse.xtext.formatting.impl.FormattingConfig
import org.eclipse.vorto.editor.mapping.services.MappingGrammarAccess
import org.eclipse.xtext.Keyword

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
		
		//Comments		
		c.setLinewrap(0, 1, 2).before(SL_COMMENTRule)
		c.setLinewrap(0, 1, 2).before(ML_COMMENTRule)
		c.setLinewrap(0, 1, 1).after(ML_COMMENTRule)
		
		//Block Elements
		stereoTypeAccess.findKeywordPairs("{","}").forEach[
			c.setNoLinewrap.after(first)
			c.setNoLinewrap.before(second)
		]
		
		c.setLinewrap(1).before(f.mappingModelAccess.modelKeyword_2)
		c.setLinewrap(1).before(f.mappingModelAccess.targetKeyword_4)
		
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

		for (Keyword k : findKeywords("from")) {
			c.setLinewrap(2).before(k)
			c.setNoLinewrap.after(k)
		}
		
		for (Keyword k : findKeywords("to")) {
			c.setLinewrap(1).before(k)
			c.setNoLinewrap.after(k)
		}
		
		for (Keyword k : f.stereoTypeAccess.findKeywords("with")) {
			c.setNoLinewrap.before(k)
			c.setNoLinewrap.after(k)
		}
		
		//Block Elements
		findKeywordPairs("{","}").forEach[
			c.setLinewrap(1).after(first)
			c.setLinewrap(1).before(second)
			c.setLinewrap(1).after(second)
			c.setIndentationIncrement().after(first)
			c.setIndentationDecrement().before(second)
		]
	}
}
