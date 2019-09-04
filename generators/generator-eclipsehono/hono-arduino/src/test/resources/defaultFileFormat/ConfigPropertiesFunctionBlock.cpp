// ConfigPropertiesFunctionBlock

#include "ConfigPropertiesFunctionBlock.h"

using namespace org_eclipse_vorto;

ConfigPropertiesFunctionBlock::ConfigPropertiesFunctionBlock(){}


void ConfigPropertiesFunctionBlock::setconfigValue(org_eclipse_vorto::UnitEntity _configValue) {
	configValue = _configValue;
}

org_eclipse_vorto::UnitEntity ConfigPropertiesFunctionBlock::getconfigValue() {
	return configValue;
}
void ConfigPropertiesFunctionBlock::setconfigBoolean(bool _configBoolean) {
	configBoolean = _configBoolean;
}

bool ConfigPropertiesFunctionBlock::getconfigBoolean() {
	return configBoolean;
}

String ConfigPropertiesFunctionBlock::serialize(String ditto_topic, String hono_deviceId, String fbName) {
    String result = "{\"topic\":\""+ ditto_topic +"/things/twin/commands/modify\",";
    result += "\"headers\":{\"response-required\": false},";
    result += "\"path\":\"/features/" + fbName + "/properties\",\"value\": {";


    //Configuration Properties
    result += "\"configuration\": {";
    result += configValue.serialize();
    result += "\"configBoolean\" : " + String(configBoolean == 1 ? "true" : "false") + "";
    result += "}";
    result += "} }";

    return result;
}
