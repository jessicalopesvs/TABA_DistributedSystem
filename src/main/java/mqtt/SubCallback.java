package mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Date;

public class SubCallback implements MqttCallback {
    public void connectionLost(Throwable throwable) {
        System.out.println("connection lost");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) {
        System.out.println("[" + new Date().toGMTString() + "] message received:\t" + new String(mqttMessage.getPayload()));
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    }
}
