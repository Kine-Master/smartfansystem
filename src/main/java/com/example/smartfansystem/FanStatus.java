package com.example.smartfansystem;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FanStatus {

    private double temperature;
    private double distanceCm;
    private double distanceInch;
    private double current;
    private double electricUsageKWh;

    // Getters and setters
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getDistanceCm() {
        return distanceCm;
    }

    public void setDistanceCm(double distanceCm) {
        this.distanceCm = distanceCm;
    }

    public double getDistanceInch() {
        return distanceInch;
    }

    public void setDistanceInch(double distanceInch) {
        this.distanceInch = distanceInch;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public double getElectricUsageKWh() {
        return electricUsageKWh;
    }

    public void setElectricUsageKWh(double electricUsageKWh) {
        this.electricUsageKWh = electricUsageKWh;
    }
}
