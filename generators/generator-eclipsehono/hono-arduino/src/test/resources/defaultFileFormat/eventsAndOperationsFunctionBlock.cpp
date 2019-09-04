// eventsAndOperationsFunctionBlock

#include "eventsAndOperationsFunctionBlock.h"

using namespace org_eclipse_vorto;

eventsAndOperationsFunctionBlock::eventsAndOperationsFunctionBlock(){}



String eventsAndOperationsFunctionBlock::serialize(String ditto_topic, String hono_deviceId, String fbName) {
    String result = "{\"topic\":\""+ ditto_topic +"/things/twin/commands/modify\",";
    result += "\"headers\":{\"response-required\": false},";
    result += "\"path\":\"/features/" + fbName + "/properties\",\"value\": {";


    result += "} }";

    return result;
}
