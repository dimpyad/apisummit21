package com.hivemq.examples;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

/**
 * This is a demo MQTT publisher class for connecting to a broker 
 * and publishing to a topic
 */
public class MQTTPublisher {
	public static void main(String[] args) throws Exception {

        // Broker and client details
        final String broker   = "tcp://broker.hivemq.com";    
        final String clientId    = "my_mqtt_java_client";

        final String username    = "demo";
        final String password    = "democlienttest";
        
        // Topic details
        final String topic      = "99";

        // MQTT connection options
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setPassword(password.toCharArray());

        // connect the client to HiveMQ broker
        MqttClient publisherClient = new MqttClient(broker, clientId);
        publisherClient.connect(options);
        System.out.println("Connected to broker: "+broker);

        // set device's hardware information
        publisherClient.publish("tempstate", topic.getBytes(), 2, false);
        System.out.println("Published on topic = " + topic);
        publisherClient.disconnect();
        publisherClient.close();
        
    }

}
