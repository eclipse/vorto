// StatusPropertiesFunctionBlock

#include "StatusPropertiesFunctionBlock.h"

using namespace org_eclipse_vorto;

StatusPropertiesFunctionBlock::StatusPropertiesFunctionBlock(){}

void StatusPropertiesFunctionBlock::setstatusValue(org_eclipse_vorto::UnitEntity _statusValue) {
	statusValue = _statusValue;
}

org_eclipse_vorto::UnitEntity StatusPropertiesFunctionBlock::getstatusValue() {
	return statusValue;
}
void StatusPropertiesFunctionBlock::setstatusBoolean(bool _statusBoolean) {
	statusBoolean = _statusBoolean;
}

bool StatusPropertiesFunctionBlock::getstatusBoolean() {
	return statusBoolean;
}


String StatusPropertiesFunctionBlock::serialize(String ditto_topic, String hono_deviceId, String fbName) {
    String result = "{\"topic\":\""+ ditto_topic +"/things/twin/commands/modify\",";
    result += "\"headers\":{\"response-required\": false},";
    result += "\"path\":\"/features/" + fbName + "/properties\",\"value\": {";
    //Status Properties
    result += "\"status\": {";
    result += "\"statusValue\" : " + statusValue.serialize();
    result += "\"statusBoolean\" : " + String(statusBoolean == 1 ? "true" : "false") + "";
    result += "}";


    result += "} }";

    return result;
}
