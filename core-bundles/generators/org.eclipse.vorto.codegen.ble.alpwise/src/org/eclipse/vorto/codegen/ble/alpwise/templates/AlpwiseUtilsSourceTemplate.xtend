package org.eclipse.vorto.codegen.ble.alpwise.templates

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.ble.templates.BleGattTemplate

class AlpwiseUtilsSourceTemplate extends BleGattTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "BleUtils.c"
	}
	
	override getPath(InformationModel context) {
		return rootPath
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
#include <stdint.h>
#include <stdio.h>
#include <BleTypes.h>

#include "BleUtils.h"

void printSendNotificationError(BleStatus status) {
	if (status == BLESTATUS_SUCCESS) {
		printf("SendIndication status: BLESTATUS_SUCCESS\n");

	} else if (status == BLESTATUS_FAILED) {
		printf("SendIndication status: BLESTATUS_FAILED\n");
	} else if (status == BLESTATUS_INVALID_PARMS) {
		printf("SendIndication status: BLESTATUS_INVALID_PARMS\n");

	} else if (status == BLESTATUS_PENDING) {
		printf("SendIndication status: BLESTATUS_PENDING\n");
	} else if (status == BLESTATUS_BUSY) {
		printf("SendIndication status: BLESTATUS_BUSY\n");
	} else {
		printf("SendIndication status: other Error!\n");
	}
}
'''
	}
	
}