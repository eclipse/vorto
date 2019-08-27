import numbers
import json

class DittoSerializer(object):
    
    def __init__(self):
        self.payload = ""
        self.first_prop = True

    def serialize_functionblock(self, name, object, ditto_topic, hono_clientId):
        self.payload += "{\"topic\": \"" + ditto_topic + "/things/twin/commands/modify\","
        self.payload += "\"headers\": {\"response-required\": false},"
        self.payload += "\"path\": \"/features/"+name+"/properties\",\"value\" : {\"status\" : {"
        object.serializeStatus(self)
        self.payload += "}, \"configuration\" : {"
        object.serializeConfiguration(self)
        self.payload += "} } }"
        returnPayload = self.payload
        # RESET
        self.payload = ""
        self.first_prop = True
        return returnPayload

    def serialize_property(self, name, value):
        if not self.first_prop:
            self.payload += ", "
        else:
            self.first_prop = False
        if isinstance(value, numbers.Number):
            self.payload += "\"" + name + "\": " + str(value)
        elif isinstance(value,dict):
        	self.payload += "\"" + name + "\": " + json.dumps(value)
        else:
            self.payload += "\"" + name + "\": \"" + str(value) + "\""
