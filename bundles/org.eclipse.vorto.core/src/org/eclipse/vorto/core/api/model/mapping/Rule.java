/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.Rule#getInformationModelElements <em>Information Model Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.Rule#getTargetElement <em>Target Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getRule()
 * @model
 * @generated
 */
public interface Rule extends EObject {
	/**
	 * Returns the value of the '<em><b>Information Model Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.InformationModelElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Information Model Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Information Model Elements</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getRule_InformationModelElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<InformationModelElement> getInformationModelElements();

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
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getRule_TargetElement()
	 * @model containment="true"
	 * @generated
	 */
	TargetElement getTargetElement();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.Rule#getTargetElement <em>Target Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Element</em>' containment reference.
	 * @see #getTargetElement()
	 * @generated
	 */
	void setTargetElement(TargetElement value);

} // Rule
