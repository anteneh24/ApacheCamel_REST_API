package com.example.first.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "javaGuide",groupId = "myGroup")
    public void consumer(String message){
        logger.info(String.format("thie is consumed -->"+message));
    }

}
