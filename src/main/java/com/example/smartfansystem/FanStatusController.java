package com.example.smartfansystem;

import com.fazecast.jSerialComm.SerialPort;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class FanStatusController {

    private final FanStatus fanStatus = new FanStatus();
    private SerialPort serialPort;
    private Thread serialThread;

    @GetMapping("/fan-status")
    public FanStatus getFanStatus() {
        // Return the latest data (make sure the data is not null or empty)
        return fanStatus;
    }

    @PostMapping("/update-fan-status")
    public void updateFanStatus(@RequestBody FanStatus newStatus) {
        // Update the fan status with the data received from Arduino
        fanStatus.setTemperature(newStatus.getTemperature());
        fanStatus.setDistanceCm(newStatus.getDistanceCm());
        fanStatus.setDistanceInch(newStatus.getDistanceInch());
        fanStatus.setCurrent(newStatus.getCurrent());
        fanStatus.setElectricUsageKWh(newStatus.getElectricUsageKWh());
    }

    @PostConstruct
    public void init() {
        // Initialize serial communication on startup
        serialPort = SerialPort.getCommPorts()[0]; // Ensure the correct COM port is selected
        serialPort.openPort();
        startSerialCommunication();  // Call the method to start reading data from Arduino
    }

    // Method to start reading serial data from the Arduino
    public void startSerialCommunication() {
        startSerialReader();  // Start the thread to read serial data continuously
    }

    private void startSerialReader() {
        serialThread = new Thread(() -> {
            while (true) {
                String data = readSerialData();
                if (!data.isEmpty()) {
                    parseAndUpdateFanStatus(data);
                }
                try {
                    Thread.sleep(1000);  // Read every second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        serialThread.start();
    }

    private void parseAndUpdateFanStatus(String data) {
        // Assuming the Arduino sends data as comma-separated values: "temperature,distance,current,electricUsage"
        String[] parts = data.split(",");
        if (parts.length >= 4) {
            try {
                double temperature = Double.parseDouble(parts[0].trim());
                double distanceCm = Double.parseDouble(parts[1].trim());
                double current = Double.parseDouble(parts[2].trim());
                double electricUsageKWh = Double.parseDouble(parts[3].trim());

                // Update fan status with new values
                fanStatus.setTemperature(temperature);
                fanStatus.setDistanceCm(distanceCm);
                fanStatus.setCurrent(current);
                fanStatus.setElectricUsageKWh(electricUsageKWh);

                System.out.println("Updated Fan Status: " + fanStatus);

            } catch (NumberFormatException e) {
                System.err.println("Error parsing data: " + data);
            }
        } else {
            System.err.println("Received invalid data: " + data);
        }
    }

    private String readSerialData() {
        // Read from the serial port
        try {
            if (serialPort != null && serialPort.isOpen()) {
                if (serialPort.bytesAvailable() > 0) {
                    byte[] data = new byte[serialPort.bytesAvailable()];
                    serialPort.readBytes(data, data.length);
                    return new String(data);
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading from serial port: " + e.getMessage());
        }
        return "";  // Return empty string if no data is available
    }
}
