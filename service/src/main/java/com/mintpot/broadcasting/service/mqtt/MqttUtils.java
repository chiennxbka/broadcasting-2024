package com.mintpot.broadcasting.service.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@Slf4j
public class MqttUtils {

    public static void sendMsgToQueue(MqttClient mqttClient, ObjectMapper objectMapper, Object object, String imei, String topic) {
        try {
            String jsonString = objectMapper.writeValueAsString(object);
            log.info(String.format("---- Message: %s, imei: %s", jsonString, imei));
            MqttMessage message = new MqttMessage(jsonString.getBytes());
            message.setQos(2);
            mqttClient.publish(topic.concat(imei), message);
        } catch (MqttException | JsonProcessingException e) {
            log.error(String.format("---- Occur server error: %s", e.getMessage()));
        }
    }
}
