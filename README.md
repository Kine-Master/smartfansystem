Responsive Smart Fan System
Overview
The Responsive Smart Fan System is an Arduino-based project designed to control a fan based on temperature, distance, and electrical usage. The system utilizes various sensors to monitor the environment, including:

LM35 temperature sensor (for temperature measurement)
HC-SR04 ultrasonic sensor (for proximity detection)
ACS712T-20A current sensor (to monitor current consumption and calculate energy usage)
The system also includes a web interface that allows users to view live data, set thresholds, and monitor the fan's operational history. Data is sent from the Arduino to a laptop via USB, and the backend uses Spring Boot to manage data storage and communication.

Features
Real-time monitoring of temperature, proximity, and current sensor data.
Option to convert temperature between Celsius and Fahrenheit.
Option to convert ultrasonic distance between centimeters and inches.
Control of fan based on thresholds set for temperature and proximity.
Historical tracking of fan status (on/off) with timestamp.
Calculation of energy consumption in kilowatt-hours (kWh) based on current sensor readings.
Hardware Components
Arduino Uno R3
LM35 Temperature Sensor
HC-SR04 Ultrasonic Sensor
ACS712T-20A Current Sensor
Relay Module (to control the fan)
Clip Fan
Software Components
Arduino IDE for writing the code and uploading it to the Arduino board.
Spring Boot for backend development and database handling.
MySQL for storing historical data.
HTML/CSS/JavaScript for the frontend user interface.
System Architecture
The system consists of two major components:

Arduino (Client): Collects data from sensors and communicates it to the backend via USB.
Spring Boot Application (Server): Receives data from the Arduino, processes it, stores it in the database, and serves data to the frontend.
Backend:
Spring Boot is used to create a REST API that communicates with the frontend.
MySQL is used for storing historical data of the fan's status.
Frontend:
HTML/CSS/JavaScript is used to create a responsive interface.
The user can control thresholds for temperature and proximity, as well as view the fan status and history.
Installation
Prerequisites
Arduino IDE: Download from Arduino's website.
Spring Boot: Ensure you have Java and Gradle installed.
MySQL: Install MySQL server and set up a database for the project.
Step 1: Set Up Arduino
Connect the LM35, HC-SR04, ACS712T-20A, and Relay Module to your Arduino Uno R3 according to the wiring diagram.
Upload the Arduino code to the board using the Arduino IDE.
Arduino Code:
cpp
Copy code
#include <SoftwareSerial.h>

// Pin Definitions
const int lm35Pin = A0;  // LM35 Temperature sensor pin
const int trigPin = 9;   // HC-SR04 Trigger pin
const int echoPin = 10;  // HC-SR04 Echo pin
const int relayPin = 8;  // Relay pin for controlling the fan
const int currentPin = A1;  // ACS712 Current Sensor pin

// Constants
const float voltageRef = 5.0;
const float currentSensorOffset = 2.5;  // Midpoint voltage (for ACS712)
const float voltageToCurrent = 0.185;  // ACS712 conversion factor

// Variables
float temperature = 0.0;
float distance = 0.0;
float currentReading = 0.0;
float energyConsumption = 0.0;
bool fanStatus = false;

void setup() {
Serial.begin(9600);
pinMode(trigPin, OUTPUT);
pinMode(echoPin, INPUT);
pinMode(relayPin, OUTPUT);
pinMode(currentPin, INPUT);

digitalWrite(relayPin, LOW);  // Initialize the fan as OFF
}

void loop() {
// Read the temperature (in Celsius) from the LM35 sensor
temperature = analogRead(lm35Pin) * (voltageRef / 1024.0) * 100.0;

// Read the distance from the HC-SR04 ultrasonic sensor
long duration, distanceCm;
digitalWrite(trigPin, LOW);
delayMicroseconds(2);
digitalWrite(trigPin, HIGH);
delayMicroseconds(10);
digitalWrite(trigPin, LOW);
duration = pulseIn(echoPin, HIGH);
distanceCm = (duration / 2) * 0.0344;

// Read the current from the ACS712 sensor
int currentSensorValue = analogRead(currentPin);
float currentVoltage = currentSensorValue * (voltageRef / 1024.0);
currentReading = (currentVoltage - currentSensorOffset) / voltageToCurrent;

// Calculate energy consumption (in kilowatt-hours)
energyConsumption = (currentReading * 5.0 / 1000.0);  // Simple model

// Display data on Serial Monitor
Serial.print("Temperature: ");
Serial.print(temperature);
Serial.print(" °C, Distance: ");
Serial.print(distanceCm);
Serial.print(" cm, Current: ");
Serial.print(currentReading);
Serial.print(" A, Energy Consumption: ");
Serial.print(energyConsumption);
Serial.println(" kWh");

// Control Fan (based on thresholds)
if (temperature > 30 && distanceCm < 100) {
digitalWrite(relayPin, HIGH);  // Turn on the fan
fanStatus = true;
} else {
digitalWrite(relayPin, LOW);   // Turn off the fan
fanStatus = false;
}

delay(1000);  // Delay for 1 second
}
The Arduino code will read data from the sensors and send it to the laptop via USB.
Step 2: Set Up Spring Boot Project
Clone the project repository:
bash
Copy code
git clone https://github.com/yourusername/smart-fan-system.git
Navigate to the project directory:
bash
Copy code
cd smart-fan-system
Open the project in your favorite IDE (e.g., IntelliJ IDEA, Eclipse, etc.).
Modify the application.properties file to configure your MySQL database credentials:
properties
Copy code
spring.datasource.url=jdbc:mysql://localhost:3306/smart_fan_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
Run the Spring Boot application using Gradle:
bash
Copy code
./gradlew bootRun
Your backend will be accessible at http://localhost:8080.
Step 3: Set Up Frontend
The frontend files are included in the resources/static folder within the project.
Open the index.html file in your browser to access the web interface.
Usage
Web Interface
Fan Status: Displays real-time data from the Arduino board, including temperature, proximity, and fan status.
Threshold Control: Allows you to change the temperature and proximity thresholds for fan activation.
History: Displays a table of past fan status records, including temperature, distance, fan status, and timestamp.
Fan Control
The fan will turn ON or OFF based on the threshold values set for temperature and proximity.
The system also tracks energy usage, calculated from the current sensor, and converts the usage into kilowatt-hours.
Data Conversion
Temperature Conversion: You can toggle between Celsius and Fahrenheit.
Distance Conversion: You can toggle between centimeters and inches for the ultrasonic sensor readings.
