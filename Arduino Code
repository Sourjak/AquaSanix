#include <DHT.h>
#include <Servo.h>
#include <math.h>

// Define sensor types and pins
#define DHTPIN 2       // AM2301 (DHT21) Data pin connected to Digital Pin 2
#define DHTTYPE DHT21  // AM2301 is same as DHT21
#define THERMISTOR_PIN A0  // NTC Thermistor connected to Analog Pin A0
#define SERVO_PIN 6    // Servo Motor connected to Digital Pin 6
#define TRIG_PIN 9     // Ultrasonic Sensor Trigger Pin
#define ECHO_PIN 10    // Ultrasonic Sensor Echo Pin

// Constants for 10k NTC Thermistor
#define SERIES_RESISTOR 10000  // 10kΩ fixed resistor
#define NOMINAL_RESISTANCE 10000  // Resistance at 25°C
#define NOMINAL_TEMPERATURE 25   // Reference temperature (Celsius)
#define B_COEFFICIENT 3950  // Beta value (Check your thermistor’s datasheet)

DHT dht(DHTPIN, DHTTYPE);  // Initialize AM2301 sensor
Servo servo;  // Create a Servo object

void setup() {
    Serial.begin(9600);  // Start Serial Monitor
    dht.begin();         // Start AM2301 sensor
    servo.attach(SERVO_PIN);  // Attach the servo motor
    pinMode(TRIG_PIN, OUTPUT);
    pinMode(ECHO_PIN, INPUT);
}

void loop() {
    // Read temperature and humidity from AM2301
    float temperature_dht = dht.readTemperature();  // Read temperature in Celsius
    float humidity = dht.readHumidity();  // Read humidity in %

    // Check if readings from AM2301 are valid
    if (isnan(temperature_dht) || isnan(humidity)) {
        Serial.println("Failed to read from AM2301 sensor!");
    } else {
        Serial.print("DHT21 Temperature: ");
        Serial.print(temperature_dht);
        Serial.print("°C | Humidity: ");
        Serial.print(humidity);
        Serial.println("%");
    }

    // Read and calculate temperature from NTC Thermistor
    int adcValue = analogRead(THERMISTOR_PIN);  // Read analog value
    float resistance = SERIES_RESISTOR / ((1023.0 / adcValue) - 1);

    // Apply Steinhart-Hart equation
    float temperature_ntc;
    temperature_ntc = resistance / NOMINAL_RESISTANCE;  // (R/R0)
    temperature_ntc = log(temperature_ntc);  // ln(R/R0)
    temperature_ntc /= B_COEFFICIENT;  // (1/B) * ln(R/R0)
    temperature_ntc += 1.0 / (NOMINAL_TEMPERATURE + 273.15);  // + (1/T0)
    temperature_ntc = 1.0 / temperature_ntc;  // Invert
    temperature_ntc -= 273.15;  // Convert to Celsius

    // Print NTC Thermistor Temperature
    Serial.print("NTC Thermistor Temperature: ");
    Serial.print(temperature_ntc);
    Serial.println("°C");

    // Read Distance from Ultrasonic Sensor (HC-SR04)
    long duration;
    float distance;

    digitalWrite(TRIG_PIN, LOW);
    delayMicroseconds(2);
    digitalWrite(TRIG_PIN, HIGH);
    delayMicroseconds(10);
    digitalWrite(TRIG_PIN, LOW);
    
    duration = pulseIn(ECHO_PIN, HIGH);
    distance = (duration * 0.034) / 2;  // Convert to cm

    Serial.print("Distance: ");
    Serial.print(distance);
    Serial.println(" cm");

    // Move Servo Motor based on distance
    if (distance < 10) {  // If object is closer than 10 cm
        servo.write(90);  // Move servo to 90 degrees
        Serial.println("Servo moved to 90°");
    } else {
        servo.write(0);  // Move servo back to 0 degrees
        Serial.println("Servo moved to 0°");
    }

    delay(2000);  // Wait 2 seconds before next reading
}
