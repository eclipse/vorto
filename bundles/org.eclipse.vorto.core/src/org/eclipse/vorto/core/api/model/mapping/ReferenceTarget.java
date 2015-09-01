/**
 */
package org.eclipse.vorto.core.api.model.mapping;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reference Target</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.ReferenceTarget#getMappingModel <em>Mapping Model</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getReferenceTarget()
 * @model
 * @generated
 */
public interface ReferenceTarget extends Target {
	/**
	 * Returns the value of the '<em><b>Mapping Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Mapping Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mapping Model</em>' reference.
	 * @see #setMappingModel(MappingModel)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getReferenceTarget_MappingModel()
	 * @model
	 * @generated
	 */
	MappingModel getMappingModel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.ReferenceTarget#getMappingModel <em>Mapping Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mapping Model</em>' reference.
	 * @see #getMappingModel()
	 * @generated
	 */
	void setMappingModel(MappingModel value);

} // ReferenceTarget
