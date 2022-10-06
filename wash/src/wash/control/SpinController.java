package wash.control;

import actor.ActorThread;
import wash.io.WashingIO;

public class SpinController extends ActorThread<WashingMessage> {

    // TODO: add attributes
    WashingIO ourIO;

    public SpinController(WashingIO io) {
        // TODO
        ourIO = io;
    }

    @Override
    public void run() {
        try {
            // ... TODO ...

            WashingMessage ourMessage = new WashingMessage(this, WashingMessage.Order.SPIN_OFF);
            boolean isSpinningLeft = false;
            boolean sent = false;

            while (true) {

                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Settings.SPEEDUP);

                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                    ourMessage = m;
                    sent = true;
                }


                if(ourMessage.getOrder() == WashingMessage.Order.SPIN_SLOW)  {
                    if (isSpinningLeft) {
                        ourIO.setSpinMode(WashingIO.SPIN_RIGHT);
                        isSpinningLeft = false;
                    } else {
                        ourIO.setSpinMode(WashingIO.SPIN_LEFT);
                        isSpinningLeft = true;
                    }
                }  else if(ourMessage.getOrder() == WashingMessage.Order.SPIN_FAST) {
                    ourIO.setSpinMode(WashingIO.SPIN_FAST);
                } else if(ourMessage.getOrder() == WashingMessage.Order.SPIN_OFF) {
                    ourIO.setSpinMode(WashingIO.SPIN_IDLE);
                }
                if (m != null) {
                    m.getSender().send(new WashingMessage(this, WashingMessage.Order.ACKNOWLEDGMENT));
                }
            }
        } catch (InterruptedException unexpected) {
            // we don't expect this thread to be interrupted,
            // so throw an error if it happens anyway
            throw new Error(unexpected);
        }
    }
}
