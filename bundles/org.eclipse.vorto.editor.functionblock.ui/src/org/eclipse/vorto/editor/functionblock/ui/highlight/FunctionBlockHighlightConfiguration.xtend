/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
 
 package org.eclipse.vorto.editor.functionblock.ui.highlight;

import org.eclipse.swt.graphics.RGB
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor
import org.eclipse.xtext.ui.editor.utils.TextStyle

public class FunctionBlockHighlightConfiguration extends
		DefaultHighlightingConfiguration {
	
	public static final String ENTITY_ID = "Entity ID";
	
	override void configure(IHighlightingConfigurationAcceptor acceptor) {
		acceptor.acceptDefaultHighlighting(ENTITY_ID, "ENTITYID", entityTextStyle());
		super.configure(acceptor);
	}
	
	def TextStyle entityTextStyle() {
		var textStyle = defaultTextStyle();
		textStyle.setBackgroundColor(new RGB(42, 0, 255));
		textStyle
	}
	
	
	
}
