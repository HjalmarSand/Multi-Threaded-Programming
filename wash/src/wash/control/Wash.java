package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;
import wash.simulation.WashingSimulator;

public class Wash {

    public static void main(String[] args) throws InterruptedException {
        WashingSimulator sim = new WashingSimulator(Settings.SPEEDUP);
        
        WashingIO io = sim.startSimulation();

        TemperatureController temp = new TemperatureController(io);
        WaterController water = new WaterController(io);
        SpinController spin = new SpinController(io);

        temp.start();
        water.start();
        spin.start();

        ActorThread<WashingMessage> washingProgram = null;
        boolean machineReady = true;

        while (true) {
            int n = io.awaitButton();
            System.out.println("user selected program " + n);

            if (machineReady) {
                if (n == 1) {
                    washingProgram = new WashingProgram1(io, temp, water, spin);
                    washingProgram.start();
                } else if (n == 2) {
                    washingProgram = new WashingProgram2(io, temp, water, spin);
                    washingProgram.start();
                } else if(n == 3) {
                    washingProgram = new WashingProgram3(io, temp, water, spin);
                    washingProgram.start();
                }
                machineReady = false;
            }

            if (n == 0) {
                if (!(washingProgram == null)) {
                    washingProgram.interrupt();
                }
                machineReady = true;
            }

            // TODO:

            // if the user presses buttons 1-3, start a washing program
            // if the user presses button 0, and a program has been started, stop it
        }
    }
};
