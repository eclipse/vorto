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
import org.eclipse.vorto.core.api.model.functionblock.FunctionblockModel
import org.eclipse.vorto.core.api.model.functionblock.impl.FunctionblockPackageImpl
import org.eclipse.vorto.editor.functionblock.FunctionblockInjectorProvider
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.InMemoryFileSystemAccess
import org.eclipse.xtext.junit4.InjectWith
import org.eclipse.xtext.junit4.XtextRunner
import org.eclipse.xtext.junit4.util.ParseHelper
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(typeof(XtextRunner))
@InjectWith(typeof(FunctionblockInjectorProvider))
class FbDatatypeTest extends FbAbstractGeneratorTest{
	
	IGenerator underTest = new GeneratorAdapter();
    @Inject ParseHelper<FunctionblockModel> parseHelper 
    
    @BeforeClass
	def static void initializeModel() {
		FunctionblockPackageImpl.init();
	}
    
    @Test
    def void test_IntPropert_Max(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as int <MAX 99>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:int">
					   							   	     <xs:maxInclusive value="99"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void test_IntPropert_Min(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as int <MIN 0>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:int">
					   							   	     <xs:minInclusive value="0"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void test_StrPropert_Regex(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory deviceName as string <REGEX "[A-Z][A-Z][A-Z]">
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="deviceName" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:string">
					   							   	     <xs:pattern value="[A-Z][A-Z][A-Z]"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void test_StrPropert_Strlen(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory deviceName as string <STRLEN 15>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="deviceName" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:string">
					   							   	     <xs:maxLength value="15"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void test_StrPropert_Strlen_and_Regex(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory deviceName as string <STRLEN 15,REGEX '[A-Z][A-Z][A-Z]'>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="deviceName" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:string">
					   							   	     <xs:maxLength value="15"/>
					   							   	     <xs:pattern value="[A-Z][A-Z][A-Z]"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void test_IntPropert_Min_with_Documentation(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as int <MIN 0> 'temperature reading'
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" maxOccurs="1" minOccurs="1">
					   							   <xs:annotation>
					   							      <xs:documentation>temperature reading</xs:documentation>
					   							   </xs:annotation>
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:int">
					   							   	     <xs:minInclusive value="0"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void testShort(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as short
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" type="xs:short" maxOccurs="1" minOccurs="1"/>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void testShortProp_Max(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as short <MAX 1111>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:short">
					   							   	     <xs:maxInclusive value="1111"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void testLongProp_Min(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as long <MIN 111111>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:long">
					   							   	     <xs:minInclusive value="111111"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void testDoubleProp_Min(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as double <MIN 111.111>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:double">
					   							   	     <xs:minInclusive value="111.111"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void testBase64Binary(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as base64Binary
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" type="xs:base64Binary" maxOccurs="1" minOccurs="1"/>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void testByte(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as byte
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" type="xs:byte" maxOccurs="1" minOccurs="1"/>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void test_ByteProp_Max(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as byte <MAX 111>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:byte">
					   							   	     <xs:maxInclusive value="111"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void test_ByteProp_Mimetype_NoEffect(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as byte <MIMETYPE 'sdafas'>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" type="xs:byte" maxOccurs="1" minOccurs="1"/>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void test_ByteProp_Mimetype_Min(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as byte <MIMETYPE 'sdafas',MIN 11>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" maxOccurs="1" minOccurs="1">
					   							   <xs:simpleType>
					   							      <xs:restriction base="xs:byte">
					   							   	     <xs:minInclusive value="11"/>
					   							   	  </xs:restriction>
					   							   </xs:simpleType>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void testByteProp(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as byte
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" type="xs:byte" maxOccurs="1" minOccurs="1"/>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    @Test
    def void testMultipleByteProp(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory multiple temperature as byte
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" type="xs:base64Binary" maxOccurs="1" minOccurs="1"/>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
    
    
    @Test
    def void test_BinaryBase64Prop_Mimetype_NoEffect(){
    	val model = parseHelper.parse('''
        namespace com.bosch
        version 1.0.0    	
        functionblock Fridge{
        	displayname 'Refrigerator'
        	description 'comment'
        	category demo
        	configuration{
        		mandatory temperature as base64Binary <MIMETYPE 'sdafas'>
        	}
        }
        ''')
        val fsa = new InMemoryFileSystemAccess()
        underTest.doGenerate(model.eResource, fsa)
        
        var file= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xsd");
        
        val expected ='''
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
					   							<xs:element name="temperature" type="xs:base64Binary" maxOccurs="1" minOccurs="1"/>
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
		assertEquals(expected,file.toString)
		
		var xml= fsa.allFiles.get(IFileSystemAccess::DEFAULT_OUTPUT+"Fridge.xml");
		
		compare_With_Empty_Operation_XML(xml.toString,"comment")
        
    }
}