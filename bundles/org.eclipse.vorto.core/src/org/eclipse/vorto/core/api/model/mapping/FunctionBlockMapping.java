/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Block Mapping</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping#getFunctionBlockMappingRules <em>Function Block Mapping Rules</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockMapping()
 * @model
 * @generated
 */
public interface FunctionBlockMapping extends Mapping {
	/**
	 * Returns the value of the '<em><b>Function Block Mapping Rules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function Block Mapping Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function Block Mapping Rules</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockMapping_FunctionBlockMappingRules()
	 * @model containment="true"
	 * @generated
	 */
	EList<FunctionBlockMappingRule> getFunctionBlockMappingRules();

} // FunctionBlockMapping
