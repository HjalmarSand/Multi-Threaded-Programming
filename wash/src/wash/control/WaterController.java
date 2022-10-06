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

            boolean sent = false;

            WashingMessage ourMessage = new WashingMessage(this, WashingMessage.Order.WATER_IDLE);
            while (true) {

                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(10000 / Settings.SPEEDUP);

                if (m != null) {
                    ourMessage = m;
                    sent = false;
                }
                // if m is null, it means a minute passed and no message was received


                if(ourMessage.getOrder() == WashingMessage.Order.WATER_IDLE) {
                    ourIO.fill(false);
                    ourIO.drain(false);
                    if(m != null) {
                        ourMessage.getSender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                    }
                } else if(ourMessage.getOrder() == WashingMessage.Order.WATER_FILL) {
                    if (ourIO.getWaterLevel() < 10) {
                        ourIO.drain(false);
                        ourIO.fill(true);
                    } else {
                        ourIO.fill(false);
                        if(!sent) {
                            ourMessage.getSender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                            sent = true;
                        }
                    }
                } else if(ourMessage.getOrder() == WashingMessage.Order.WATER_DRAIN) {
                    ourIO.fill(false);
                    ourIO.drain(true);
                    if(!sent && ourIO.getWaterLevel() == 0) {
                        ourMessage.getSender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                        sent = true;
                    }
                }
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
