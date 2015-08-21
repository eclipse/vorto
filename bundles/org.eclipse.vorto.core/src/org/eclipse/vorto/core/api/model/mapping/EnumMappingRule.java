/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getEnumElements <em>Enum Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getEnumSourceElement <em>Enum Source Element</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumMappingRule()
 * @model
 * @generated
 */
public interface EnumMappingRule extends EObject {
	/**
	 * Returns the value of the '<em><b>Enum Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.EnumSourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enum Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enum Elements</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumMappingRule_EnumElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<EnumSourceElement> getEnumElements();

	/**
	 * Returns the value of the '<em><b>Enum Source Element</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.EnumSourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enum Source Element</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enum Source Element</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumMappingRule_EnumSourceElement()
	 * @model containment="true"
	 * @generated
	 */
	EList<EnumSourceElement> getEnumSourceElement();

	/**
	 * Returns the value of the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' containment reference.
	 * @see #setTarget(EnumTargetElement)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getEnumMappingRule_Target()
	 * @model containment="true"
	 * @generated
	 */
	EnumTargetElement getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.EnumMappingRule#getTarget <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' containment reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(EnumTargetElement value);

} // EnumMappingRule
