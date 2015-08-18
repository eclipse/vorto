/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Block Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getFunctionBlockSourceElements <em>Function Block Source Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockMappingRule()
 * @model
 * @generated
 */
public interface FunctionBlockMappingRule extends EObject {
	/**
	 * Returns the value of the '<em><b>Function Block Source Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function Block Source Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function Block Source Elements</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockMappingRule_FunctionBlockSourceElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<FunctionBlockSourceElement> getFunctionBlockSourceElements();

	/**
	 * Returns the value of the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' containment reference.
	 * @see #setTarget(FunctionBlockTargetElement)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getFunctionBlockMappingRule_Target()
	 * @model containment="true"
	 * @generated
	 */
	FunctionBlockTargetElement getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule#getTarget <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' containment reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(FunctionBlockTargetElement value);

} // FunctionBlockMappingRule
