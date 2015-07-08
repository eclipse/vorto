/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Information Model Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InformationModelElement#getFunctionblockModel <em>Functionblock Model</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InformationModelElement#getTail <em>Tail</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInformationModelElement()
 * @model
 * @generated
 */
public interface InformationModelElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Functionblock Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Functionblock Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Functionblock Model</em>' reference.
	 * @see #setFunctionblockModel(FunctionblockModel)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInformationModelElement_FunctionblockModel()
	 * @model
	 * @generated
	 */
	FunctionblockModel getFunctionblockModel();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelElement#getFunctionblockModel <em>Functionblock Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Functionblock Model</em>' reference.
	 * @see #getFunctionblockModel()
	 * @generated
	 */
	void setFunctionblockModel(FunctionblockModel value);

	/**
	 * Returns the value of the '<em><b>Tail</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tail</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tail</em>' containment reference.
	 * @see #setTail(FunctionBlockElement)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInformationModelElement_Tail()
	 * @model containment="true"
	 * @generated
	 */
	FunctionBlockElement getTail();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InformationModelElement#getTail <em>Tail</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tail</em>' containment reference.
	 * @see #getTail()
	 * @generated
	 */
	void setTail(FunctionBlockElement value);

} // InformationModelElement
