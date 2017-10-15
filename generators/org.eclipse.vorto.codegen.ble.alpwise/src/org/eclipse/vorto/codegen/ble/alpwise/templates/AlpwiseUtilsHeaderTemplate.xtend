package org.eclipse.vorto.codegen.ble.alpwise.templates

import org.eclipse.vorto.codegen.api.InvocationContext
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel

class AlpwiseUtilsHeaderTemplate extends AlpwiseTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		return "BleUtils.h"
	}
	
	override getPath(InformationModel context) {
		return rootPath
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
#ifndef __BLEUTILS_H__
#define __BLEUTILS_H__

#include <stdint.h>
#include <attbase.h>

/**
 * Common properties for characteristic entries in the attribute database
 */
typedef struct characteristicProperty_s {
	uint8_t                          uuid[16];        /* Characteristic UUID */
	AttCharacteriticProperties       flags;           /* Access flags */
	AttUuid                          type;            /* UUID type and pointer to UUID */
	Att128BitCharacteristicAttribute characteristic;
	AttAttribute                     attr;            /* Attribute handle */
} characteristicProperty_t;

/**@brief This function prints out the error return value when a notification is sent.
 * @param[in] status The return value of the send notifcation function
 *
 */
void printSendNotificationError(BleStatus status);

#endif /* __BLEUTILS_H__ */
'''
	}	
}