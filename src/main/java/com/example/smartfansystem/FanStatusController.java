package com.example.smartfansystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class FanStatusController {

    private final FanStatus fanStatus = new FanStatus();

    @GetMapping("/fan-status")
    public FanStatus getFanStatus() {
        // Assume we get the latest data from Arduino (this can be fetched from serial or other source)
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
}
