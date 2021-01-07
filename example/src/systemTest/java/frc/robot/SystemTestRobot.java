package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.DriverStationSim;

public class SystemTestRobot extends Robot {

    public static void main(String... args) {
        RobotBase.startRobot(SystemTestRobot::new);
    }

    @Override
    public void startCompetition() {
        // Ensure that uncaught exceptions result in a non-zero exit status
        // so that failures can be detected by the caller (e.g. gradlew systemTest)
        try {
            super.startCompetition();
            HAL.shutdown();
            System.exit(0);
        } catch (Throwable throwable) {
            Throwable cause = throwable.getCause();
            if (cause != null) {
                throwable = cause;
            }
            DriverStation.reportError("Unhandled exception: " + throwable.toString(),
                throwable.getStackTrace());
            HAL.shutdown();
            System.exit(-1);
        }      
    }

    @Override
    public void simulationInit() {
        // Simulate starting autonomous
        DriverStationSim.setAutonomous(true);
        DriverStationSim.setEnabled(true);
        DriverStationSim.notifyNewData();

        super.simulationInit();
    }

    private int count = 0;

    @Override
    public void simulationPeriodic() {
        super.simulationPeriodic();

        // NOTE: System.err output will be displayed by gradlew systemTest
        // but System.out output will be put in a log file. We use System.err
        // here so we can see what's going on as it happens.
        System.err.println(count);
        count++;
        if (count > 50*10) {
            // Simulate disabling the robot
            DriverStationSim.setEnabled(false);
            DriverStationSim.notifyNewData();

            // Call endCompetition() to end the test and report success.
            // NOTE: throwing an exception will end the test and report failure.
            endCompetition();
        }
    }
}
