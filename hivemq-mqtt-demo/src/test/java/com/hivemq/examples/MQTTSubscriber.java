package com.hivemq.examples;


import java.util.concurrent.CountDownLatch;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * This is a demo MQTT subscriber class for connecting to broker 
 * and subscribing to a topic
 */
public class MQTTSubscriber {

	public static void main(String[] args) {

        
        // Broker derail
        String broker       = "tcp://broker.hivemq.com:1883";
        
        // Client details
        String clientId     = "democlient";
        
        // Message details
        String topic        = "tempstate";
        int qos = 0;
        
        // Latch used for synchronizing b/w threads
        final CountDownLatch latch = new CountDownLatch(1);

        try 
        {
        	// Initializing mqtt client for a new connection
            MqttClient subscriberClient = new MqttClient(broker, clientId);
            MqttConnectOptions connOpts1 = new MqttConnectOptions();
            
            //Setting up clean session
            connOpts1.setCleanSession(true);
            
            //Connecting to mqtt broker
            subscriberClient.connect(connOpts1);
            System.out.println("Connected to broker: "+broker);
              
            
            // Callback - for receiving messages
            subscriberClient.setCallback(new MqttCallback() 
            {

                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    // Called when a message arrives from the server that
                    // matches any subscription made by the client
  
                    System.out.println("\nReceived a Message!" +
                            "\n\tTopic:   " + topic + 
                            "\n\tMessage: " + new String(message.getPayload()) + 
                            "\n\tQoS:     " + message.getQos() + "\n");
                    latch.countDown(); // unblock main thread
                }

                public void connectionLost(Throwable cause) {
                    System.out.println("Connection lost!" + cause.getMessage());
                    latch.countDown();
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                }

            });
            
            // Subscribing  to the topic 
            subscriberClient.subscribe(topic, qos);
            System.out.println("Subscribed to topic from hiveMQ broker. Waiting for new message ..");
   

            // Wait for the message to be received
            try 
            {              
                latch.await(); // block here until message received, and latch will flip
            } catch (InterruptedException e) {
                System.out.println("I was awoken while waiting");
            }
            

            
            // Disconnect the client
            subscriberClient.disconnect();
            System.out.println("Disconnecting from Broker!!");
            
            // Closing the client
            subscriberClient.close();
   
            
        } catch(MqttException me) {
            System.out.println("Exception reason "+me.getReasonCode());
            System.out.println("Exception msg "+me.getMessage());
            System.out.println("Exception loc "+me.getLocalizedMessage());
            System.out.println("Exception cause "+me.getCause());
            System.out.println("Exception "+me);
            me.printStackTrace();
        }
    }

}
