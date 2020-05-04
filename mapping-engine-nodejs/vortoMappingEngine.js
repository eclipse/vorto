/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
// Load dependencies
const convertToXML = require("xml-js");
const log = require("loglevel");
const fontoxpath = require("fontoxpath");
const parser = require("slimdom-sax-parser");


module.exports = class VortoMapper {
    constructor(mappingSpec) {
        this.mappingSpec = mappingSpec;
    }

    setMappingSpec(mappingSpec) {
        this.mappingSpec = mappingSpec;
    }

    setLogLevel(message) {
        if (["trace", "debug", "info", "warn", "error"].includes(message)) {
            log.setLevel(message);
        }
    }

    // avoid export of fontoxpath and just pass on all arguments to the fontoxpath method
    registerCustomXPathFunction() {
        fontoxpath.registerCustomXPathFunction.apply(null, arguments);
    }

    transform(rawPayload) {
        // Convert rawPayload to xml in order to use xpath
        const options = { compact: true, ignoreComment: true };
        let xmlRawPayload = convertToXML.json2xml(rawPayload, options);

        // Add a root element 
        xmlRawPayload = `<rootNode>${xmlRawPayload}</rootNode>`;
        log.debug("Raw device payload in xml ...\n" + xmlRawPayload);

        const doc = parser.sync(xmlRawPayload);

        let outputObj = {};
        // Iterate through the mapping spec and look for function blocks
        // Step 1: Iterate over number of function blocks in the information model
        try {
            const numberOfFunctionBlocks = this.mappingSpec.infoModel.functionblocks.length;
            if (numberOfFunctionBlocks) {
                log.debug("Number of function blocks found = " + numberOfFunctionBlocks);

                this.mappingSpec.infoModel.functionblocks.forEach((functionBlock) => {
                    const fbName = functionBlock.name;
                    // Step 2: Search for status properties in the function block along with the mapping
                    const status = this.getStatusMapping(doc, fbName);

                    // Step 3: Add all properties under the user defined function block variable
                    outputObj[fbName] = { status };
                });

            }
        } catch (err) {
            log.error("Error : " + err.message);
            throw new Error("Failed!\n" + err.message);
        }

        const outputObjStr = JSON.stringify(outputObj, null, 0);
        log.debug("Final output... \n" + outputObjStr);

        return outputObjStr;
    }

    getStatusMapping(doc, fbName) {
        let status = {};
        const numberOfStatusProperties = this.mappingSpec.properties[fbName].statusProperties.length;
        if (numberOfStatusProperties) {
            log.debug("Number of status properties found = " + numberOfStatusProperties);

            for (let countSP = 0; countSP < numberOfStatusProperties; countSP++) {
                const currentStatus = this.mappingSpec.properties[fbName].statusProperties[countSP];

                const statusPropertyName = currentStatus.name;
                const path = currentStatus.stereotypes[0].attributes.xpath;

                log.debug("path : " + path);
                if (path) {
                    // Step 3 : Evaluate xpath expression
                    const xpathResult = fontoxpath.evaluateXPathToString(path, doc);
                    log.debug("xpathResult = " + xpathResult);
                    status[statusPropertyName] = xpathResult;
                }
            }
        }
        return status;
    }
};
