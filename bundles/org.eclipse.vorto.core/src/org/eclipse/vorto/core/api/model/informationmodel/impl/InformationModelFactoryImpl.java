/**
 */
package org.eclipse.vorto.core.api.model.informationmodel.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.vorto.core.api.model.informationmodel.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class InformationModelFactoryImpl extends EFactoryImpl implements InformationModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static InformationModelFactory init() {
		try {
			InformationModelFactory theInformationModelFactory = (InformationModelFactory)EPackage.Registry.INSTANCE.getEFactory(InformationModelPackage.eNS_URI);
			if (theInformationModelFactory != null) {
				return theInformationModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new InformationModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case InformationModelPackage.INFORMATION_MODEL: return createInformationModel();
			case InformationModelPackage.FUNCTIONBLOCK_PROPERTY: return createFunctionblockProperty();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModel createInformationModel() {
		InformationModelImpl informationModel = new InformationModelImpl();
		return informationModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FunctionblockProperty createFunctionblockProperty() {
		FunctionblockPropertyImpl functionblockProperty = new FunctionblockPropertyImpl();
		return functionblockProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InformationModelPackage getInformationModelPackage() {
		return (InformationModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static InformationModelPackage getPackage() {
		return InformationModelPackage.eINSTANCE;
	}

} //InformationModelFactoryImpl
