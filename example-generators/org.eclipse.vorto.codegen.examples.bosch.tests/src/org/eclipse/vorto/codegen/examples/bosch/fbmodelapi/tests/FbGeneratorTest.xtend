/*******************************************************************************
 * Copyright (c) 2014 Bosch Software Innovations GmbH and others.
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
 *
 *******************************************************************************/
package org.eclipse.vorto.codegen.examples.bosch.fbmodelapi.tests;

import com.google.inject.Inject
import com.google.inject.Provider
import org.eclipse.emf.common.util.URI
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl
import org.eclipse.vorto.editor.functionblock.FunctionblockInjectorProvider
import org.eclipse.vorto.editor.functionblock.FunctionblockRuntimeModule
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.InMemoryFileSystemAccess
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.resource.XtextResourceSet
import org.eclipse.xtext.util.StringInputStream
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(FunctionblockInjectorProvider))
class CXFGeneratorTest extends FbAbstractGeneratorTest {

	IGenerator underTest = new GeneratorAdapter();
	@Inject ParseHelper<FunctionblockModel> parseHelper

	@Inject Provider<XtextResourceSet> resourceSetProvider

	@BeforeClass
	def static void initializeModel() {
		FunctionblockPackageImpl.init();
	}

	@Test
	def void testGeneratedFilesFromDSL() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				description 'comment'
				category demo					
				functionblock Fridge {				
					configuration {
						mandatory temperature as temperature
					}
				}        		
			     ''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)
		assertEquals(4, fsa.allFiles.size)  
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd"))
		assertTrue(fsa.allFiles.containsKey(IFileSystemAccess::DEFAULT_OUTPUT + "jaxb-bindings.xjb"))

		with(new FunctionblockRuntimeModule())
		println(getSerializer().serialize(model))
	}

	@Test
	def void testMinimalFunctionBlock() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				description 'comment'
				category demo						
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:annotation>
			   			<xs:documentation>comment</xs:documentation>
			   		</xs:annotation>
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "comment")

	}

	@Test
	def void testNamespaceAndCategory() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0		
				displayname 'Refrigerator'
				category indigo/demo						
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/indigo/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/indigo/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.indigo.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/indigo/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);
	}

	@Test
	def void testFunctionBlockName_With_CapitalLetter_CamelCase() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0			
				displayname 'Motion Detector'
				category demo					
				functionblock MotionDetector{
					configuration{
						mandatory temperature as int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "MotionDetector.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/MotionDetector/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/MotionDetector/1"
			   elementFormDefault="qualified">
			   <xs:element name="motionDetector">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Motion Detector"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "MotionDetector.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.motiondetector"
				name="MotionDetector"
				namespace="http://www.bosch.com/functionblock/demo/MotionDetector/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);

	}

	@Test
	def void testStatus() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo								
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "")

	}

	@Test
	def void testFault() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0		
				displayname 'Refrigerator'
				category demo						
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, null)
	}

	@Test
	def void testOperation_NoParam_void() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0			
				displayname 'Refrigerator'
				description 'comment'
				category demo					
				functionblock Fridge
					configuration
						mandatory temperature as int
					operations
						on()
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation>comment</tns:documentation>
				<tns:oneWayOperation name="on" empty="true">
				</tns:oneWayOperation>
			</tns:service>	
		'''.toString
		println(expected)
		println(file.toString)
		assertEquals(expected, file.toString)

	}

	@Test
	def void testOperations_NoParams_ReturningPrimitive() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on()
						off() returns int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="offReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="intValue" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:oneWayOperation name="on" empty="true">
				</tns:oneWayOperation>
				<tns:requestResponseOperation name="off" empty="true">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);
	}

	@Test
	def void testOperations_NoParams_ReturningPrimitiveArray() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on()
						off() returns multiple int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="offReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="intValue" type="xs:int" maxOccurs="unbounded"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		println(file.toString)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:oneWayOperation name="on" empty="true">
				</tns:oneWayOperation>
				<tns:requestResponseOperation name="off" empty="true">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);
	}

	@Test
	def void testEntity() {
		val resourceSet = resourceSetProvider.get		
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   entity Temperature{
						mandatory temperature as int
					}'''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo					
				using com.bosch.Temperature			
				functionblock Fridge{
					configuration{
						mandatory temperature as Temperature
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="tns:Temperature" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "")
	}

	@Test
	def void testEmptyEntity() {
		val resourceSet = resourceSetProvider.get		
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   entity Temperature{}'''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo				
				using com.bosch.Temperature			
				functionblock Fridge{
					configuration{
						mandatory temperature as Temperature
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			   </xs:complexType>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="tns:Temperature" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "")
	}

	@Test
	def void testManyEntities() {
		
		val resourceSet = resourceSetProvider.get

		val temperatureRs = resourceSet.createResource(URI.createURI("temp.type"))

		temperatureRs.load(new StringInputStream(
				'''namespace com.bosch
				   version 1.0.0
				   displayname 'Temperature'
				   category demo					   					   
				   entity Temperature{
				   	mandatory temperature as int
				   }'''
				   ), emptyMap)

		val powerConsumption = resourceSet.createResource(URI.createURI("power.type"))

		powerConsumption.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'PowerConsumption'
				   category demo					   					   
				   entity PowerConsumption{
				   	    mandatory value as int
				   }'''
				   ), emptyMap)
				   
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo				
				using com.bosch.Temperature;1.0.0
				using com.bosch.PowerConsumption;1.0.0			
				functionblock Fridge{
				   	configuration{
				   		mandatory temperature as Temperature
				   	}
				   	status{
				   		mandatory powerConsumption as PowerConsumption
				   	}
				   	fault{
				   		mandatory shortCircuit as boolean
				   	}
				   }
				   
			   ''', resourceSet);
			   
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:complexType name="PowerConsumption">
			      <xs:sequence>
			         <xs:element name="value" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="tns:Temperature" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="powerConsumption" type="tns:PowerConsumption" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "")
	}

	@Test
	def void testOperation_1_param_void() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(RGB as int)
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:oneWayOperation name="on" empty="false">
				</tns:oneWayOperation>
			</tns:service>	
		'''.toString
		assertEquals(expected, file.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="RGB" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testOperation_1_paramArray_void() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(multiple RGB as int)
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:oneWayOperation name="on" empty="false">
				</tns:oneWayOperation>
			</tns:service>	
		'''.toString
		assertEquals(expected, file.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="RGB" type="xs:int" maxOccurs="unbounded"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testOperation_2_param_void() {
		val resourceSet = resourceSetProvider.get		
		val mytype = resourceSet.createResource(URI.createURI("my.type"))
		
		mytype.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   entity Temperature{mandatory temperature as int} '''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo				
				using com.bosch.Temperature				
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(temperature as Temperature , time as int)
					}
				}
				} 	
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:oneWayOperation name="on" empty="false">
				</tns:oneWayOperation>
			</tns:service>	
		'''.toString
		assertEquals(expected, file.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temperature" type="tns:Temperature" maxOccurs="1"/>
			   				<xs:element name="time" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testOperation_1_Param_ReturningPrimitive() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(time as int) returns int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file1 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected1 = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:requestResponseOperation name="on" empty="false">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''.toString
		assertEquals(expected1, file1.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="onReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="intValue" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="time" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testOperations_2_Param_ReturningPrimitive() {
		val resourceSet = resourceSetProvider.get		
		val mytype = resourceSet.createResource(URI.createURI("my.type"))
		
		mytype.load(new StringInputStream(
			   '''namespace com.bosch
			   version 1.0.0
			   displayname 'dummy'
			   category demo			   
			   entity Temperature{mandatory temperature as int} '''
				   ), emptyMap)
				   		
				   				
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo				
				using com.bosch.Temperature				
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(temperature as Temperature , time as int) returns int
					}
				}	
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file1 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected1 = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:requestResponseOperation name="on" empty="false">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''.toString
		assertEquals(expected1, file1.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:element name="onReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="intValue" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temperature" type="tns:Temperature" maxOccurs="1"/>
			   				<xs:element name="time" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testOperations_Returning_Entity() {
		val resourceSet = resourceSetProvider.get		
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   entity Temperature{
						mandatory temperature as int
				   }'''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo					
				using com.bosch.Temperature		
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on() returns Temperature
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file1 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected1 = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:requestResponseOperation name="on" empty="true">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''.toString
		assertEquals(expected1, file1.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:element name="onReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temperature" type="tns:Temperature" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testOperation_Returning_EntityArray() {
		val resourceSet = resourceSetProvider.get		
		val mytype = resourceSet.createResource(URI.createURI("my.type"))
		
		mytype.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   entity Temperature{mandatory temperature as int} '''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo				
				using com.bosch.Temperature				
				functionblock Fridge{

					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on() returns multiple Temperature
					}
				}	
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file1 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected1 = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:requestResponseOperation name="on" empty="true">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''.toString
		assertEquals(expected1, file1.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:element name="onReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temperature" type="tns:Temperature" maxOccurs="unbounded"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testOperation_1_ParamArray_Returning_EntityArray() {
		val resourceSet = resourceSetProvider.get		
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   entity Temperature{mandatory temperature as int} 	
				   '''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo				
				using com.bosch.Temperature				
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(multiple temp as int) returns multiple Temperature
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file1 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected1 = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:requestResponseOperation name="on" empty="false">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''.toString
		assertEquals(expected1, file1.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:element name="onReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temperature" type="tns:Temperature" maxOccurs="unbounded"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temp" type="xs:int" maxOccurs="unbounded"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testMandatoryProperty() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{
					configuration{
						mandatory multiple temperature as int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="unbounded" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "")

	}

	@Test
	def void testOptionalPropertyArray() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{

					configuration{
						optional multiple temperature as int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="unbounded" minOccurs="0"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "")

	}

	@Test
	def void testOptionalProperty() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{

					configuration{
						optional temperature as int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="0"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "")

	}

	@Test
	def void testDatatype_DateTime() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{

					configuration{
						mandatory temperature as dateTime
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:dateTime" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "")

	}

	@Test
	def void testGenerateBindingDescriptor() {
		val resourceSet = resourceSetProvider.get		
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   entity Temperature{
						optional value as int
				   }'''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo						
				using com.bosch.Temperature		
				functionblock Fridge{

					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on() returns Temperature
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file1 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "jaxb-bindings.xjb");

		val expected = '''
			<jaxb:bindings xmlns:jaxb="http://java.sun.com/xml/ns/jaxb" xmlns:xs="http://www.w3.org/2001/XMLSchema"
			xmlns:xjc="http://java.sun.com/xml/ns/jaxb/xjc" jaxb:extensionBindingPrefixes="xjc" jaxb:version="2.1">
				<jaxb:globalBindings>
					<xjc:serializable uid="1" />
					<xjc:superInterface name="java.io.Serializable" />
					<!-- use simple minded binding see http://weblogs.java.net/blog/2006/03/03/why-does-jaxb-put-xmlrootelement-sometimes-not-always -->
					<xjc:simple />
				</jaxb:globalBindings>
				<jaxb:bindings schemaLocation="../../generated-resources/OSGI-INF/functionblock/Fridge.xsd">
					<jaxb:bindings node="//xs:element[@name='fridge']">
						<jaxb:class name="FridgeProperties"></jaxb:class>
					</jaxb:bindings>
				</jaxb:bindings>
			</jaxb:bindings>
		'''.toString
		println(file1.toString)
		println(expected);
		assertEquals(expected, file1.toString)
	}

	@Test
	def void testNestedEntities() {
		val resourceSet = resourceSetProvider.get
	
		val humidity = resourceSet.createResource(URI.createURI("Humidity.type"))
		
		humidity.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   entity Humidity{
						mandatory value as int
				   }'''
				   ), emptyMap)
				   	
		val extraInfo = resourceSet.createResource(URI.createURI("ExtraInfo.type"))
	
		extraInfo.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   		
				   using com.bosch.Humidity;1.0.0 			   
				   entity ExtraInfo{
						mandatory degrees as string
						mandatory humidity as Humidity
				   }'''
				   ), emptyMap)

		val temperatureRs = resourceSet.createResource(URI.createURI("Temperature.type"))

		temperatureRs.load(new StringInputStream(
				'''namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   	
				   using com.bosch.ExtraInfo;1.0.0				   
				   entity Temperature{
						mandatory value as int
						mandatory extraInfo as ExtraInfo
					}'''
				   ), emptyMap)
				   				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo					
				using com.bosch.Humidity;1.0.0	
				using com.bosch.ExtraInfo;1.0.0			
				using com.bosch.Temperature;1.0.0			
				functionblock Fridge{

					configuration{
						mandatory temperature as Temperature
					}
					status{
						mandatory temperature as Temperature
					}
				
					operations{
						getTemperature() returns Temperature
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="value" type="xs:int" maxOccurs="1" minOccurs="1"/>
			         <xs:element name="extraInfo" type="tns:ExtraInfo" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:complexType name="ExtraInfo">
			      <xs:sequence>
			         <xs:element name="degrees" type="xs:string" maxOccurs="1" minOccurs="1"/>
			         <xs:element name="humidity" type="tns:Humidity" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:complexType name="Humidity">
			      <xs:sequence>
			         <xs:element name="value" type="xs:int" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:element name="getTemperatureReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temperature" type="tns:Temperature" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="tns:Temperature" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="tns:Temperature" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		println(expected)
		println(file.toString)
		assertEquals(expected, file.toString)
	}

	@Test
	def void testVersion() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 2.0.0
				displayname 'Refrigerator'
				category demo								
				functionblock Fridge{

					configuration{
						mandatory temperature as dateTime
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/2"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/2"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:dateTime" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/2"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);

	}

	@Test
	def void test_Version_Qualifier() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 12.1.0-SNAPSHOT
				displayname 'Refrigerator'
				category demo								
				functionblock Fridge{
					configuration{
						mandatory temperature as dateTime
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/12"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/12"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:dateTime" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/12"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);

	}

	@Test
	def test_FuncBlock_Description() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 12.1.0-SNAPSHOT	
				displayname 'Refrigerator'
				description 'This is nice refrigerator'
				category demo						
				functionblock Fridge{
					configuration{
						mandatory temperature as dateTime
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/12"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/12"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:annotation>
			   			<xs:documentation>This is nice refrigerator</xs:documentation>
			   		</xs:annotation>
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:dateTime" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/12"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation>This is nice refrigerator</tns:documentation>
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);
	}

	@Test
	def void test_Property_Description() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0		
				displayname 'Refrigerator'
				category demo						
				functionblock Fridge{

					configuration{
						mandatory temperature as dateTime 'this is the datetime'
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:dateTime" maxOccurs="1" minOccurs="1">
			   							   <xs:annotation>
			   							      <xs:documentation>this is the datetime</xs:documentation>
			   							   </xs:annotation>
			   							</xs:element>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);

	}

	@Test
	def void test_Entity_Description() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo							
				functionblock Fridge{

					configuration{
						mandatory temperature as dateTime
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:dateTime" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expectedXML = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
			</tns:service>	
		'''

		assertEquals(expectedXML, xml.toString);

	}

	@Test
	def void test_Enum_XSD() {
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				description 'comment'
				category demo								
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
				}
			''')
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:element name="fridge">
			   		<xs:annotation>
			   			<xs:documentation>comment</xs:documentation>
			   		</xs:annotation>
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "comment")

	}

	@Test
	def void test_Enum_Configuration() {
		val resourceSet = resourceSetProvider.get		
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   enum unit{
						Kelvin, Degree , Celcius
				   }'''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				description 'comment'
				category demo				
				using com.bosch.unit				
				functionblock Fridge{

					configuration{
						mandatory temperature as int
						mandatory metricUnit as unit
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:simpleType name="unit">
			      <xs:restriction base="xs:string">
			   	     <xs:enumeration value="Kelvin"/>
			   	     <xs:enumeration value="Degree"/>
			   	     <xs:enumeration value="Celcius"/>
			      </xs:restriction>
			   </xs:simpleType>
			   <xs:element name="fridge">
			   		<xs:annotation>
			   			<xs:documentation>comment</xs:documentation>
			   		</xs:annotation>
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   							<xs:element name="metricUnit" type="tns:unit" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(expected)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "comment")

	}

	@Test
	def void testEnum_OperationParam() {
		val resourceSet = resourceSetProvider.get		
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   enum unit{
						Kelvin, Degree , Celcius
				   }'''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0	
				displayname 'Refrigerator'
				category demo				
				using com.bosch.unit			
				functionblock Fridge{
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(metricUnit as unit)
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:oneWayOperation name="on" empty="false">
				</tns:oneWayOperation>
			</tns:service>	
		'''.toString
		assertEquals(expected, file.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:simpleType name="unit">
			      <xs:restriction base="xs:string">
			   	     <xs:enumeration value="Kelvin"/>
			   	     <xs:enumeration value="Degree"/>
			   	     <xs:enumeration value="Celcius"/>
			      </xs:restriction>
			   </xs:simpleType>
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="metricUnit" type="tns:unit" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testEnum_Multiple_OperationParam() {
		val resourceSet = resourceSetProvider.get		
		val mytype = resourceSet.createResource(URI.createURI("my.type"))
		
		mytype.load(new StringInputStream(
			   '''namespace com.bosch
			   version 1.0.0
				   displayname 'dummy'
				   category demo			   
			   enum unit{
					Kelvin, Degree , Celcius
				}'''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo				
				using com.bosch.unit				
				functionblock Fridge{
					
					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(multiple metricUnit as unit)
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:oneWayOperation name="on" empty="false">
				</tns:oneWayOperation>
			</tns:service>	
		'''.toString
		assertEquals(expected, file.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:simpleType name="unit">
			      <xs:restriction base="xs:string">
			   	     <xs:enumeration value="Kelvin"/>
			   	     <xs:enumeration value="Degree"/>
			   	     <xs:enumeration value="Celcius"/>
			      </xs:restriction>
			   </xs:simpleType>
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="metricUnit" type="tns:unit" maxOccurs="unbounded"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testEnum_OperationReturn() {
		val resourceSet = resourceSetProvider.get
	
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   enum unit{
						Kelvin, Degree , Celcius
				   }'''
				   ), emptyMap)
				   		
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo					
				using com.bosch.unit	
				functionblock Fridge{

					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(temperature as int) returns unit
					}
				}
			''',resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:requestResponseOperation name="on" empty="false">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''.toString
		assertEquals(expected, file.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:simpleType name="unit">
			      <xs:restriction base="xs:string">
			   	     <xs:enumeration value="Kelvin"/>
			   	     <xs:enumeration value="Degree"/>
			   	     <xs:enumeration value="Celcius"/>
			      </xs:restriction>
			   </xs:simpleType>
			   <xs:element name="onReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="unit" type="tns:unit" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temperature" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void testEnum_Multiple_OperationReturn() {
		val resourceSet = resourceSetProvider.get
	
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   enum unit{
						Kelvin, Degree , Celcius
				   }'''
				   ), emptyMap)
				   		
				   				
		val model = parseHelper.parse(
			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				category demo				
				using com.bosch.unit				
				functionblock Fridge{

					configuration{
						mandatory temperature as int
					}
					status{
						mandatory blinking as boolean
					}
					fault{
						mandatory shortCircuit as boolean
					}
					operations{
						on(temperature as int) returns multiple unit
					}
				}
			''', resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		val expected = '''
			<?xml version='1.0' encoding='UTF-8'?>
			<tns:service eventConsumer="false" revision="$Rev:$"
				javaPackageName="com.bosch.functionblock.demo.fridge"
				name="Fridge"
				namespace="http://www.bosch.com/functionblock/demo/Fridge/1"
				xmlns:tns="http://www.bosch.com/ism/servicemetamodel"
				xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				xsi:schemaLocation="http://www.bosch.com/ism/servicemetamodel http://rb-tmp-dev.de.bosch.com/iapsr/ServiceMetaModel/1/ServiceMetaModel.xsd">
				<tns:documentation></tns:documentation>
				<tns:requestResponseOperation name="on" empty="false">
				</tns:requestResponseOperation>	
			</tns:service>	
		'''.toString
		assertEquals(expected, file.toString)

		var file2 = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected2 = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:simpleType name="unit">
			      <xs:restriction base="xs:string">
			   	     <xs:enumeration value="Kelvin"/>
			   	     <xs:enumeration value="Degree"/>
			   	     <xs:enumeration value="Celcius"/>
			      </xs:restriction>
			   </xs:simpleType>
			   <xs:element name="onReply">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="unit" type="tns:unit" maxOccurs="unbounded"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="on">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="temperature" type="xs:int" maxOccurs="1"/>
			   			</xs:sequence>
			   		</xs:complexType>
			   </xs:element>
			   <xs:element name="fridge">
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="shortCircuit" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="blinking" type="xs:boolean" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString

		assertEquals(expected2, file2.toString)
	}

	@Test
	def void test_Enum_With_Entity() {
		val resourceSet = resourceSetProvider.get
	
		val unit = resourceSet.createResource(URI.createURI("unit.type"))
		
		unit.load(new StringInputStream(
			   '''namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo				   					   
				   enum unit{
						Kelvin, Degree , Celcius
				   }'''
				   ), emptyMap)

		val temp = resourceSet.createResource(URI.createURI("temp.type"))
		
		temp.load(new StringInputStream(
			   ''' namespace com.bosch
				   version 1.0.0
				   displayname 'dummy'
				   category demo
				   using com.bosch.unit;1.0.0				   				   
				   entity Temperature{
						mandatory temperature as int
						mandatory metricUnit as unit
				   }'''
				   ), emptyMap)
				   				   		
		val model = parseHelper.parse(			'''
				namespace com.bosch
				version 1.0.0
				displayname 'Refrigerator'
				description 'comment'
				category demo					
				using com.bosch.Temperature;1.0.0				
				functionblock Fridge{

				      	configuration{
				      		mandatory temperature as Temperature
				      	}
				      }				      
			      ''',resourceSet)
		val fsa = new InMemoryFileSystemAccess()
		underTest.doGenerate(model.eResource, fsa)

		var file = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xsd");

		val expected = '''
			<?xml version="1.0" encoding="UTF-8"?>
			<xs:schema
			   xmlns:xs="http://www.w3.org/2001/XMLSchema"
			   xmlns:tns="http://www.bosch.com/functionblock/demo/Fridge/1"
			   targetNamespace="http://www.bosch.com/functionblock/demo/Fridge/1"
			   elementFormDefault="qualified">
			   <xs:complexType name="Temperature">
			      <xs:sequence>
			         <xs:element name="temperature" type="xs:int" maxOccurs="1" minOccurs="1"/>
			         <xs:element name="metricUnit" type="tns:unit" maxOccurs="1" minOccurs="1"/>
			      </xs:sequence>
			   </xs:complexType>
			   <xs:simpleType name="unit">
			      <xs:restriction base="xs:string">
			   	     <xs:enumeration value="Kelvin"/>
			   	     <xs:enumeration value="Degree"/>
			   	     <xs:enumeration value="Celcius"/>
			      </xs:restriction>
			   </xs:simpleType>
			   <xs:element name="fridge">
			   		<xs:annotation>
			   			<xs:documentation>comment</xs:documentation>
			   		</xs:annotation>
			   		<xs:complexType>
			   			<xs:sequence>
			   				<xs:element name="fault">
			   				</xs:element>
			   				<xs:element name="configuration">
			   					<xs:complexType>
			   						<xs:sequence>
			   							<xs:element name="temperature" type="tns:Temperature" maxOccurs="1" minOccurs="1"/>
			   						</xs:sequence>
			   					</xs:complexType>
			   				</xs:element>
			   				<xs:element name="status">
			   				</xs:element>
			   			</xs:sequence>
			   			<xs:attribute name="displayname" type="xs:string" use="required" fixed="Refrigerator"/>
			   		</xs:complexType>
			   	</xs:element>
			</xs:schema>
		'''.toString
		println(file.toString)
		assertEquals(expected, file.toString)

		var xml = fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT + "Fridge.xml");

		compare_With_Empty_Operation_XML(xml.toString, "comment")

	}
}
