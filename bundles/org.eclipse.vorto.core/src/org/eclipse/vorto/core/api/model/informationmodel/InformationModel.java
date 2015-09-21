/**
 */
package org.eclipse.vorto.core.api.model.informationmodel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.vorto.core.api.model.model.Model;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Information Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getInformationModel()
 * @model
 * @generated
 */
public interface InformationModel extends Model {
	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getInformationModel_Properties()
	 * @model containment="true"
	 * @generated
	 */
	EList<FunctionblockProperty> getProperties();

} // InformationModel
