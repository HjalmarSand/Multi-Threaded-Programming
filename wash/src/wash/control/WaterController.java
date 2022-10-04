package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class WaterController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    WashingIO ourIO;

    public WaterController(WashingIO io) {
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
                    if(m.getOrder() == WashingMessage.Order.WATER_IDLE) {
                        ourIO.fill(false);
                        ourIO.drain(false);
                    } else if(m.getOrder() == WashingMessage.Order.WATER_FILL) {
                        while(true) {
                            if (ourIO.getWaterLevel() <= 10) {
                                System.out.println("filling");
                                ourIO.fill(true);
                                ourIO.drain(false);
                            } else {
                                System.out.println("not filling");
                                ourIO.fill(false);
                            }
                            Thread.sleep(10000 / Settings.SPEEDUP);
                        }
                    } else if(m.getOrder() == WashingMessage.Order.WATER_DRAIN) {
                        ourIO.drain(true);
                        ourIO.fill(false);
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
