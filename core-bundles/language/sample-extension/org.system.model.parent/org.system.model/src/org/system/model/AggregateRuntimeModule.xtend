/*
 * generated by Xtext 2.10.0
 */
package org.system.model

import com.google.inject.Provides
import org.eclipse.vorto.editor.functionblock.validation.TypeFileAccessingHelper
import org.eclipse.vorto.editor.functionblock.validation.TypeHelper

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
class AggregateRuntimeModule extends AbstractAggregateRuntimeModule {

	@Provides def TypeHelper getTypeHelper() {
		return new TypeFileAccessingHelper()
	}
}
