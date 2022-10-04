package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class TemperatureController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    WashingIO ourIO;
    int dt = 10;
    double heatPerTime = 0.0478 * dt;


    public TemperatureController(WashingIO io) {
        // TODO
        ourIO = io;
    }

    @Override
    public void run() {
        // TODO
        try {
            // ... TODO ...


            while (true) {

                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Settings.SPEEDUP);

                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                    System.out.println("entering");
                    if(m.getOrder() == WashingMessage.Order.TEMP_IDLE) {
                        ourIO.heat(false);
                    } else if(m.getOrder() == WashingMessage.Order.TEMP_SET_40) {
                        while(true) {
                            if (ourIO.getTemperature() < 40 - heatPerTime) {
                                ourIO.heat(true);
                            } else {
                                ourIO.heat(false);
                            }
                            Thread.sleep(10000);
                        }
                    } else if(m.getOrder() == WashingMessage.Order.TEMP_SET_60) {
                        while(true) {
                            if (ourIO.getTemperature() < 60 - heatPerTime) {
                                ourIO.heat(true);
                            } else {
                                ourIO.heat(false);
                            }
                            Thread.sleep(10000);
                        }
                    }

                    m.getSender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                }

                // ... TODO ...
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
