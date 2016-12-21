/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
/**
 */
package org.eclipse.vorto.core.api.model.informationmodel;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.vorto.core.api.model.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModelFactory
 * @model kind="package"
 * @generated
 */
public interface InformationModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "informationmodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/vorto/metamodel/InformationModel";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "informationmodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	InformationModelPackage eINSTANCE = org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl <em>Information Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl
	 * @see org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl#getInformationModel()
	 * @generated
	 */
	int INFORMATION_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL__NAME = ModelPackage.MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL__NAMESPACE = ModelPackage.MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL__VERSION = ModelPackage.MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL__REFERENCES = ModelPackage.MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL__DESCRIPTION = ModelPackage.MODEL__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Displayname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL__DISPLAYNAME = ModelPackage.MODEL__DISPLAYNAME;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL__CATEGORY = ModelPackage.MODEL__CATEGORY;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL__PROPERTIES = ModelPackage.MODEL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Information Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INFORMATION_MODEL_FEATURE_COUNT = ModelPackage.MODEL_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.core.api.model.informationmodel.impl.FunctionblockPropertyImpl <em>Functionblock Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.core.api.model.informationmodel.impl.FunctionblockPropertyImpl
	 * @see org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl#getFunctionblockProperty()
	 * @generated
	 */
	int FUNCTIONBLOCK_PROPERTY = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_PROPERTY__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_PROPERTY__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_PROPERTY__TYPE = 2;

	/**
	 * The number of structural features of the '<em>Functionblock Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUNCTIONBLOCK_PROPERTY_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel <em>Information Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Information Model</em>'.
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModel
	 * @generated
	 */
	EClass getInformationModel();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Properties</em>'.
	 * @see org.eclipse.vorto.core.api.model.informationmodel.InformationModel#getProperties()
	 * @see #getInformationModel()
	 * @generated
	 */
	EReference getInformationModel_Properties();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty <em>Functionblock Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Functionblock Property</em>'.
	 * @see org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty
	 * @generated
	 */
	EClass getFunctionblockProperty();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getName()
	 * @see #getFunctionblockProperty()
	 * @generated
	 */
	EAttribute getFunctionblockProperty_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getDescription()
	 * @see #getFunctionblockProperty()
	 * @generated
	 */
	EAttribute getFunctionblockProperty_Description();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.vorto.core.api.model.informationmodel.FunctionblockProperty#getType()
	 * @see #getFunctionblockProperty()
	 * @generated
	 */
	EReference getFunctionblockProperty_Type();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	InformationModelFactory getInformationModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl <em>Information Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelImpl
		 * @see org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl#getInformationModel()
		 * @generated
		 */
		EClass INFORMATION_MODEL = eINSTANCE.getInformationModel();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INFORMATION_MODEL__PROPERTIES = eINSTANCE.getInformationModel_Properties();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.core.api.model.informationmodel.impl.FunctionblockPropertyImpl <em>Functionblock Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.core.api.model.informationmodel.impl.FunctionblockPropertyImpl
		 * @see org.eclipse.vorto.core.api.model.informationmodel.impl.InformationModelPackageImpl#getFunctionblockProperty()
		 * @generated
		 */
		EClass FUNCTIONBLOCK_PROPERTY = eINSTANCE.getFunctionblockProperty();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUNCTIONBLOCK_PROPERTY__NAME = eINSTANCE.getFunctionblockProperty_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUNCTIONBLOCK_PROPERTY__DESCRIPTION = eINSTANCE.getFunctionblockProperty_Description();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FUNCTIONBLOCK_PROPERTY__TYPE = eINSTANCE.getFunctionblockProperty_Type();

	}

} //InformationModelPackage
