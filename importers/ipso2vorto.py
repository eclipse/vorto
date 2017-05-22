#!/usr/bin/python2.7

"""ipso2vorto.py: This script takes IPSO and LWM2M descriptions in form of XMLs and
                  transforms them to a Vorto Function Block model and the
                  corresponding Mapping model"""

__author__ = "Jeroen Laverman"
__copyright__   = "Copyright 2016, Bosch Software Innovations GmbH"
__credits__ = ["Jeroen Laverman"]
__license__ = "-"
__version__ = "1.0.0"
__maintainer__ = "Jeroen Laverman"
__email__ = "jeroen.laverman@bosch-si.com"
__status__ = "beta"

import xml.etree.ElementTree as ET
import os
import re


# Start ############################ parseDescr()
def parseDescr(sb):
    sb = sb.replace("(", "'")
    sb = sb.replace(")", "'")
    return sb.replace("\"","'")
# End ############################ parseDescr()

# Start ############################ parseID()
def parseID(sb):
    sb = sb.replace("/","_")
    sb = sb.replace(" ","_")
    sb = sb.replace("-","_")
    sb = sb.replace("\"","'")
    return sb
# End ############################ parseID()

# Start ########################### stripParanthesis()
def stripParanthesis(sb):
    if sb.find('(') == -1:
        return sb;
    else:
        startIdx =  sb.index("(")
        return sb[:startIdx].strip()

# End ############################ stripParanthesis()

# Start ############################ removeNumberPrefix()
def removeNumberPrefix(sb):
    numberValue = re.match('^[0-9]*', sb).group(0)
    if len(numberValue) == 0:
        return sb.strip()
    else:
        return sb[len(numberValue):].strip()

# End ############################## removeNumberPrefix()

# Start ############################ parseType()
def parseType(dataType):
    if dataType == "string":
        return "string"
    if dataType == "float":
        return "float"
    if dataType == "integer":
        return "int"
    if dataType == "opaque":
        return "byte"
    if dataType == "time":
        return "dateTime"
    if dataType == "boolean":
        return "boolean"
    if dataType == "objlnk":
        return "string"
    return "ERROR"
# End ############################ parseType()

# Start ############################ parseFunctionBlockModel()
def parseFunctionBlockModel(LWM2M, projectName, namespace, version):
    for Object in LWM2M:
        sb = 'namespace ' + namespace + '\n'
        sb += 'version ' + version + '\n'

        objname = parseID(Object.find('Name').text)
        sb += 'displayname \"' + objname + '\"\n'

        desc1 = parseDescr(Object.find('Description1').text)
        # objId = parseID(Object.find('ObjectID').text)
        sb += 'description \"' + desc1 + '\"\n'

        category = 'SmartObject'
        sb += 'category ' + category + '\n\n'
        sb += 'functionblock ' + objname + ' {\n\n'
        sb += '\tstatus {\n'

        for Item in Object.find('Resources').findall('Item'):

            operations = Item.find("Operations").text

            if operations in ('R', 'W', 'RW'):
                mandatory = Item.find('Mandatory').text.lower()
                sb += '\t\t' + mandatory

                name = stripParanthesis(Item.find('Name').text.lower())
                name = removeNumberPrefix(name)
                name = parseID(name)
                if name == "description":
                    name = name + "Item"
                elif name == "status":
                    name = name + "Item"
                sb += ' '+ name + ' as'

                primType = parseType(Item.find('Type').text.lower())

                if operations == 'R':
                    sb += ' ' + primType + ' with { readable: true}'
                if operations == 'W':
                    sb += ' ' + primType + ' with { writable: true}'
                if operations == 'RW':
                    sb += ' ' + primType + ' with { readable: true, writable: true}'

                # itemId = Item.get('ID')
                desc = parseDescr(Item.find('Description').text)
                sb += ' \"' + desc + '\"\n'

        sb += '\t}\n\n'
        sb += '\toperations {\n'

        for Item in Object.find('Resources').findall('Item'):
            operations = Item.find("Operations").text
            if operations:
                if operations in ('E'):
                    name = parseID(Item.find('Name').text.lower())
                    sb += '\t\t' + name + '() '

                    # itemId = Item.get('ID')
                    desc = parseDescr(Item.find('Description').text)
                    sb += ' \"' + desc + '\"\n'


        sb += '\t}\n'
    sb += '}\n'

    directory = projectName + '/functionblocks/'
    filename = namespace + '.' + parseID(Object.find('Name').text) + '_' \
            + version.replace(".","_") + ".fbmodel"
    outputFile = directory + filename

    if not os.path.exists(os.path.dirname(outputFile)):
        try:
            os.makedirs(os.path.dirname(outputFile))
        except OSError as exc: # Guard against race condition
            if exc.errno != errno.EEXIST:
                raise
    with open(outputFile, "w") as f:
        f.write(sb.encode("utf-8"))
# End ############################ parseFunctionBlockModel()


# Start ############################ parseFunctionBlockMapping()
def parseFunctionBlockMapping(LWM2M, projectName, namespace, version, license_text):
    for Object in LWM2M:
        #namespace = 'com.ipso.smartobjects'
        namespace = namespace
        sb = 'namespace ' + namespace + '\n'
        sb += 'version ' + version + '\n'

        objname = parseID(stripParanthesis(Object.find('Name').text))
        sb += 'displayname \"' + objname + '_lwm2m\"\n'
        sb += 'description \"Mapping model for ' + objname + '\"\n'

        #sb += 'using com.ipso.smartobjects.' + objname + ';' + version + '\n\n'
        sb += 'using ' + namespace + '.' + objname + ';' + version + '\n\n'

        sb += 'functionblockmapping ' + objname + '_mapping {\n'
        sb += '\ttargetplatform lwm2m\n\n'

        sb += '\t from ' + objname + ' to Object with {'

        objId = parseID(Object.find('ObjectID').text)
        objUrl = Object.find('ObjectURN').text
        objMultiInstance = Object.find('MultipleInstances').text
        objMandatory = Object.find('Mandatory').text
        objDescr = Object.find('Description2').text

        sb += 'Name: \"' + Object.find('Name').text + '\", '
        sb += 'ObjectID: \"' + objId + '\", '
        sb += 'ObjectURL: \"' + objUrl + '\", '
        sb += 'MultipleInstances: \"' + objMultiInstance + '\", '
        sb += 'Mandatory: \"' + objMandatory + '\", '
        if not objDescr:
            sb += 'Description2: \"\"' + ', '
        else:
            sb += 'Description2: \"' + objDescr + '\", '
        sb += 'License: \"%s' % parseDescr(license_text) + '\"'
        sb += '}\n\n'


        for Item in Object.find('Resources').findall('Item'):

            operations = Item.find("Operations").text

            if operations:
                if operations in ('R', 'W', 'RW'):
                    name = parseID(removeNumberPrefix(stripParanthesis(Item.find('Name').text.lower())))
                    if name == "description":
                        name = name + "Item"
                    elif name == "status":
                        name = name + "Item"
                    sb += '\t from ' + objname + '.status.' +name
                    sb += ' to Resource with {'
                    itemId = Item.get('ID')
                    sb += 'ID: \"' + itemId + '\"'
                    sb += ', Name: \"' + Item.find('Name').text + '\"'
                    units = Item.find("Units").text
                    if units:
                        sb += ', Units: \"' + units + '\"'
                    sb += '}\n'

                if operations in ('E'):
                    name = parseID(removeNumberPrefix(stripParanthesis(Item.find('Name').text.lower())))
                    sb += '\t from ' + objname + '.operation.' +name
                    sb += ' to Resource with {'

                    itemId = Item.get('ID')
                    multiInstance = Item.find("MultipleInstances").text
                    mandatory = Item.find("Mandatory").text
                    sb += 'ID: \"' + itemId + '\"'
                    sb += ', Name: \"' + Item.find('Name').text + '\"'
                    sb += ', MultipleInstances: \"' + multiInstance + '\"'
                    sb += ', Mandatory: \"' + mandatory + '\"'
                    sb += '}\n'
    sb += '}\n'

    directory = projectName + '/mappings/'
    filename = namespace + '.' + parseID(Object.find('Name').text) + '_' \
            + version.replace(".","_") + "_lwm2m.mapping"
    outputFile = directory + filename

    if not os.path.exists(os.path.dirname(outputFile)):
        try:
            os.makedirs(os.path.dirname(outputFile))
        except OSError as exc: # Guard against race condition
            if exc.errno != errno.EEXIST:
                raise
    with open(outputFile, "w") as f:
        f.write(sb.encode("utf-8"))

# End ############################ parseFunctionBlockMapping()


# Start ############################ parseXMLFiles()

def parseXMLFiles(XML_PATH, PROJECT_NAME, NAMESPACE, VERSION, LICENSE_TEXT):
    for filename in os.listdir(XML_PATH):
        if not filename.endswith('.xml'): continue
        fullname = os.path.join(XML_PATH, filename)

        tree = ET.parse(fullname)
        LWM2M = tree.getroot()

        # Create FunctionBlock models
        parseFunctionBlockModel(LWM2M, PROJECT_NAME, NAMESPACE, VERSION)

        # Create corresponding LWM2M mapping files
        parseFunctionBlockMapping(LWM2M, PROJECT_NAME, NAMESPACE, VERSION, LICENSE_TEXT)

# End ############################ parseXMLFiles()

#path = 'xmltest/'

XML_PATH = 'xmls/ipso/'
PROJECT_NAME = 'IPSO'
NAMESPACE = 'com.ipso.smartobjects'
VERSION = '0.0.1'
LICENSE_TEXT = """
FILE INFORMATION

LEGAL DISCLAIMER

  Copyright 2017 Open Mobile Alliance All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions
  are met:

  1. Redistributions of source code must retain the above copyright
  notice, this list of conditions and the following disclaimer.
  2. Redistributions in binary form must reproduce the above copyright
  notice, this list of conditions and the following disclaimer in the
  documentation and/or other materials provided with the distribution.
  3. Neither the name of the copyright holder nor the names of its
  contributors may be used to endorse or promote products derived
  from this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
  "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
  FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE
  COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
  BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
  LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN
  ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
  POSSIBILITY OF SUCH DAMAGE.

  The above license is used as a license under copyright only.  Please
  reference the OMA IPR Policy for patent licensing terms:
  http://www.openmobilealliance.org/ipr.html
"""

parseXMLFiles(XML_PATH, PROJECT_NAME, NAMESPACE, VERSION, LICENSE_TEXT)

XML_PATH = 'xmls/lwm2m/'
PROJECT_NAME = 'LWM2M'
NAMESPACE = 'org.oma.lwm2m'

parseXMLFiles(XML_PATH, PROJECT_NAME, NAMESPACE, VERSION, LICENSE_TEXT)

