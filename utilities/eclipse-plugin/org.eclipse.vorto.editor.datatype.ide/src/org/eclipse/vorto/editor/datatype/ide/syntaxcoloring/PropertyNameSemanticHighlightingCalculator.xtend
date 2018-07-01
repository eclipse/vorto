/**
 * Copyright (c) 2018 Bosch Software Innovations GmbH and others.
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
 */
package org.eclipse.vorto.editor.datatype.ide.syntaxcoloring

import javax.inject.Inject
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.ide.editor.syntaxcoloring.DefaultSemanticHighlightingCalculator
import org.eclipse.xtext.ide.editor.syntaxcoloring.IHighlightedPositionAcceptor
import org.eclipse.xtext.util.CancelIndicator

import org.eclipse.vorto.core.api.model.datatype.Property
import org.eclipse.vorto.core.api.model.datatype.DatatypePackage
import org.eclipse.vorto.editor.datatype.services.DatatypeGrammarAccess
import org.eclipse.xtext.ide.editor.syntaxcoloring.HighlightingStyles

class PropertyNameSemanticHighlightingCalculator extends DefaultSemanticHighlightingCalculator {
	@Inject package DatatypeGrammarAccess grammar

	override protected boolean highlightElement(EObject object, IHighlightedPositionAcceptor acceptor,
		CancelIndicator cancelIndicator) {
		switch (object) {
			Property: {
				highlightFeature(acceptor, object, DatatypePackage.eINSTANCE.property_Name, HighlightingStyles.DEFAULT_ID)
				return true
			}
			default: false
		}
	}
}