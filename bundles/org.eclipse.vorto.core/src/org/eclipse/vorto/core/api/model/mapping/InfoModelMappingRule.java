/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Info Model Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getSourceElements <em>Source Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getInfoModelSourceElements <em>Info Model Source Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelMappingRule()
 * @model
 * @generated
 */
public interface InfoModelMappingRule extends EObject {
	/**
	 * Returns the value of the '<em><b>Source Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Elements</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelMappingRule_SourceElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<InfoModelSourceElement> getSourceElements();

	/**
	 * Returns the value of the '<em><b>Info Model Source Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Info Model Source Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Info Model Source Elements</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelMappingRule_InfoModelSourceElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<InfoModelSourceElement> getInfoModelSourceElements();

	/**
	 * Returns the value of the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' containment reference.
	 * @see #setTarget(InfoModelTargetElement)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelMappingRule_Target()
	 * @model containment="true"
	 * @generated
	 */
	InfoModelTargetElement getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getTarget <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' containment reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(InfoModelTargetElement value);

} // InfoModelMappingRule
