/**
 */
package org.eclipse.vorto.core.api.model.mapping;

import org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Info Model Fb Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionblock <em>Functionblock</em>}</li>
 *   <li>{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionBlockElement <em>Function Block Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelFbElement()
 * @model
 * @generated
 */
public interface InfoModelFbElement extends InfoModelChild {
	/**
	 * Returns the value of the '<em><b>Functionblock</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Functionblock</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Functionblock</em>' reference.
	 * @see #setFunctionblock(FunctionblockProperty)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelFbElement_Functionblock()
	 * @model
	 * @generated
	 */
	FunctionblockProperty getFunctionblock();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionblock <em>Functionblock</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Functionblock</em>' reference.
	 * @see #getFunctionblock()
	 * @generated
	 */
	void setFunctionblock(FunctionblockProperty value);

	/**
	 * Returns the value of the '<em><b>Function Block Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Function Block Element</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Function Block Element</em>' containment reference.
	 * @see #setFunctionBlockElement(FunctionBlockElement)
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#getInfoModelFbElement_FunctionBlockElement()
	 * @model containment="true"
	 * @generated
	 */
	FunctionBlockElement getFunctionBlockElement();

	/**
	 * Sets the value of the '{@link org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement#getFunctionBlockElement <em>Function Block Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Function Block Element</em>' containment reference.
	 * @see #getFunctionBlockElement()
	 * @generated
	 */
	void setFunctionBlockElement(FunctionBlockElement value);

} // InfoModelFbElement
