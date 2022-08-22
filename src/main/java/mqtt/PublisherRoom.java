package mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;


public class PublisherRoom {
    public static final String TEMPERATURE_TOPIC = "floor/room/temperature";
    public static final String HUMIDITY_TOPIC = "floor/room/humidity";

    public static void main(String[] args) throws MqttException, InterruptedException {
        MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());

        //LAST WILL
        String willMSG = "Client Disconnected";
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setWill("floor/room/will", willMSG.getBytes(), 2, true);

//        client.connect();
        client.connect(mqttConnectOptions);

        //creating a thread to run multiple publishers
        Thread room = new Thread(() -> {
            while (true) {

                try {
                    //Creating a random number of rooms and temperature
                    int roomNumber = ThreadLocalRandom.current().nextInt(0, 10);
                    double roomTemperature = ThreadLocalRandom.current().nextDouble(15.0, 35.0);

                    String temperatureMsg = "Checking Room " + roomNumber + "\t\t -> Temperature " + new DecimalFormat(".00").format(roomTemperature) + "ºC";
                    MqttMessage temperatureMqttMsg = new MqttMessage(temperatureMsg.getBytes());
                    /**temperature*/
                    client.publish(TEMPERATURE_TOPIC, temperatureMqttMsg);
                    System.out.println(temperatureMsg + "\t\t[Topic -> " + TEMPERATURE_TOPIC + "]");
                    temperatureMqttMsg.setRetained(true);

                    /**humidity*/
                    double roomHumidity = ThreadLocalRandom.current().nextDouble(0.0, 100.0);
                    String humidityMsg = "Humidity " + new DecimalFormat(".00").format(roomHumidity) + "%\n";
                    MqttMessage humidtyMqttMsg = new MqttMessage(humidityMsg.getBytes());
                    client.publish(HUMIDITY_TOPIC, humidtyMqttMsg);
                    humidtyMqttMsg.setRetained(true);

                    System.out.println(humidityMsg);

                    //time to run 1 second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        room.start();
        room.join();
        client.disconnect();
    }
}