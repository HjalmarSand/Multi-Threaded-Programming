import clock.AlarmClockEmulator;
import clock.io.*;
import clock.io.ClockInput.UserInput;

import java.util.concurrent.Semaphore;

public class ClockMain {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new DisplayTimeThread();
        Thread t2 = new GlueThread();
        t2.start();
        t1.start();
    }
}
