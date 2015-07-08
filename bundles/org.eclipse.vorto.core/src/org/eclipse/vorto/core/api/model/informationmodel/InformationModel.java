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
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getDisplayname <em>Displayname</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getCategory <em>Category</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getInformationModel()
 * @model
 * @generated
 */
public interface InformationModel extends Model {
	/**
	 * Returns the value of the '<em><b>Displayname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Displayname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Displayname</em>' attribute.
	 * @see #setDisplayname(String)
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getInformationModel_Displayname()
	 * @model
	 * @generated
	 */
	String getDisplayname();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getDisplayname <em>Displayname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Displayname</em>' attribute.
	 * @see #getDisplayname()
	 * @generated
	 */
	void setDisplayname(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getInformationModel_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' attribute.
	 * @see #setCategory(String)
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage#getInformationModel_Category()
	 * @model
	 * @generated
	 */
	String getCategory();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getCategory <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category</em>' attribute.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(String value);

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
