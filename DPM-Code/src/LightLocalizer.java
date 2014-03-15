import java.util.ArrayList;

import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.util.Delay;

public class LightLocalizer {
	private Odometer odo;
	private Driver robot;
	private ColorSensor cs;
	private static final int LINE_VALUE = 420;
	private final double d_Light_To_Sensor = 12;
	
	NXTRegulatedMotor leftMotor = Motor.A;
	NXTRegulatedMotor rightMotor = Motor.B;
	
	public static int counter = 0;

	ArrayList<Double> angles = new ArrayList<Double>();
	
	public static double lightValue;
	public static double theta;
	
	public LightLocalizer(Odometer odo, Driver driver, ColorSensor cs) {
		this.odo = odo;
		this.robot = driver;
		this.cs = cs;
		LCD.clear();
		// turn on the light
	}
	
	public void doLocalization() {

	double lastLineTime = System.currentTimeMillis();
	cs.setFloodlight(lejos.robotics.Color.RED);
	cs.calibrateHigh();
	double lv = cs.getNormalizedLightValue() - 125;
	robot.rotate(true);
	while (odo.getTheta() * 180 / Math.PI <= 358){
		try {Thread.sleep(50);} catch (InterruptedException e) {}
		lightValue = cs.getNormalizedLightValue();
		if(cs.getNormalizedLightValue() < lv && ((System.currentTimeMillis() - lastLineTime) > 60)){
				Sound.beep();
				counter++;
				angles.add(odo.getTheta());
				lastLineTime = System.currentTimeMillis();
			}
	}

	robot.stop();

	odo.setY(-d_Light_To_Sensor * Math.cos((angles.get(2)-angles.get(0))/2));
	odo.setX(-d_Light_To_Sensor * Math.cos((angles.get(3)-angles.get(1))/2));
	
	} 
 }