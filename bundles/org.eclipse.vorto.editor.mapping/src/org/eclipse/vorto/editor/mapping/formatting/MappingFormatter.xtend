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

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation.html#formatting
 * on how and when to use it 
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class MappingFormatter extends AbstractDeclarativeFormatter {
	
	@Inject extension MappingGrammarAccess
	
	override protected configureFormatting(FormattingConfig c) {
		for(pair: findKeywordPairs('{', '}')) {
			c.setIndentation(pair.first, pair.second)
			c.setLinewrap(1).after(pair.first)
			c.setLinewrap(1).before(pair.second)
			c.setLinewrap(1).after(pair.second)
		}
		for(comma: findKeywords(',')) {
			c.setNoLinewrap().before(comma)
			c.setNoSpace().before(comma)
			c.setLinewrap().after(comma)
		}
		c.setLinewrap(0, 1, 2).before(SL_COMMENTRule)
		c.setLinewrap(0, 1, 2).before(ML_COMMENTRule)
		c.setLinewrap(0, 1, 1).after(ML_COMMENTRule)
	}
}
