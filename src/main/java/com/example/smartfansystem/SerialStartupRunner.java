package com.example.smartfansystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class SerialStartupRunner {

    @Autowired
    private FanStatusController fanStatusController;

    @PostConstruct
    public void init() {
        // Call the method to start the serial communication
        fanStatusController.startSerialCommunication();
    }
}
