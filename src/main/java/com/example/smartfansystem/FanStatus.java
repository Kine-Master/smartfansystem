package com.example.smartfansystem;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FanStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double temperature;
    private double distanceCm;
    private double distanceInch;
    private double current;
    private double electricUsageKWh;
}
