import netifaces
import paho.mqtt.client as mqtt
import datetime, threading, time
import model.functionblock.StatusPropertiesFunctionBlock as statusPropertiesFunctionBlock 
import model.functionblock.ConfigPropertiesFunctionBlock as configPropertiesFunctionBlock 
import model.functionblock.eventsAndOperationsFunctionBlock as eventsAndOperationsFunctionBlock 
import model.infomodel.MySensor as MySensor
import model.DittoSerializer as DittoSerializer

# DEVICE CONFIG GOES HERE
tenantId = "ADD TENANT HERE"
device_password = "ADD DEVICE PASSWORD HERE"
hub_adapter_host = "mqtt.bosch-iot-hub.com"
deviceId = "ADD DEVICE ID HERE"
clientId = deviceId
authId = "ADD AUTH ID HERE"
certificatePath = "ADD PATH TO SERVER CERTIFICATE HERE"
ditto_topic = "ADD TOPIC HERE, e.g. com.mycompany/4711"


# The callback for when the client receives a CONNACK response from the server.
def on_connect(client, userdata, flags, rc):
    global next_call

    if rc != 0:
        print("Connection to MQTT broker failed: " + str(rc))
        return

    # Subscribing in on_connect() means that if we lose the connection and
    # reconnect then subscriptions will be renewed.

    # BEGIN SAMPLE CODE
    client.subscribe("commands/" + tenantId + "/")
    # END SAMPLE CODE

    # Time stamp when the periodAction function shall be called again
    next_call = time.time()
    
    # Start the periodic task for publishing MQTT messages
    periodicAction()

# The callback for when a PUBLISH message is received from the server.
def on_message(client, userdata, msg):
    
    ### BEGIN SAMPLE CODE
    
    print(msg.topic+" "+str(msg.payload))

    ### END SAMPLE CODE

# The functions to publish the functionblocks data
def publishStatusPropertiesFunctionBlock():
    payload = ser.serialize_functionblock("statusPropertiesFunctionBlock", infomodel.statusPropertiesFunctionBlock, ditto_topic, deviceId)
    print("Publish Payload: ", payload, " to Topic: ", publishTopic)
    client.publish(publishTopic, payload)
def publishConfigPropertiesFunctionBlock():
    payload = ser.serialize_functionblock("configPropertiesFunctionBlock", infomodel.configPropertiesFunctionBlock, ditto_topic, deviceId)
    print("Publish Payload: ", payload, " to Topic: ", publishTopic)
    client.publish(publishTopic, payload)
def publishEventsAndOperationsFunctionBlock():
    payload = ser.serialize_functionblock("eventsAndOperationsFunctionBlock", infomodel.eventsAndOperationsFunctionBlock, ditto_topic, deviceId)
    print("Publish Payload: ", payload, " to Topic: ", publishTopic)
    client.publish(publishTopic, payload)


# The function that will be executed periodically once the connection to the MQTT broker was established
def periodicAction():
    global next_call

    ### BEGIN READING SENSOR DATA
    
    infomodel.statusPropertiesFunctionBlock.statusValue = {
    	"value" : 0,
    	"unitEnum" : 0
    }
    infomodel.statusPropertiesFunctionBlock.statusBoolean = 0
    infomodel.configPropertiesFunctionBlock.configValue = {
    	"value" : 0,
    	"unitEnum" : 0
    }
    infomodel.configPropertiesFunctionBlock.configBoolean = 0

    ### END READING SENSOR DATA

    # Publish payload
    publishStatusPropertiesFunctionBlock()
    publishConfigPropertiesFunctionBlock()
    publishEventsAndOperationsFunctionBlock()

    # Schedule next call
    next_call = next_call + timePeriod
    threading.Timer(next_call - time.time(), periodicAction).start()

# Initialization of Information Model
infomodel = MySensor.MySensor()

# Create a serializer for the MQTT payload from the Information Model
ser = DittoSerializer.DittoSerializer()

# Timer variable for periodic function
next_call = 0

# Period for publishing data to the MQTT broker in seconds
timePeriod = 10

# Configuration of client ID and publish topic	
publishTopic = "telemetry/" + tenantId + "/" + deviceId

# Output relevant information for consumers of our information
print("Connecting client:    ", clientId)
print("Publishing to topic:  ", publishTopic)

# Create the MQTT client
client = mqtt.Client(clientId)
client.on_connect = on_connect
client.on_message = on_message

client.tls_set(certificatePath)

username = authId + "@" + tenantId
client.username_pw_set(username, device_password)

# Connect to the MQTT broker
client.connect(hub_adapter_host, 8883, 60)

# Blocking call that processes network traffic, dispatches callbacks and
# handles reconnecting.
# Other loop*() functions are available that give a threaded interface and a
# manual interface.
client.loop_start()

while (1):
    pass
