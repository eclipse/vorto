/**
 */
package org.eclipse.vorto.core.api.model.informationmodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage
 * @generated
 */
public interface InformationModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	InformationModelFactory eINSTANCE = org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Information Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Information Model</em>'.
	 * @generated
	 */
	InformationModel createInformationModel();

	/**
	 * Returns a new object of class '<em>Functionblock Property</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Functionblock Property</em>'.
	 * @generated
	 */
	FunctionblockProperty createFunctionblockProperty();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	InformationModelPackage getInformationModelPackage();

} //InformationModelFactory
