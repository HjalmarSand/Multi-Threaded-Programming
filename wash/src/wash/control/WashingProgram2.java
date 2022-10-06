package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

import static wash.control.WashingMessage.Order.*;


public class WashingProgram2 extends ActorThread<WashingMessage> {

    private WashingIO io;
    private ActorThread<WashingMessage> temp;
    private ActorThread<WashingMessage> water;
    private ActorThread<WashingMessage> spin;
    public WashingProgram2(WashingIO io,
                           ActorThread<WashingMessage> temp,
                           ActorThread<WashingMessage> water,
                           ActorThread<WashingMessage> spin) {
        this.io = io;
        this.temp = temp;
        this.water = water;
        this.spin = spin;
    }

    @Override
    public void run() {
        try {
            io.lock(true);
            //Program 2 (white wash): Like program 1, but with a 20 minute pre­wash in 40◦C.
            // The main wash (30 minutes) is to be performed in 60◦C. Between the pre­wash and
            // the main wash, the water in the barrel is
            //drained and replaced with new, clean water.

            water.send(new WashingMessage(this, WATER_FILL));
            receive();
            temp.send(new WashingMessage(this, TEMP_SET_40));
            receive();
            spin.send(new WashingMessage(this, SPIN_SLOW));
            receive();
            Thread.sleep(1200000 / Settings.SPEEDUP);

            spin.send(new WashingMessage(this, SPIN_OFF));
            receive();
            temp.send(new WashingMessage(this, TEMP_IDLE));
            receive();
            water.send(new WashingMessage(this, WATER_DRAIN));
            receive();
            water.send(new WashingMessage(this, WATER_FILL));
            receive();
            temp.send(new WashingMessage(this, TEMP_SET_60));
            receive();
            spin.send(new WashingMessage(this, SPIN_SLOW));
            receive();
            Thread.sleep(1800000 / Settings.SPEEDUP);

            temp.send(new WashingMessage(this, TEMP_IDLE));
            receive();
            spin.send(new WashingMessage(this, SPIN_OFF));
            receive();
            water.send(new WashingMessage(this, WATER_DRAIN));
            receive();
            for (int i = 0; i < 5; i++) {
                System.out.println("Rinsing" + i);
                water.send(new WashingMessage(this, WATER_FILL));
                receive();
                spin.send(new WashingMessage(this, SPIN_SLOW));
                receive();
                Thread.sleep(120000 / Settings.SPEEDUP);
                spin.send(new WashingMessage(this, SPIN_OFF));
                receive();
                water.send(new WashingMessage(this, WATER_DRAIN));
                receive();
            }
            spin.send(new WashingMessage(this, SPIN_FAST));
            receive();
            Thread.sleep(300000 / Settings.SPEEDUP);
            spin.send(new WashingMessage(this, SPIN_OFF));
            receive();


            io.lock(false);
        } catch (InterruptedException e) {
            temp.send(new WashingMessage(this, TEMP_IDLE));
            water.send(new WashingMessage(this, WATER_IDLE));
            spin.send(new WashingMessage(this, SPIN_OFF));
            System.out.println("washing program terminated");
        }

    }
}
