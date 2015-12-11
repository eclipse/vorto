/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.common.util.EList;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getRules <em>Rules</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getTargetPlatform <em>Target Platform</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel()
 * @model
 * @generated
 */
public interface MappingModel extends Model {
	/**
	 * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.mapping.MappingRule}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rules</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rules</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_Rules()
	 * @model containment="true"
	 * @generated
	 */
	EList<MappingRule> getRules();

	/**
	 * Returns the value of the '<em><b>Target Platform</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Platform</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Platform</em>' attribute.
	 * @see #setTargetPlatform(String)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getMappingModel_TargetPlatform()
	 * @model
	 * @generated
	 */
	String getTargetPlatform();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.MappingModel#getTargetPlatform <em>Target Platform</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Platform</em>' attribute.
	 * @see #getTargetPlatform()
	 * @generated
	 */
	void setTargetPlatform(String value);

} // MappingModel
