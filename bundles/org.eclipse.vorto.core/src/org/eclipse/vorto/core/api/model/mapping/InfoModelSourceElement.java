/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Info Model Source Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement#getInfoModel <em>Info Model</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement#getInfoModelChild <em>Info Model Child</em>}</li>
 * </ul>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelSourceElement()
 * @model
 * @generated
 */
public interface InfoModelSourceElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Info Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Info Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Info Model</em>' reference.
	 * @see #setInfoModel(InformationModel)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelSourceElement_InfoModel()
	 * @model
	 * @generated
	 */
	InformationModel getInfoModel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement#getInfoModel <em>Info Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Info Model</em>' reference.
	 * @see #getInfoModel()
	 * @generated
	 */
	void setInfoModel(InformationModel value);

	/**
	 * Returns the value of the '<em><b>Info Model Child</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Info Model Child</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Info Model Child</em>' containment reference.
	 * @see #setInfoModelChild(InfoModelChild)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelSourceElement_InfoModelChild()
	 * @model containment="true"
	 * @generated
	 */
	InfoModelChild getInfoModelChild();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement#getInfoModelChild <em>Info Model Child</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Info Model Child</em>' containment reference.
	 * @see #getInfoModelChild()
	 * @generated
	 */
	void setInfoModelChild(InfoModelChild value);

} // InfoModelSourceElement
