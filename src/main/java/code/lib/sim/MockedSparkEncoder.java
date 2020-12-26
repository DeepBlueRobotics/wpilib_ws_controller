package code.lib.sim;

import com.cyberbotics.webots.controller.PositionSensor;

import org.team199.wpiws.devices.SimDeviceSim;

public class MockedSparkEncoder implements Runnable {
    private String name;
    private SimDeviceSim encoder;
    private PositionSensor webotsEncoder;
    // Default value for a CANEncoder
    private final int countsPerRevolution = 4096;
    private double position;

    public MockedSparkEncoder(String name) {
        this.name = name;
        // Match motor on CAN 0 with channels [0, 1], CAN 1 to channels [2, 3], etc.
        // Probably not the best way to do it but it works
        encoder = new SimDeviceSim(name);
        webotsEncoder = Simulation.getRobot().getMotor(name.substring(11)).getPositionSensor();
        if(webotsEncoder != null) {
            webotsEncoder.enable(BaseSimConfig.getSensorTimestep());
            Simulation.registerPeriodicMethod(this);
        }
    }

    public double getPosition() {
        return position;
    }

    @Override
    public void run() {
        // Get the position of the Webots encoders and set the position of the WPIlib encoders 
        // getValue() returns radians
        // revoultions = radians * gearing / pi
        double revolutions = (webotsEncoder.getValue() * BaseSimConfig.getMotorGearing(name)) / Math.PI;
        int count = (int) Math.floor(revolutions * countsPerRevolution);
        encoder.set("count", "" + count);
    }
}