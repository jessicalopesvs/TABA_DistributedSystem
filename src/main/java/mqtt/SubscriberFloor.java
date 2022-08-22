package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class SubscriberFloor {
    public static void main(String[] args) throws MqttException {

        //Setting the MQTT server url and client
        MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());

        //LAST WILL
        String willMSG = "Client Disconnected";
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setWill("floor/room/will", willMSG.getBytes(), 2, true);



        //setting the callback
        client.setCallback(new SubCallback());

        //connecting client
        client.connect();

        //different subscribers
        client.subscribe("floor/+/id");
        client.subscribe("floor/+/location");
        client.subscribe("/floor/Lost");

    }
}
