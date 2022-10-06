package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class TemperatureController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    WashingIO ourIO;
    int dt = 10;
    double upperConstant = 0.0478 * dt;
    double lowerConstant  = dt * 9.52 / 1000;



    public TemperatureController(WashingIO io) {
        // TODO
        ourIO = io;
    }

    @Override
    public void run() {
        // TODO

        boolean sent = false;
        try {
            // ... TODO ...

            WashingMessage ourMessage = new WashingMessage(this, WashingMessage.Order.TEMP_IDLE);
            while (true) {

                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(dt * 1000 / Settings.SPEEDUP);

                if (m != null) {
                    ourMessage = m;
                    sent = false;
                }
                // if m is null, it means a minute passed and no message was received
                if(ourMessage.getOrder() == WashingMessage.Order.TEMP_IDLE) {
                    ourIO.heat(false);
                    if (m != null) {
                        ourMessage.getSender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                    }
                } else if(ourMessage.getOrder() == WashingMessage.Order.TEMP_SET_40) {
                    if (ourIO.getTemperature() < 38 + lowerConstant) {
                        ourIO.heat(true);
                    } else if (ourIO.getTemperature() > 40 - upperConstant){
                        ourIO.heat(false);
                        if(!sent) {
                            ourMessage.getSender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                            sent = true;
                        }
                    }
                } else if(ourMessage.getOrder() == WashingMessage.Order.TEMP_SET_60) {
                    if (ourIO.getTemperature() < 58 + lowerConstant) {
                        ourIO.heat(true);
                    } else if (ourIO.getTemperature() > 60 - upperConstant){
                        ourIO.heat(false);
                        if(!sent) {
                            ourMessage.getSender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                            sent = true;
                        }
                    }
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
