/**
 */
package org.eclipse.vorto.codegen.ble.model.blegatt;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.eclipse.vorto.codegen.ble.model.blegatt.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "blegatt";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/vorto/metamodel/blegatt/BleGatt";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "blegatt";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl <em>Device</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getDevice()
	 * @generated
	 */
	int DEVICE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__NAME = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__NAMESPACE = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__VERSION = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__REFERENCES = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__DESCRIPTION = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Displayname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__DISPLAYNAME = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__DISPLAYNAME;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__CATEGORY = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__CATEGORY;

	/**
	 * The feature id for the '<em><b>Device Info</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__DEVICE_INFO = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Services</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__SERVICES = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Infomodel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE__INFOMODEL = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Device</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE_FEATURE_COUNT = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Device</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE_OPERATION_COUNT = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceInfoImpl <em>Device Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceInfoImpl
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getDeviceInfo()
	 * @generated
	 */
	int DEVICE_INFO = 1;

	/**
	 * The feature id for the '<em><b>Model Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE_INFO__MODEL_NUMBER = 0;

	/**
	 * The number of structural features of the '<em>Device Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE_INFO_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Device Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEVICE_INFO_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.ServiceImpl <em>Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ServiceImpl
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getService()
	 * @generated
	 */
	int SERVICE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__NAME = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__NAME;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__NAMESPACE = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__NAMESPACE;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__VERSION = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__VERSION;

	/**
	 * The feature id for the '<em><b>References</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__REFERENCES = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__REFERENCES;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__DESCRIPTION = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Displayname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__DISPLAYNAME = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__DISPLAYNAME;

	/**
	 * The feature id for the '<em><b>Category</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__CATEGORY = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL__CATEGORY;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__UUID = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Functionblocks</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__FUNCTIONBLOCKS = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Characteristics</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE__CHARACTERISTICS = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_FEATURE_COUNT = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERVICE_OPERATION_COUNT = org.eclipse.vorto.core.api.model.model.ModelPackage.MODEL_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl <em>Characteristic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getCharacteristic()
	 * @generated
	 */
	int CHARACTERISTIC = 3;

	/**
	 * The feature id for the '<em><b>Uuid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC__UUID = 0;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC__PROPERTIES = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC__NAME = 2;

	/**
	 * The feature id for the '<em><b>Is Readable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC__IS_READABLE = 3;

	/**
	 * The feature id for the '<em><b>Is Writable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC__IS_WRITABLE = 4;

	/**
	 * The feature id for the '<em><b>Is Eventable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC__IS_EVENTABLE = 5;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC__LENGTH = 6;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC__VALUE = 7;

	/**
	 * The number of structural features of the '<em>Characteristic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Characteristic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicPropertyImpl <em>Characteristic Property</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicPropertyImpl
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getCharacteristicProperty()
	 * @generated
	 */
	int CHARACTERISTIC_PROPERTY = 4;

	/**
	 * The feature id for the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC_PROPERTY__OFFSET = 0;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC_PROPERTY__LENGTH = 1;

	/**
	 * The feature id for the '<em><b>Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC_PROPERTY__PROPERTY = 2;

	/**
	 * The feature id for the '<em><b>Datatype</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC_PROPERTY__DATATYPE = 3;

	/**
	 * The number of structural features of the '<em>Characteristic Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC_PROPERTY_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Characteristic Property</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CHARACTERISTIC_PROPERTY_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device <em>Device</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Device</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Device
	 * @generated
	 */
	EClass getDevice();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device#getDeviceInfo <em>Device Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Device Info</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Device#getDeviceInfo()
	 * @see #getDevice()
	 * @generated
	 */
	EReference getDevice_DeviceInfo();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device#getServices <em>Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Services</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Device#getServices()
	 * @see #getDevice()
	 * @generated
	 */
	EReference getDevice_Services();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Device#getInfomodel <em>Infomodel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Infomodel</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Device#getInfomodel()
	 * @see #getDevice()
	 * @generated
	 */
	EReference getDevice_Infomodel();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.codegen.ble.model.blegatt.DeviceInfo <em>Device Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Device Info</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.DeviceInfo
	 * @generated
	 */
	EClass getDeviceInfo();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.DeviceInfo#getModelNumber <em>Model Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Model Number</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.DeviceInfo#getModelNumber()
	 * @see #getDeviceInfo()
	 * @generated
	 */
	EAttribute getDeviceInfo_ModelNumber();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Service <em>Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Service</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Service
	 * @generated
	 */
	EClass getService();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Service#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Service#getUuid()
	 * @see #getService()
	 * @generated
	 */
	EAttribute getService_Uuid();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Service#getFunctionblocks <em>Functionblocks</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Functionblocks</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Service#getFunctionblocks()
	 * @see #getService()
	 * @generated
	 */
	EReference getService_Functionblocks();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Service#getCharacteristics <em>Characteristics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Characteristics</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Service#getCharacteristics()
	 * @see #getService()
	 * @generated
	 */
	EReference getService_Characteristics();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic <em>Characteristic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Characteristic</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic
	 * @generated
	 */
	EClass getCharacteristic();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getUuid <em>Uuid</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uuid</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getUuid()
	 * @see #getCharacteristic()
	 * @generated
	 */
	EAttribute getCharacteristic_Uuid();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Properties</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getProperties()
	 * @see #getCharacteristic()
	 * @generated
	 */
	EReference getCharacteristic_Properties();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getName()
	 * @see #getCharacteristic()
	 * @generated
	 */
	EAttribute getCharacteristic_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsReadable <em>Is Readable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Readable</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsReadable()
	 * @see #getCharacteristic()
	 * @generated
	 */
	EAttribute getCharacteristic_IsReadable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsWritable <em>Is Writable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Writable</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsWritable()
	 * @see #getCharacteristic()
	 * @generated
	 */
	EAttribute getCharacteristic_IsWritable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsEventable <em>Is Eventable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Eventable</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#isIsEventable()
	 * @see #getCharacteristic()
	 * @generated
	 */
	EAttribute getCharacteristic_IsEventable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getLength()
	 * @see #getCharacteristic()
	 * @generated
	 */
	EAttribute getCharacteristic_Length();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.Characteristic#getValue()
	 * @see #getCharacteristic()
	 * @generated
	 */
	EAttribute getCharacteristic_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty <em>Characteristic Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Characteristic Property</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty
	 * @generated
	 */
	EClass getCharacteristicProperty();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getOffset <em>Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getOffset()
	 * @see #getCharacteristicProperty()
	 * @generated
	 */
	EAttribute getCharacteristicProperty_Offset();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getLength()
	 * @see #getCharacteristicProperty()
	 * @generated
	 */
	EAttribute getCharacteristicProperty_Length();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Property</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getProperty()
	 * @see #getCharacteristicProperty()
	 * @generated
	 */
	EReference getCharacteristicProperty_Property();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getDatatype <em>Datatype</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Datatype</em>'.
	 * @see org.eclipse.vorto.codegen.ble.model.blegatt.CharacteristicProperty#getDatatype()
	 * @see #getCharacteristicProperty()
	 * @generated
	 */
	EAttribute getCharacteristicProperty_Datatype();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl <em>Device</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceImpl
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getDevice()
		 * @generated
		 */
		EClass DEVICE = eINSTANCE.getDevice();

		/**
		 * The meta object literal for the '<em><b>Device Info</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEVICE__DEVICE_INFO = eINSTANCE.getDevice_DeviceInfo();

		/**
		 * The meta object literal for the '<em><b>Services</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEVICE__SERVICES = eINSTANCE.getDevice_Services();

		/**
		 * The meta object literal for the '<em><b>Infomodel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEVICE__INFOMODEL = eINSTANCE.getDevice_Infomodel();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceInfoImpl <em>Device Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.DeviceInfoImpl
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getDeviceInfo()
		 * @generated
		 */
		EClass DEVICE_INFO = eINSTANCE.getDeviceInfo();

		/**
		 * The meta object literal for the '<em><b>Model Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEVICE_INFO__MODEL_NUMBER = eINSTANCE.getDeviceInfo_ModelNumber();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.ServiceImpl <em>Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ServiceImpl
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getService()
		 * @generated
		 */
		EClass SERVICE = eINSTANCE.getService();

		/**
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SERVICE__UUID = eINSTANCE.getService_Uuid();

		/**
		 * The meta object literal for the '<em><b>Functionblocks</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE__FUNCTIONBLOCKS = eINSTANCE.getService_Functionblocks();

		/**
		 * The meta object literal for the '<em><b>Characteristics</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SERVICE__CHARACTERISTICS = eINSTANCE.getService_Characteristics();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl <em>Characteristic</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicImpl
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getCharacteristic()
		 * @generated
		 */
		EClass CHARACTERISTIC = eINSTANCE.getCharacteristic();

		/**
		 * The meta object literal for the '<em><b>Uuid</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC__UUID = eINSTANCE.getCharacteristic_Uuid();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARACTERISTIC__PROPERTIES = eINSTANCE.getCharacteristic_Properties();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC__NAME = eINSTANCE.getCharacteristic_Name();

		/**
		 * The meta object literal for the '<em><b>Is Readable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC__IS_READABLE = eINSTANCE.getCharacteristic_IsReadable();

		/**
		 * The meta object literal for the '<em><b>Is Writable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC__IS_WRITABLE = eINSTANCE.getCharacteristic_IsWritable();

		/**
		 * The meta object literal for the '<em><b>Is Eventable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC__IS_EVENTABLE = eINSTANCE.getCharacteristic_IsEventable();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC__LENGTH = eINSTANCE.getCharacteristic_Length();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC__VALUE = eINSTANCE.getCharacteristic_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicPropertyImpl <em>Characteristic Property</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.CharacteristicPropertyImpl
		 * @see org.eclipse.vorto.codegen.ble.model.blegatt.impl.ModelPackageImpl#getCharacteristicProperty()
		 * @generated
		 */
		EClass CHARACTERISTIC_PROPERTY = eINSTANCE.getCharacteristicProperty();

		/**
		 * The meta object literal for the '<em><b>Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC_PROPERTY__OFFSET = eINSTANCE.getCharacteristicProperty_Offset();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC_PROPERTY__LENGTH = eINSTANCE.getCharacteristicProperty_Length();

		/**
		 * The meta object literal for the '<em><b>Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CHARACTERISTIC_PROPERTY__PROPERTY = eINSTANCE.getCharacteristicProperty_Property();

		/**
		 * The meta object literal for the '<em><b>Datatype</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CHARACTERISTIC_PROPERTY__DATATYPE = eINSTANCE.getCharacteristicProperty_Datatype();

	}

} //ModelPackage
