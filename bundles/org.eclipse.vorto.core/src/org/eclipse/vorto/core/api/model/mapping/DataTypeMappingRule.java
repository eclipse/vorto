/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Type Mapping Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getDataTypeMappingElements <em>Data Type Mapping Elements</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getDataTypeSourceElement <em>Data Type Source Element</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getTargetElement <em>Target Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypeMappingRule()
 * @model
 * @generated
 */
public interface DataTypeMappingRule extends EObject {
	/**
	 * Returns the value of the '<em><b>Data Type Mapping Elements</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.DataTypeSourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Type Mapping Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Type Mapping Elements</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypeMappingRule_DataTypeMappingElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<DataTypeSourceElement> getDataTypeMappingElements();

	/**
	 * Returns the value of the '<em><b>Data Type Source Element</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.DataTypeSourceElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Data Type Source Element</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Type Source Element</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypeMappingRule_DataTypeSourceElement()
	 * @model containment="true"
	 * @generated
	 */
	EList<DataTypeSourceElement> getDataTypeSourceElement();

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
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getDataTypeMappingRule_TargetElement()
	 * @model containment="true"
	 * @generated
	 */
	TargetElement getTargetElement();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule#getTargetElement <em>Target Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Element</em>' containment reference.
	 * @see #getTargetElement()
	 * @generated
	 */
	void setTargetElement(TargetElement value);

} // DataTypeMappingRule
