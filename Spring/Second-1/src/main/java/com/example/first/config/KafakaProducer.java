package com.example.first.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafakaProducer {

    private static Logger logger = LoggerFactory.getLogger(KafakaProducer.class);

    private KafkaTemplate<String,String> template ;

    public KafakaProducer(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    public void sendMessage(String message){
        logger.info(String.format("this is the message -> ",message));
        template.send("javaGuide",message);
    }
}
