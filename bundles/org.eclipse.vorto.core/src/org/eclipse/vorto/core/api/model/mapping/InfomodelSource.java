/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Infomodel Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfomodelSource#getModel <em>Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfomodelSource()
 * @model
 * @generated
 */
public interface InfomodelSource extends Source {
	/**
	 * Returns the value of the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' reference.
	 * @see #setModel(InformationModel)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfomodelSource_Model()
	 * @model
	 * @generated
	 */
	InformationModel getModel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfomodelSource#getModel <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(InformationModel value);

} // InfomodelSource
