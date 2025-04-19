import processing.serial.*;

Serial myPort;
String data = "";
float angle = 0, distance = 0;
int maxDistance = 150; // Maximum sonar detection range (in cm)

void setup() {
  size(600, 600);
  
  // Set the correct serial port
  myPort = new Serial(this, "/dev/cu.usbmodem1301", 9600);
  myPort.bufferUntil('\n');
  
  // Debug: Print to check if the port is open
  println("Connected to: /dev/cu.usbmodem1301");
}

void draw() {
  background(0);  
  translate(width / 2, height - 50);  

  // Draw Radar Arc (Distance Circles)
  noFill();
  stroke(0, 255, 0);
  for (int r = 50; r <= maxDistance; r += 50) {
    ellipse(0, 0, r * 2, r * 2);
  }

  // Draw Sonar Line
  float mappedDistance = map(distance, 0, maxDistance, 0, width / 2);
  strokeWeight(3);
  line(0, 0, cos(radians(angle)) * mappedDistance, -sin(radians(angle)) * mappedDistance);

  // Display Angle & Distance
  fill(255);
  textSize(18);
  text("Angle: " + angle + "°", -280, 250);
  text("Distance: " + distance + " cm", -280, 270);
}

void serialEvent(Serial myPort) {
  data = myPort.readStringUntil('\n');  
  if (data != null) {
    println("Received: " + data);  // Debugging line to check incoming data
    data = trim(data);
    String[] values = split(data, ",");
    if (values.length == 2) {
      angle = float(values[0]);  
      distance = float(values[1]);  
      if (distance > maxDistance) {
        distance = maxDistance;
      }
    }
  }
}