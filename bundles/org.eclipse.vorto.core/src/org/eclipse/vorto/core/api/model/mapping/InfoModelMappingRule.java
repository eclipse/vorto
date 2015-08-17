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
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getInfoModelSourceElements <em>Info Model Source Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getTargetElement <em>Target Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelMappingRule()
 * @model
 * @generated
 */
public interface InfoModelMappingRule extends EObject {
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
	 * Returns the value of the '<em><b>Target Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Element</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Element</em>' containment reference.
	 * @see #setTargetElement(TargetElement)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelMappingRule_TargetElement()
	 * @model containment="true"
	 * @generated
	 */
	TargetElement getTargetElement();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule#getTargetElement <em>Target Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Element</em>' containment reference.
	 * @see #getTargetElement()
	 * @generated
	 */
	void setTargetElement(TargetElement value);

} // InfoModelMappingRule
