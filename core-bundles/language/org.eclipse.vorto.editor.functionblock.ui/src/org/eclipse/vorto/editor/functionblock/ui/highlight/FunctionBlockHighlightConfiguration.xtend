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
