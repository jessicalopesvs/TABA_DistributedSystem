package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

public class SubscriberRoom {
    public static void main(String[] args) throws MqttException {

        //Setting the MQTT server url and client
        MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
        //setting the callback
        client.setCallback(new SubCallback());
        client.connect();

        //different subscribers
        client.subscribe("floor/room/#");
    }
}
