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
import org.eclipse.vorto.core.api.model.mapping.DataTypeMapping;
import org.eclipse.vorto.core.api.model.mapping.DataTypeReference;
import org.eclipse.vorto.core.api.model.mapping.EntityAttributeElement;
import org.eclipse.vorto.core.api.model.mapping.EntityExpression;
import org.eclipse.vorto.core.api.model.mapping.EntityExpressionRef;
import org.eclipse.vorto.core.api.model.mapping.EntityMapping;
import org.eclipse.vorto.core.api.model.mapping.EntityMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EntitySourceElement;
import org.eclipse.vorto.core.api.model.mapping.EntityTargetElement;
import org.eclipse.vorto.core.api.model.mapping.EnumAttributeElement;
import org.eclipse.vorto.core.api.model.mapping.EnumExpression;
import org.eclipse.vorto.core.api.model.mapping.EnumMapping;
import org.eclipse.vorto.core.api.model.mapping.EnumMappingRule;
import org.eclipse.vorto.core.api.model.mapping.EnumReference;
import org.eclipse.vorto.core.api.model.mapping.EnumSourceElement;
import org.eclipse.vorto.core.api.model.mapping.EnumTargetElement;
import org.eclipse.vorto.core.api.model.mapping.EventElement;
import org.eclipse.vorto.core.api.model.mapping.FBTypeElement;
import org.eclipse.vorto.core.api.model.mapping.FaultElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockChildElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockElementAttribute;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMapping;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockMappingRule;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockReference;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockSourceElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionBlockTargetElement;
import org.eclipse.vorto.core.api.model.mapping.FunctionblockModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.InfoModelChild;
import org.eclipse.vorto.core.api.model.mapping.InfoModelFbElement;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMapping;
import org.eclipse.vorto.core.api.model.mapping.InfoModelMappingRule;
import org.eclipse.vorto.core.api.model.mapping.InfoModelSourceElement;
import org.eclipse.vorto.core.api.model.mapping.InfoModelTargetElement;
import org.eclipse.vorto.core.api.model.mapping.InformationModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.Mapping;
import org.eclipse.vorto.core.api.model.mapping.MappingFactory;
import org.eclipse.vorto.core.api.model.mapping.MappingModel;
import org.eclipse.vorto.core.api.model.mapping.MappingPackage;
import org.eclipse.vorto.core.api.model.mapping.ModelAttribute;
import org.eclipse.vorto.core.api.model.mapping.NestedEntityExpression;
import org.eclipse.vorto.core.api.model.mapping.OperationElement;
import org.eclipse.vorto.core.api.model.mapping.StatusElement;
import org.eclipse.vorto.core.api.model.mapping.StereoType;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeElement;
import org.eclipse.vorto.core.api.model.mapping.StereoTypeReference;

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
	private EClass mappingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass infoModelMappingEClass = null;

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
	private EClass infoModelTargetElementEClass = null;

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
	private EClass informationModelAttributeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockMappingEClass = null;

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
	private EClass functionBlockTargetElementEClass = null;

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
	private EClass entityMappingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityTargetElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entitySourceElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entityAttributeElementEClass = null;

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
	private EClass enumMappingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumMappingRuleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumTargetElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumSourceElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumAttributeElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumExpressionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass enumReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass functionBlockReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTypeReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTypeMappingEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stereoTypeReferenceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stereoTypeElementEClass = null;

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
	public EReference getMappingModel_Mapping() {
		return (EReference)mappingModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getMapping() {
		return mappingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getMapping_Name() {
		return (EAttribute)mappingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelMapping() {
		return infoModelMappingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelMapping_InfoModelMappingRules() {
		return (EReference)infoModelMappingEClass.getEStructuralFeatures().get(0);
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
	public EReference getInfoModelMappingRule_SourceElements() {
		return (EReference)infoModelMappingRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelMappingRule_InfoModelSourceElements() {
		return (EReference)infoModelMappingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getInfoModelMappingRule_Target() {
		return (EReference)infoModelMappingRuleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getInfoModelTargetElement() {
		return infoModelTargetElementEClass;
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
	public EClass getInformationModelAttribute() {
		return informationModelAttributeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getInformationModelAttribute_Attribute() {
		return (EAttribute)informationModelAttributeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockMapping() {
		return functionBlockMappingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockMapping_FunctionBlockMappingRules() {
		return (EReference)functionBlockMappingEClass.getEStructuralFeatures().get(0);
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
	public EReference getFunctionBlockMappingRule_Target() {
		return (EReference)functionBlockMappingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockTargetElement() {
		return functionBlockTargetElementEClass;
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
	public EReference getConfigurationElement_TypeRef() {
		return (EReference)configurationElementEClass.getEStructuralFeatures().get(0);
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
	public EReference getStatusElement_TypeRef() {
		return (EReference)statusElementEClass.getEStructuralFeatures().get(0);
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
	public EReference getFaultElement_TypeRef() {
		return (EReference)faultElementEClass.getEStructuralFeatures().get(0);
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
	public EReference getEventElement_TypeRef() {
		return (EReference)eventElementEClass.getEStructuralFeatures().get(1);
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
	public EClass getEntityMapping() {
		return entityMappingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityMapping_EntityMappingRules() {
		return (EReference)entityMappingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityMappingRule() {
		return entityMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityMappingRule_EntityMappingElements() {
		return (EReference)entityMappingRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityMappingRule_EntitySourceElement() {
		return (EReference)entityMappingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityMappingRule_Target() {
		return (EReference)entityMappingRuleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityTargetElement() {
		return entityTargetElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntitySourceElement() {
		return entitySourceElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEntityAttributeElement() {
		return entityAttributeElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEntityAttributeElement_TypeRef() {
		return (EReference)entityAttributeElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEntityAttributeElement_Attribute() {
		return (EAttribute)entityAttributeElementEClass.getEStructuralFeatures().get(1);
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
	public EClass getEnumMapping() {
		return enumMappingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumMapping_EnumMappingRules() {
		return (EReference)enumMappingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumMappingRule() {
		return enumMappingRuleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumMappingRule_EnumElements() {
		return (EReference)enumMappingRuleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumMappingRule_EnumSourceElement() {
		return (EReference)enumMappingRuleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumMappingRule_Target() {
		return (EReference)enumMappingRuleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumTargetElement() {
		return enumTargetElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumSourceElement() {
		return enumSourceElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumSourceElement_TypeRef() {
		return (EReference)enumSourceElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumAttributeElement() {
		return enumAttributeElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getEnumAttributeElement_Attribute() {
		return (EAttribute)enumAttributeElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumExpression() {
		return enumExpressionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumExpression_Literal() {
		return (EReference)enumExpressionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getEnumReference() {
		return enumReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getEnumReference_Reference() {
		return (EReference)enumReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFunctionBlockReference() {
		return functionBlockReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getFunctionBlockReference_Reference() {
		return (EReference)functionBlockReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataTypeReference() {
		return dataTypeReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getDataTypeReference_Reference() {
		return (EReference)dataTypeReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDataTypeMapping() {
		return dataTypeMappingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStereoTypeReference() {
		return stereoTypeReferenceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStereoTypeReference_TargetElement() {
		return (EReference)stereoTypeReferenceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStereoTypeElement() {
		return stereoTypeElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getStereoTypeElement_StereoTypes() {
		return (EReference)stereoTypeElementEClass.getEStructuralFeatures().get(0);
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
		createEReference(mappingModelEClass, MAPPING_MODEL__MAPPING);

		mappingEClass = createEClass(MAPPING);
		createEAttribute(mappingEClass, MAPPING__NAME);

		infoModelMappingEClass = createEClass(INFO_MODEL_MAPPING);
		createEReference(infoModelMappingEClass, INFO_MODEL_MAPPING__INFO_MODEL_MAPPING_RULES);

		infoModelMappingRuleEClass = createEClass(INFO_MODEL_MAPPING_RULE);
		createEReference(infoModelMappingRuleEClass, INFO_MODEL_MAPPING_RULE__SOURCE_ELEMENTS);
		createEReference(infoModelMappingRuleEClass, INFO_MODEL_MAPPING_RULE__INFO_MODEL_SOURCE_ELEMENTS);
		createEReference(infoModelMappingRuleEClass, INFO_MODEL_MAPPING_RULE__TARGET);

		infoModelTargetElementEClass = createEClass(INFO_MODEL_TARGET_ELEMENT);

		infoModelSourceElementEClass = createEClass(INFO_MODEL_SOURCE_ELEMENT);
		createEReference(infoModelSourceElementEClass, INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL);
		createEReference(infoModelSourceElementEClass, INFO_MODEL_SOURCE_ELEMENT__INFO_MODEL_CHILD);

		infoModelChildEClass = createEClass(INFO_MODEL_CHILD);

		infoModelFbElementEClass = createEClass(INFO_MODEL_FB_ELEMENT);
		createEReference(infoModelFbElementEClass, INFO_MODEL_FB_ELEMENT__FUNCTIONBLOCK);

		informationModelAttributeEClass = createEClass(INFORMATION_MODEL_ATTRIBUTE);
		createEAttribute(informationModelAttributeEClass, INFORMATION_MODEL_ATTRIBUTE__ATTRIBUTE);

		functionBlockMappingEClass = createEClass(FUNCTION_BLOCK_MAPPING);
		createEReference(functionBlockMappingEClass, FUNCTION_BLOCK_MAPPING__FUNCTION_BLOCK_MAPPING_RULES);

		functionBlockMappingRuleEClass = createEClass(FUNCTION_BLOCK_MAPPING_RULE);
		createEReference(functionBlockMappingRuleEClass, FUNCTION_BLOCK_MAPPING_RULE__FUNCTION_BLOCK_SOURCE_ELEMENTS);
		createEReference(functionBlockMappingRuleEClass, FUNCTION_BLOCK_MAPPING_RULE__TARGET);

		functionBlockTargetElementEClass = createEClass(FUNCTION_BLOCK_TARGET_ELEMENT);

		functionBlockSourceElementEClass = createEClass(FUNCTION_BLOCK_SOURCE_ELEMENT);
		createEReference(functionBlockSourceElementEClass, FUNCTION_BLOCK_SOURCE_ELEMENT__FUNCTIONBLOCK);
		createEReference(functionBlockSourceElementEClass, FUNCTION_BLOCK_SOURCE_ELEMENT__FUNCTION_BLOCK_ELEMENT);

		functionBlockElementEClass = createEClass(FUNCTION_BLOCK_ELEMENT);

		functionBlockElementAttributeEClass = createEClass(FUNCTION_BLOCK_ELEMENT_ATTRIBUTE);
		createEAttribute(functionBlockElementAttributeEClass, FUNCTION_BLOCK_ELEMENT_ATTRIBUTE__ATTRIBUTE);

		functionBlockChildElementEClass = createEClass(FUNCTION_BLOCK_CHILD_ELEMENT);

		operationElementEClass = createEClass(OPERATION_ELEMENT);
		createEReference(operationElementEClass, OPERATION_ELEMENT__OPERATION);

		configurationElementEClass = createEClass(CONFIGURATION_ELEMENT);
		createEReference(configurationElementEClass, CONFIGURATION_ELEMENT__TYPE_REF);

		statusElementEClass = createEClass(STATUS_ELEMENT);
		createEReference(statusElementEClass, STATUS_ELEMENT__TYPE_REF);

		faultElementEClass = createEClass(FAULT_ELEMENT);
		createEReference(faultElementEClass, FAULT_ELEMENT__TYPE_REF);

		eventElementEClass = createEClass(EVENT_ELEMENT);
		createEReference(eventElementEClass, EVENT_ELEMENT__EVENT);
		createEReference(eventElementEClass, EVENT_ELEMENT__TYPE_REF);

		fbTypeElementEClass = createEClass(FB_TYPE_ELEMENT);
		createEReference(fbTypeElementEClass, FB_TYPE_ELEMENT__PROPERTY);

		entityMappingEClass = createEClass(ENTITY_MAPPING);
		createEReference(entityMappingEClass, ENTITY_MAPPING__ENTITY_MAPPING_RULES);

		entityMappingRuleEClass = createEClass(ENTITY_MAPPING_RULE);
		createEReference(entityMappingRuleEClass, ENTITY_MAPPING_RULE__ENTITY_MAPPING_ELEMENTS);
		createEReference(entityMappingRuleEClass, ENTITY_MAPPING_RULE__ENTITY_SOURCE_ELEMENT);
		createEReference(entityMappingRuleEClass, ENTITY_MAPPING_RULE__TARGET);

		entityTargetElementEClass = createEClass(ENTITY_TARGET_ELEMENT);

		entitySourceElementEClass = createEClass(ENTITY_SOURCE_ELEMENT);

		entityAttributeElementEClass = createEClass(ENTITY_ATTRIBUTE_ELEMENT);
		createEReference(entityAttributeElementEClass, ENTITY_ATTRIBUTE_ELEMENT__TYPE_REF);
		createEAttribute(entityAttributeElementEClass, ENTITY_ATTRIBUTE_ELEMENT__ATTRIBUTE);

		entityExpressionRefEClass = createEClass(ENTITY_EXPRESSION_REF);

		enumMappingEClass = createEClass(ENUM_MAPPING);
		createEReference(enumMappingEClass, ENUM_MAPPING__ENUM_MAPPING_RULES);

		enumMappingRuleEClass = createEClass(ENUM_MAPPING_RULE);
		createEReference(enumMappingRuleEClass, ENUM_MAPPING_RULE__ENUM_ELEMENTS);
		createEReference(enumMappingRuleEClass, ENUM_MAPPING_RULE__ENUM_SOURCE_ELEMENT);
		createEReference(enumMappingRuleEClass, ENUM_MAPPING_RULE__TARGET);

		enumTargetElementEClass = createEClass(ENUM_TARGET_ELEMENT);

		enumSourceElementEClass = createEClass(ENUM_SOURCE_ELEMENT);
		createEReference(enumSourceElementEClass, ENUM_SOURCE_ELEMENT__TYPE_REF);

		enumAttributeElementEClass = createEClass(ENUM_ATTRIBUTE_ELEMENT);
		createEAttribute(enumAttributeElementEClass, ENUM_ATTRIBUTE_ELEMENT__ATTRIBUTE);

		enumExpressionEClass = createEClass(ENUM_EXPRESSION);
		createEReference(enumExpressionEClass, ENUM_EXPRESSION__LITERAL);

		enumReferenceEClass = createEClass(ENUM_REFERENCE);
		createEReference(enumReferenceEClass, ENUM_REFERENCE__REFERENCE);

		functionBlockReferenceEClass = createEClass(FUNCTION_BLOCK_REFERENCE);
		createEReference(functionBlockReferenceEClass, FUNCTION_BLOCK_REFERENCE__REFERENCE);

		dataTypeReferenceEClass = createEClass(DATA_TYPE_REFERENCE);
		createEReference(dataTypeReferenceEClass, DATA_TYPE_REFERENCE__REFERENCE);

		dataTypeMappingEClass = createEClass(DATA_TYPE_MAPPING);

		stereoTypeReferenceEClass = createEClass(STEREO_TYPE_REFERENCE);
		createEReference(stereoTypeReferenceEClass, STEREO_TYPE_REFERENCE__TARGET_ELEMENT);

		stereoTypeElementEClass = createEClass(STEREO_TYPE_ELEMENT);
		createEReference(stereoTypeElementEClass, STEREO_TYPE_ELEMENT__STEREO_TYPES);

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
		infoModelMappingEClass.getESuperTypes().add(this.getMapping());
		infoModelFbElementEClass.getESuperTypes().add(this.getInfoModelChild());
		informationModelAttributeEClass.getESuperTypes().add(this.getInfoModelChild());
		functionBlockMappingEClass.getESuperTypes().add(this.getMapping());
		functionBlockElementAttributeEClass.getESuperTypes().add(this.getFunctionBlockElement());
		functionBlockChildElementEClass.getESuperTypes().add(this.getFunctionBlockElement());
		operationElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		configurationElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		statusElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		faultElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		eventElementEClass.getESuperTypes().add(this.getFunctionBlockChildElement());
		entityMappingEClass.getESuperTypes().add(this.getDataTypeMapping());
		entityAttributeElementEClass.getESuperTypes().add(this.getEntitySourceElement());
		entityExpressionRefEClass.getESuperTypes().add(this.getEntitySourceElement());
		enumMappingEClass.getESuperTypes().add(this.getDataTypeMapping());
		enumAttributeElementEClass.getESuperTypes().add(this.getEnumSourceElement());
		enumExpressionEClass.getESuperTypes().add(this.getEnumSourceElement());
		enumReferenceEClass.getESuperTypes().add(this.getEnumTargetElement());
		functionBlockReferenceEClass.getESuperTypes().add(this.getInfoModelTargetElement());
		dataTypeReferenceEClass.getESuperTypes().add(this.getFunctionBlockTargetElement());
		dataTypeReferenceEClass.getESuperTypes().add(this.getEntityTargetElement());
		dataTypeMappingEClass.getESuperTypes().add(this.getMapping());
		stereoTypeReferenceEClass.getESuperTypes().add(this.getInfoModelTargetElement());
		stereoTypeReferenceEClass.getESuperTypes().add(this.getFunctionBlockTargetElement());
		stereoTypeReferenceEClass.getESuperTypes().add(this.getEntityTargetElement());
		stereoTypeReferenceEClass.getESuperTypes().add(this.getEnumTargetElement());
		nestedEntityExpressionEClass.getESuperTypes().add(this.getEntityExpressionRef());
		entityExpressionEClass.getESuperTypes().add(this.getEntityExpressionRef());

		// Initialize classes, features, and operations; add parameters
		initEClass(mappingModelEClass, MappingModel.class, "MappingModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMappingModel_Mapping(), this.getMapping(), null, "mapping", null, 0, 1, MappingModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mappingEClass, Mapping.class, "Mapping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMapping_Name(), ecorePackage.getEString(), "name", null, 0, 1, Mapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelMappingEClass, InfoModelMapping.class, "InfoModelMapping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfoModelMapping_InfoModelMappingRules(), this.getInfoModelMappingRule(), null, "infoModelMappingRules", null, 0, -1, InfoModelMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelMappingRuleEClass, InfoModelMappingRule.class, "InfoModelMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfoModelMappingRule_SourceElements(), this.getInfoModelSourceElement(), null, "sourceElements", null, 0, -1, InfoModelMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInfoModelMappingRule_InfoModelSourceElements(), this.getInfoModelSourceElement(), null, "infoModelSourceElements", null, 0, -1, InfoModelMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInfoModelMappingRule_Target(), this.getInfoModelTargetElement(), null, "target", null, 0, 1, InfoModelMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelTargetElementEClass, InfoModelTargetElement.class, "InfoModelTargetElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(infoModelSourceElementEClass, InfoModelSourceElement.class, "InfoModelSourceElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfoModelSourceElement_InfoModel(), theInformationModelPackage.getInformationModel(), null, "infoModel", null, 0, 1, InfoModelSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getInfoModelSourceElement_InfoModelChild(), this.getInfoModelChild(), null, "infoModelChild", null, 0, 1, InfoModelSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(infoModelChildEClass, InfoModelChild.class, "InfoModelChild", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(infoModelFbElementEClass, InfoModelFbElement.class, "InfoModelFbElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getInfoModelFbElement_Functionblock(), theInformationModelPackage.getFunctionblockProperty(), null, "functionblock", null, 0, 1, InfoModelFbElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(informationModelAttributeEClass, InformationModelAttribute.class, "InformationModelAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getInformationModelAttribute_Attribute(), this.getInfoModelAttribute(), "attribute", null, 0, 1, InformationModelAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockMappingEClass, FunctionBlockMapping.class, "FunctionBlockMapping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFunctionBlockMapping_FunctionBlockMappingRules(), this.getFunctionBlockMappingRule(), null, "functionBlockMappingRules", null, 0, -1, FunctionBlockMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockMappingRuleEClass, FunctionBlockMappingRule.class, "FunctionBlockMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFunctionBlockMappingRule_FunctionBlockSourceElements(), this.getFunctionBlockSourceElement(), null, "functionBlockSourceElements", null, 0, -1, FunctionBlockMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFunctionBlockMappingRule_Target(), this.getFunctionBlockTargetElement(), null, "target", null, 0, 1, FunctionBlockMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockTargetElementEClass, FunctionBlockTargetElement.class, "FunctionBlockTargetElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(functionBlockSourceElementEClass, FunctionBlockSourceElement.class, "FunctionBlockSourceElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFunctionBlockSourceElement_Functionblock(), theFunctionblockPackage.getFunctionblockModel(), null, "functionblock", null, 0, 1, FunctionBlockSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getFunctionBlockSourceElement_FunctionBlockElement(), this.getFunctionBlockElement(), null, "functionBlockElement", null, 0, 1, FunctionBlockSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockElementEClass, FunctionBlockElement.class, "FunctionBlockElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(functionBlockElementAttributeEClass, FunctionBlockElementAttribute.class, "FunctionBlockElementAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFunctionBlockElementAttribute_Attribute(), this.getFunctionblockModelAttribute(), "attribute", null, 0, 1, FunctionBlockElementAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockChildElementEClass, FunctionBlockChildElement.class, "FunctionBlockChildElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(operationElementEClass, OperationElement.class, "OperationElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOperationElement_Operation(), theFunctionblockPackage.getOperation(), null, "operation", null, 0, 1, OperationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(configurationElementEClass, ConfigurationElement.class, "ConfigurationElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getConfigurationElement_TypeRef(), this.getFBTypeElement(), null, "typeRef", null, 0, 1, ConfigurationElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statusElementEClass, StatusElement.class, "StatusElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStatusElement_TypeRef(), this.getFBTypeElement(), null, "typeRef", null, 0, 1, StatusElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(faultElementEClass, FaultElement.class, "FaultElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFaultElement_TypeRef(), this.getFBTypeElement(), null, "typeRef", null, 0, 1, FaultElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eventElementEClass, EventElement.class, "EventElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEventElement_Event(), theFunctionblockPackage.getEvent(), null, "event", null, 0, 1, EventElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEventElement_TypeRef(), this.getFBTypeElement(), null, "typeRef", null, 0, 1, EventElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(fbTypeElementEClass, FBTypeElement.class, "FBTypeElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFBTypeElement_Property(), theDatatypePackage.getProperty(), null, "property", null, 0, 1, FBTypeElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityMappingEClass, EntityMapping.class, "EntityMapping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityMapping_EntityMappingRules(), this.getEntityMappingRule(), null, "entityMappingRules", null, 0, -1, EntityMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityMappingRuleEClass, EntityMappingRule.class, "EntityMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityMappingRule_EntityMappingElements(), this.getEntitySourceElement(), null, "entityMappingElements", null, 0, -1, EntityMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingRule_EntitySourceElement(), this.getEntitySourceElement(), null, "EntitySourceElement", null, 0, -1, EntityMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEntityMappingRule_Target(), this.getEntityTargetElement(), null, "target", null, 0, 1, EntityMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityTargetElementEClass, EntityTargetElement.class, "EntityTargetElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(entitySourceElementEClass, EntitySourceElement.class, "EntitySourceElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(entityAttributeElementEClass, EntityAttributeElement.class, "EntityAttributeElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntityAttributeElement_TypeRef(), theDatatypePackage.getEntity(), null, "typeRef", null, 0, 1, EntityAttributeElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEntityAttributeElement_Attribute(), this.getModelAttribute(), "attribute", null, 0, 1, EntityAttributeElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(entityExpressionRefEClass, EntityExpressionRef.class, "EntityExpressionRef", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(enumMappingEClass, EnumMapping.class, "EnumMapping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnumMapping_EnumMappingRules(), this.getEnumMappingRule(), null, "enumMappingRules", null, 0, -1, EnumMapping.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumMappingRuleEClass, EnumMappingRule.class, "EnumMappingRule", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnumMappingRule_EnumElements(), this.getEnumSourceElement(), null, "enumElements", null, 0, -1, EnumMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnumMappingRule_EnumSourceElement(), this.getEnumSourceElement(), null, "EnumSourceElement", null, 0, -1, EnumMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEnumMappingRule_Target(), this.getEnumTargetElement(), null, "target", null, 0, 1, EnumMappingRule.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumTargetElementEClass, EnumTargetElement.class, "EnumTargetElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(enumSourceElementEClass, EnumSourceElement.class, "EnumSourceElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnumSourceElement_TypeRef(), theDatatypePackage.getEnum(), null, "typeRef", null, 0, 1, EnumSourceElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumAttributeElementEClass, EnumAttributeElement.class, "EnumAttributeElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEnumAttributeElement_Attribute(), this.getModelAttribute(), "attribute", null, 0, 1, EnumAttributeElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumExpressionEClass, EnumExpression.class, "EnumExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnumExpression_Literal(), theDatatypePackage.getEnumLiteral(), null, "literal", null, 0, 1, EnumExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(enumReferenceEClass, EnumReference.class, "EnumReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEnumReference_Reference(), this.getEnumMapping(), null, "reference", null, 0, 1, EnumReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(functionBlockReferenceEClass, FunctionBlockReference.class, "FunctionBlockReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getFunctionBlockReference_Reference(), this.getFunctionBlockMapping(), null, "reference", null, 0, 1, FunctionBlockReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataTypeReferenceEClass, DataTypeReference.class, "DataTypeReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataTypeReference_Reference(), this.getDataTypeMapping(), null, "reference", null, 0, 1, DataTypeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataTypeMappingEClass, DataTypeMapping.class, "DataTypeMapping", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(stereoTypeReferenceEClass, StereoTypeReference.class, "StereoTypeReference", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStereoTypeReference_TargetElement(), this.getStereoTypeElement(), null, "targetElement", null, 0, 1, StereoTypeReference.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stereoTypeElementEClass, StereoTypeElement.class, "StereoTypeElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStereoTypeElement_StereoTypes(), this.getStereoType(), null, "stereoTypes", null, 0, -1, StereoTypeElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		initEReference(getEntityExpression_Entity(), theDatatypePackage.getEntity(), null, "entity", null, 0, 1, EntityExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
