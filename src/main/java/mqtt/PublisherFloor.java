/*
@author Jéssica Lopes
x21147477
 */

package mqtt;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PublisherFloor {

    //TOPICS
    public static final String LIGHT_TOPIC = "floor/light/id";
    public static final String WINDOW_TOPIC = "floor/window/location";


    //constant lists to randomize
    public static final String[] FLOOR_NUM = {"First Floor", "Second Floor", "Third Floor", "Fourth Floor"};
    public static final String[] LIGHT_STATUS = {"ON", "OFF"};
    public static final String[] LIGHT_ID = {"CEILING", "SIDE LAMP"};
    public static final String[] WINDOW_NAME = {"WINDOW 1", "WINDOW 2", "WINDOW 3", "WINDOW 4"};
    public static final String[] WINDOW_STATUS = {"OPEN", "CLOSED"};


    public static void main(String[] args) throws MqttException {

        //Setting the MQTT server url and client
        MqttClient client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
        client.connect();//connecting client to server

        //creating a thread to run multiple publishers

        Thread floor = new Thread(() -> {
            while (true) {
                //Messages published every 0,5 second
                //generating thread safe random numbers -> each time the loop runs, a random element will be generated
                int rndFloor = ThreadLocalRandom.current().nextInt(FLOOR_NUM.length);
                int rndLightStatus = ThreadLocalRandom.current().nextInt(LIGHT_STATUS.length);
                int rndLightID = ThreadLocalRandom.current().nextInt(LIGHT_ID.length);
                int rndWindows = new Random().nextInt(WINDOW_NAME.length);
                int rndWindowStatus = new Random().nextInt(WINDOW_STATUS.length);

                /** LIGHTS **/
                //setting up messages
                String FloorMsg = "-----Checking " + FLOOR_NUM[rndFloor] + " -----";
                MqttMessage floors = new MqttMessage(FloorMsg.getBytes());

                String lightMsg = "Status: " + LIGHT_STATUS[rndLightStatus];
                MqttMessage light = new MqttMessage(lightMsg.getBytes());
        light.setRetained(true);

                //ID
                String idMsg = "The lights at " + LIGHT_ID[rndLightID];
                MqttMessage id = new MqttMessage(idMsg.getBytes());
        id.setRetained(true);

                /**WINDOW*/
                String windowIDMsg = WINDOW_NAME[rndWindows] + " checked";
                MqttMessage windows = new MqttMessage(windowIDMsg.getBytes());

                String windowStatus = "Window status: " + WINDOW_STATUS[rndWindowStatus];
                MqttMessage windowSt = new MqttMessage(windowStatus.getBytes());

                try {
                    //light messages
                    client.publish(LIGHT_TOPIC, floors);
                    System.out.println(FloorMsg);

                    client.publish(LIGHT_TOPIC, id);
                    System.out.println(idMsg);

                    client.publish(LIGHT_TOPIC, light);
                    System.out.println(lightMsg);

                    //window messages

                    client.publish(WINDOW_TOPIC, windows);
                    System.out.println(windowIDMsg);

                    client.publish(WINDOW_TOPIC, windowSt);
                    System.out.println(windowStatus);

                    //time to run 0,5 milliseconds
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });
        floor.start();

        try {
            floor.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.disconnect();
    }
}
