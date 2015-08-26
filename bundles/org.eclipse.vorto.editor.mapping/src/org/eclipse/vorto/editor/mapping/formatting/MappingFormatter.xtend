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
