/**
 */
package org.eclipse.vorto.core.api.model.mapping.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.vorto.core.api.model.datatype.DatatypePackage;

import org.eclipse.vorto.core.api.model.functionblock.FunctionblockPackage;

import org.eclipse.vorto.core.api.model.informationmodel.InformationModelPackage;

import org.eclipse.vorto.core.api.model.mapping.Attribute;
import org.eclipse.vorto.core.api.model.mapping.ConfigurationElement;
import org.eclipse.vorto.core.api.model.mapping.DataTypeAttribute;
import org.eclipse.vorto.core.api.model.mapping.DataTypeMappingRule;
import org.eclipse.vorto.core.api.model.mapping.DataTypePropertyElement;
import org.eclipse.vorto.core.api.model.mapping.DataTypeSourceElement;
import org.eclipse.vorto.core.api.model.mapping.EntityExpression;
import org.eclipse.vorto.core.api.model.mapping.EntityExpressionRef;
import org.eclipse.vorto.core.api.model.mapping.EventElement;
import org.eclipse.vorto.core.api.model.mapping.FBTypeElement;
import org.eclipse.vorto.core.api.model.mapping.FBTypeElementChild;
import org.eclipse.vorto.core.api.model.mapping.FBTypeProperty;
import org.eclipse.vorto.core.api.model.mapping.FaultElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelChild;
import org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement;
import org.eclipse.vorto.core.api.model.mapping.InformationModelProperty;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression;
import org.eclipse.vorto.core.api.model.mapping.OperationElement;
import org.eclipse.vorto.core.api.model.mapping.StatusElement;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.TargetElement;

import org.eclipse.vorto.core.api.model.model.ModelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class MappingPackageImpl extends EPackageImpl implements MappingPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappingModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelSourceElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelChildEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelFbElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass informationModelPropertyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockSourceElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockElementAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockChildElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass operationElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass configurationElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass statusElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass faultElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eventElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fbTypeElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fbTypeElementChildEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fbTypePropertyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTypeMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTypeSourceElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTypePropertyElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityExpressionRefEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTypeAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass targetElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stereoTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass attributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass nestedEntityExpressionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityExpressionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum infoModelAttributeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum functionblockModelAttributeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum modelAttributeEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.vorto.core.api.model.mapping.MappingPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private MappingPackageImpl() {
		super(eNS_URI, MappingFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link MappingPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static MappingPackage init() {
		if (isInited) return (MappingPackage)EPackage.Registry.INSTANCE.getEPackage(MappingPackage.eNS_URI);

		// Obtain or create and register package
		MappingPackageImpl theMappingPackage = (MappingPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof MappingPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new MappingPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		InformationModelPackage.eINSTANCE.eClass();
		ModelPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theMappingPackage.createPackageContents();

		// Initialize created meta-data
		theMappingPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theMappingPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(MappingPackage.eNS_URI, theMappingPackage);
		return theMappingPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMappingModel() {
		return mappingModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMappingModel_InfoModelMappingRules() {
		return (EReference)mappingModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMappingModel_FunctionBlockMappingRules() {
		return (EReference)mappingModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getMappingModel_DataTypeMappingRules() {
		return (EReference)mappingModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelMappingRule() {
		return infoModelMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelMappingRule_InfoModelSourceElements() {
		return (EReference)infoModelMappingRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelMappingRule_TargetElement() {
		return (EReference)infoModelMappingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelSourceElement() {
		return infoModelSourceElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelSourceElement_InfoModel() {
		return (EReference)infoModelSourceElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelSourceElement_InfoModelChild() {
		return (EReference)infoModelSourceElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelChild() {
		return infoModelChildEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelFbElement() {
		return infoModelFbElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelFbElement_Functionblock() {
		return (EReference)infoModelFbElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelFbElement_FunctionBlockElement() {
		return (EReference)infoModelFbElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInformationModelProperty() {
		return informationModelPropertyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInformationModelProperty_Attribute() {
		return (EAttribute)informationModelPropertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockMappingRule() {
		return functionBlockMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockMappingRule_FunctionBlockSourceElements() {
		return (EReference)functionBlockMappingRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockMappingRule_TargetElement() {
		return (EReference)functionBlockMappingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockSourceElement() {
		return functionBlockSourceElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockSourceElement_Functionblock() {
		return (EReference)functionBlockSourceElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockSourceElement_FunctionBlockElement() {
		return (EReference)functionBlockSourceElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockElement() {
		return functionBlockElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockElementAttribute() {
		return functionBlockElementAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFunctionBlockElementAttribute_Attribute() {
		return (EAttribute)functionBlockElementAttributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockChildElement() {
		return functionBlockChildElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockChildElement_Type() {
		return (EReference)functionBlockChildElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockChildElement_TypeRef() {
		return (EReference)functionBlockChildElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOperationElement() {
		return operationElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getOperationElement_Operation() {
		return (EReference)operationElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getConfigurationElement() {
		return configurationElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStatusElement() {
		return statusElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFaultElement() {
		return faultElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEventElement() {
		return eventElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEventElement_Event() {
		return (EReference)eventElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFBTypeElement() {
		return fbTypeElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFBTypeElement_Property() {
		return (EReference)fbTypeElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFBTypeElement_Child() {
		return (EReference)fbTypeElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFBTypeElementChild() {
		return fbTypeElementChildEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFBTypeProperty() {
		return fbTypePropertyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFBTypeProperty_Property() {
		return (EReference)fbTypePropertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataTypeMappingRule() {
		return dataTypeMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataTypeMappingRule_DataTypeMappingElements() {
		return (EReference)dataTypeMappingRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataTypeMappingRule_DataTypeSourceElement() {
		return (EReference)dataTypeMappingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataTypeMappingRule_TargetElement() {
		return (EReference)dataTypeMappingRuleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataTypeSourceElement() {
		return dataTypeSourceElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataTypePropertyElement() {
		return dataTypePropertyElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataTypePropertyElement_TypeRef() {
		return (EReference)dataTypePropertyElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataTypePropertyElement_Attribute() {
		return (EReference)dataTypePropertyElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityExpressionRef() {
		return entityExpressionRefEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataTypeAttribute() {
		return dataTypeAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getDataTypeAttribute_Attribute() {
		return (EAttribute)dataTypeAttributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTargetElement() {
		return targetElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTargetElement_StereoTypes() {
		return (EReference)targetElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStereoType() {
		return stereoTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStereoType_Name() {
		return (EAttribute)stereoTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStereoType_Attributes() {
		return (EReference)stereoTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAttribute() {
		return attributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttribute_Name() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getAttribute_Value() {
		return (EAttribute)attributeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getNestedEntityExpression() {
		return nestedEntityExpressionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNestedEntityExpression_Ref() {
		return (EReference)nestedEntityExpressionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getNestedEntityExpression_Tail() {
		return (EReference)nestedEntityExpressionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityExpression() {
		return entityExpressionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityExpression_Entity() {
		return (EReference)entityExpressionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getInfoModelAttribute() {
		return infoModelAttributeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getFunctionblockModelAttribute() {
		return functionblockModelAttributeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnum getModelAttribute() {
		return modelAttributeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MappingFactory getMappingFactory() {
		return (MappingFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		mappingModelEClass = createEClass(MAPPING_MODEL);
		createEReference(mappingModelEClass, MAPPING_MODEL__INFO_MODEL_MAPPING_RULES);
		createEReference(mappingModelEClass, MAPPING_MODEL__FUNCTION_BLOCK_MAPPING_RULES);
		createEReference(mappingModelEClass, MAPPING_MODEL__DATA_TYPE_MAPPING_RULES);

		infoModelMappingRuleEClass = createEClass(INFO_MODEL_MAPPING_RULE);
		createEReference(infoModelMappingRuleEClass, INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS);
		createEReference(infoModelMappingRuleEClass, INFO_MODEL_MAPPING_RULE__TARGET_ELEMENT);

		infoModelSourceElementEClass = createEClass(INFO_MODEL_SOURCE_ELEMENT);
		createEReference(infoModelSourceElementEClass, INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL);
		createEReference(infoModelSourceElementEClass, INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD);

		infoModelChildEClass = createEClass(INFO_MODEL_CHILD);

		infoModelFbElementEClass = createEClass(INFO_MODEL_FB_ELEMENT);
		createEReference(infoModelFbElementEClass, INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK);
		createEReference(infoModelFbElementEClass, INFO_MODEL_FB_ELEMENT__FUNCTION_BLOCK_ELEMENT);

		informationModelPropertyEClass = createEClass(INFORMATION_MODEL_PROPERTY);
		createEAttribute(informationModelPropertyEClass, INFORMATION_MODEL_PROPERTY__ATTRIBUTE);

		functionBlockMappingRuleEClass = createEClass(FUNCTION_BLOCK_MAPPING_RULE);
		createEReference(functionBlockMappingRuleEClass, FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS);
		createEReference(functionBlockMappingRuleEClass, FUNCTION_BLOCK_MAPPING_RULE__TARGET_ELEMENT);

		functionBlockSourceElementEClass = createEClass(FUNCTION_BLOCK_SOURCE_ELEMENT);
		createEReference(functionBlockSourceElementEClass, FUNCTION_BLOCK_SOURCE_ELEMENT__FUNCTIONBLOCK);
		createEReference(functionBlockSourceElementEClass, FUNCTION_BLOCK_SOURCE_ELEMENT__FUNCTION_BLOCK_ELEMENT);

		functionBlockElementEClass = createEClass(FUNCTION_BLOCK_ELEMENT);

		functionBlockElementAttributeEClass = createEClass(FUNCTION_BLOCK_ELEMENT_ATTRIBUTE);
		createEAttribute(functionBlockElementAttributeEClass, FUNCTION_BLOCK_ELEMENT_ATTRIBUTE__ATTRIBUTE);

		functionBlockChildElementEClass = createEClass(FUNCTION_BLOCK_CHILD_ELEMENT);
		createEReference(functionBlockChildElementEClass, FUNCTION_BLOCK_CHILD_ELEMENT__TYPE);
		createEReference(functionBlockChildElementEClass, FUNCTION_BLOCK_CHILD_ELEMENT__TYPE_REF);

		operationElementEClass = createEClass(OPERATION_ELEMENT);
		createEReference(operationElementEClass, OPERATION_ELEMENT__OPERATION);

		configurationElementEClass = createEClass(CONFIGURATION_ELEMENT);

		statusElementEClass = createEClass(STATUS_ELEMENT);

		faultElementEClass = createEClass(FAULT_ELEMENT);

		eventElementEClass = createEClass(EVENT_ELEMENT);
		createEReference(eventElementEClass, EVENT_ELEMENT__EVENT);

		fbTypeElementEClass = createEClass(FB_TYPE_ELEMENT);
		createEReference(fbTypeElementEClass, FB_TYPE_ELEMENT__PROPERTY);
		createEReference(fbTypeElementEClass, FB_TYPE_ELEMENT__CHILD);

		fbTypeElementChildEClass = createEClass(FB_TYPE_ELEMENT_CHILD);

		fbTypePropertyEClass = createEClass(FB_TYPE_PROPERTY);
		createEReference(fbTypePropertyEClass, FB_TYPE_PROPERTY__PROPERTY);

		dataTypeMappingRuleEClass = createEClass(DATA_TYPE_MAPPING_RULE);
		createEReference(dataTypeMappingRuleEClass, DATA_TYPE_MAPPING_RULE__DATA_TYPE_MAPPING_ELEMENTS);
		createEReference(dataTypeMappingRuleEClass, DATA_TYPE_MAPPING_RULE__DATA_TYPE_SOURCE_ELEMENT);
		createEReference(dataTypeMappingRuleEClass, DATA_TYPE_MAPPING_RULE__TARGET_ELEMENT);

		dataTypeSourceElementEClass = createEClass(DATA_TYPE_SOURCE_ELEMENT);

		dataTypePropertyElementEClass = createEClass(DATA_TYPE_PROPERTY_ELEMENT);
		createEReference(dataTypePropertyElementEClass, DATA_TYPE_PROPERTY_ELEMENT__TYPE_REF);
		createEReference(dataTypePropertyElementEClass, DATA_TYPE_PROPERTY_ELEMENT__ATTRIBUTE);

		entityExpressionRefEClass = createEClass(ENTITY_EXPRESSION_REF);

		dataTypeAttributeEClass = createEClass(DATA_TYPE_ATTRIBUTE);
		createEAttribute(dataTypeAttributeEClass, DATA_TYPE_ATTRIBUTE__ATTRIBUTE);

		targetElementEClass = createEClass(TARGET_ELEMENT);
		createEReference(targetElementEClass, TARGET_ELEMENT__STEREO_TYPES);

		stereoTypeEClass = createEClass(STEREO_TYPE);
		createEAttribute(stereoTypeEClass, STEREO_TYPE__NAME);
		createEReference(stereoTypeEClass, STEREO_TYPE__ATTRIBUTES);

		attributeEClass = createEClass(ATTRIBUTE);
		createEAttribute(attributeEClass, ATTRIBUTE__NAME);
		createEAttribute(attributeEClass, ATTRIBUTE__VALUE);

		nestedEntityExpressionEClass = createEClass(NESTED_ENTITY_EXPRESSION);
		createEReference(nestedEntityExpressionEClass, NESTED_ENTITY_EXPRESSION__REF);
		createEReference(nestedEntityExpressionEClass, NESTED_ENTITY_EXPRESSION__TAIL);

		entityExpressionEClass = createEClass(ENTITY_EXPRESSION);
		createEReference(entityExpressionEClass, ENTITY_EXPRESSION__ENTITY);

		// Create enums
		infoModelAttributeEEnum = createEEnum(INFO_MODEL_ATTRIBUTE);
		functionblockModelAttributeEEnum = createEEnum(FUNCTIONBLOCK_MODEL_ATTRIBUTE);
		modelAttributeEEnum = createEEnum(MODEL_ATTRIBUTE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		ModelPackage theModelPackage = (ModelPackage)EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI);
		InformationModelPackage theInformationModelPackage = (InformationModelPackage)EPackage.Registry.INSTANCE.getEPackage(InformationModelPackage.eNS_URI);
		FunctionblockPackage theFunctionblockPackage = (FunctionblockPackage)EPackage.Registry.INSTANCE.getEPackage(FunctionblockPackage.eNS_URI);
		DatatypePackage theDatatypePackage = (DatatypePackage)EPackage.Registry.INSTANCE.getEPackage(DatatypePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		mappingModelEClass.getESuperTypes().add(theModelPackage.getModel());
		infoModelFbElementEClass.getESuperTypes().add(this.getInfoModelChild());
		informationModelPropertyEClass.getESuperTypes().add(this.getInfoModelChild());
		functionBlockElementAttributeEClass.getESuperTypes().add(this.getFunctionBlockElement());
		functionBlockChildElementEClass.getESuperTypes().add(this.getFunctionBlockElement());
		configurationElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		statusElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		faultElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		eventElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		fbTypePropertyEClass.getESuperTypes().add(this.getFBTypeElementChild());
		dataTypePropertyElementEClass.getESuperTypes().add(this.getDataTypeSourceElement());
		entityExpressionRefEClass.getESuperTypes().add(this.getDataTypeSourceElement());
		dataTypeAttributeEClass.getESuperTypes().add(this.getFBTypeElementChild());
		nestedEntityExpressionEClass.getESuperTypes().add(this.getEntityExpressionRef());
		entityExpressionEClass.getESuperTypes().add(this.getEntityExpressionRef());

		// Initialize classes, features, and operations; add parameters
		initEClass(mappingModelEClass, MappingModel.class, "MappingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMappingModel_InfoModelMappingRules(), this.getInfoModelMappingRule(), null, "infoModelMappingRules", null, 0, -1, MappingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingModel_FunctionBlockMappingRules(), this.getFunctionBlockMappingRule(), null, "functionBlockMappingRules", null, 0, -1, MappingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingModel_DataTypeMappingRules(), this.getDataTypeMappingRule(), null, "dataTypeMappingRules", null, 0, -1, MappingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelMappingRuleEClass, InfoModelMappingRule.class, "InfoModelMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfoModelMappingRule_InfoModelSourceElements(), this.getInfoModelSourceElement(), null, "infoModelSourceElements", null, 0, -1, InfoModelMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInfoModelMappingRule_TargetElement(), this.getTargetElement(), null, "targetElement", null, 0, 1, InfoModelMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelSourceElementEClass, InfoModelSourceElement.class, "InfoModelSourceElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfoModelSourceElement_InfoModel(), theInformationModelPackage.getInformationModel(), null, "infoModel", null, 0, 1, InfoModelSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInfoModelSourceElement_InfoModelChild(), this.getInfoModelChild(), null, "infoModelChild", null, 0, 1, InfoModelSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelChildEClass, InfoModelChild.class, "InfoModelChild", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(infoModelFbElementEClass, InfoModelFbElement.class, "InfoModelFbElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfoModelFbElement_Functionblock(), theInformationModelPackage.getFunctionblockProperty(), null, "functionblock", null, 0, 1, InfoModelFbElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInfoModelFbElement_FunctionBlockElement(), this.getFunctionBlockElement(), null, "functionBlockElement", null, 0, 1, InfoModelFbElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(informationModelPropertyEClass, InformationModelProperty.class, "InformationModelProperty", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInformationModelProperty_Attribute(), this.getInfoModelAttribute(), "attribute", null, 0, 1, InformationModelProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockMappingRuleEClass, FunctionBlockMappingRule.class, "FunctionBlockMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFunctionBlockMappingRule_FunctionBlockSourceElements(), this.getFunctionBlockSourceElement(), null, "functionBlockSourceElements", null, 0, -1, FunctionBlockMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFunctionBlockMappingRule_TargetElement(), this.getTargetElement(), null, "targetElement", null, 0, 1, FunctionBlockMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockSourceElementEClass, FunctionBlockSourceElement.class, "FunctionBlockSourceElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFunctionBlockSourceElement_Functionblock(), theFunctionblockPackage.getFunctionblockModel(), null, "functionblock", null, 0, 1, FunctionBlockSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFunctionBlockSourceElement_FunctionBlockElement(), this.getFunctionBlockElement(), null, "functionBlockElement", null, 0, 1, FunctionBlockSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockElementEClass, FunctionBlockElement.class, "FunctionBlockElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(functionBlockElementAttributeEClass, FunctionBlockElementAttribute.class, "FunctionBlockElementAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFunctionBlockElementAttribute_Attribute(), this.getFunctionblockModelAttribute(), "attribute", null, 0, 1, FunctionBlockElementAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockChildElementEClass, FunctionBlockChildElement.class, "FunctionBlockChildElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFunctionBlockChildElement_Type(), this.getOperationElement(), null, "type", null, 0, 1, FunctionBlockChildElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFunctionBlockChildElement_TypeRef(), this.getFBTypeElement(), null, "typeRef", null, 0, 1, FunctionBlockChildElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(operationElementEClass, OperationElement.class, "OperationElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOperationElement_Operation(), theFunctionblockPackage.getOperation(), null, "operation", null, 0, 1, OperationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(configurationElementEClass, ConfigurationElement.class, "ConfigurationElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(statusElementEClass, StatusElement.class, "StatusElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(faultElementEClass, FaultElement.class, "FaultElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(eventElementEClass, EventElement.class, "EventElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEventElement_Event(), theFunctionblockPackage.getEvent(), null, "event", null, 0, 1, EventElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fbTypeElementEClass, FBTypeElement.class, "FBTypeElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFBTypeElement_Property(), theDatatypePackage.getProperty(), null, "property", null, 0, 1, FBTypeElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFBTypeElement_Child(), this.getFBTypeElementChild(), null, "child", null, 0, 1, FBTypeElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fbTypeElementChildEClass, FBTypeElementChild.class, "FBTypeElementChild", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(fbTypePropertyEClass, FBTypeProperty.class, "FBTypeProperty", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFBTypeProperty_Property(), theDatatypePackage.getProperty(), null, "property", null, 0, 1, FBTypeProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataTypeMappingRuleEClass, DataTypeMappingRule.class, "DataTypeMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataTypeMappingRule_DataTypeMappingElements(), this.getDataTypeSourceElement(), null, "dataTypeMappingElements", null, 0, -1, DataTypeMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataTypeMappingRule_DataTypeSourceElement(), this.getDataTypeSourceElement(), null, "DataTypeSourceElement", null, 0, -1, DataTypeMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataTypeMappingRule_TargetElement(), this.getTargetElement(), null, "targetElement", null, 0, 1, DataTypeMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataTypeSourceElementEClass, DataTypeSourceElement.class, "DataTypeSourceElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dataTypePropertyElementEClass, DataTypePropertyElement.class, "DataTypePropertyElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataTypePropertyElement_TypeRef(), theDatatypePackage.getType(), null, "typeRef", null, 0, 1, DataTypePropertyElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataTypePropertyElement_Attribute(), this.getDataTypeAttribute(), null, "attribute", null, 0, 1, DataTypePropertyElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityExpressionRefEClass, EntityExpressionRef.class, "EntityExpressionRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(dataTypeAttributeEClass, DataTypeAttribute.class, "DataTypeAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataTypeAttribute_Attribute(), this.getModelAttribute(), "attribute", null, 0, 1, DataTypeAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(targetElementEClass, TargetElement.class, "TargetElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTargetElement_StereoTypes(), this.getStereoType(), null, "stereoTypes", null, 0, -1, TargetElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stereoTypeEClass, StereoType.class, "StereoType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStereoType_Name(), ecorePackage.getEString(), "name", null, 0, 1, StereoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStereoType_Attributes(), this.getAttribute(), null, "attributes", null, 0, -1, StereoType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(attributeEClass, Attribute.class, "Attribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getAttribute_Name(), ecorePackage.getEString(), "name", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getAttribute_Value(), ecorePackage.getEString(), "value", null, 0, 1, Attribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(nestedEntityExpressionEClass, NestedEntityExpression.class, "NestedEntityExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getNestedEntityExpression_Ref(), this.getEntityExpressionRef(), null, "ref", null, 0, 1, NestedEntityExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getNestedEntityExpression_Tail(), theDatatypePackage.getProperty(), null, "tail", null, 0, 1, NestedEntityExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityExpressionEClass, EntityExpression.class, "EntityExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityExpression_Entity(), theDatatypePackage.getType(), null, "entity", null, 0, 1, EntityExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(infoModelAttributeEEnum, InfoModelAttribute.class, "InfoModelAttribute");
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.NAME);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.NAMESPACE);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.VERSION);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.DISPLAYNAME);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.CATEGORY);
		addEEnumLiteral(infoModelAttributeEEnum, InfoModelAttribute.DESCRIPTION);

		initEEnum(functionblockModelAttributeEEnum, FunctionblockModelAttribute.class, "FunctionblockModelAttribute");
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.NAME);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.NAMESPACE);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.VERSION);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.DISPLAYNAME);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.CATEGORY);
		addEEnumLiteral(functionblockModelAttributeEEnum, FunctionblockModelAttribute.DESCRIPTION);

		initEEnum(modelAttributeEEnum, ModelAttribute.class, "ModelAttribute");
		addEEnumLiteral(modelAttributeEEnum, ModelAttribute.NAME);
		addEEnumLiteral(modelAttributeEEnum, ModelAttribute.NAMESPACE);
		addEEnumLiteral(modelAttributeEEnum, ModelAttribute.DESCRIPTION);

		// Create resource
		createResource(eNS_URI);
	}

} //MappingPackageImpl
