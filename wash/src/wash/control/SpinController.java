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


            while (true) {

                // wait for up to a (simulated) minute for a WashingMessage
                WashingMessage m = receiveWithTimeout(60000 / Settings.SPEEDUP);

                // if m is null, it means a minute passed and no message was received
                if (m != null) {
                    System.out.println("entering");
                    if(m.getOrder() == WashingMessage.Order.SPIN_SLOW) {
                        System.out.println(1);
                        while(true) {
                            ourIO.setSpinMode(WashingIO.SPIN_LEFT);
                            System.out.println("left");
                            Thread.sleep(60000 / Settings.SPEEDUP);
                            ourIO.setSpinMode(WashingIO.SPIN_RIGHT);
                            System.out.println("right");
                            Thread.sleep(60000 / Settings.SPEEDUP);
                        }
                    } else if(m.getOrder() == WashingMessage.Order.SPIN_FAST) {
                        System.out.println(2);
                        ourIO.setSpinMode(WashingIO.SPIN_FAST);
                    } else if(m.getOrder() == WashingMessage.Order.SPIN_OFF) {
                        System.out.println(3);
                        ourIO.setSpinMode(WashingIO.SPIN_IDLE);
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
