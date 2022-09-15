package clock.io;

import java.time.Clock;

public class GlueThread extends Thread {

    @Override
    public void run(){
        while (true) {
            try {
                ClockDataTransmitter.getSemaphore().acquire();
                ClockDataTransmitter.getSignalSem().acquire();
                ClockInput.UserInput userInput = ClockDataTransmitter.getClockInput().getUserInput();
                int choice = userInput.getChoice();
                if (choice == 1) {
                    int s = userInput.getSeconds() + userInput.getMinutes() * 60 + userInput.getHours() * 3600;
                    ClockDataTransmitter.setTimeSecond(s);
                    ClockDataTransmitter.getSignalSem().release();

                } else if (choice == 2) {
                    int s = userInput.getSeconds() + userInput.getMinutes() * 60 + userInput.getHours() * 3600;
                    ClockDataTransmitter.setAlarmIsSounding(false);
                    ClockDataTransmitter.setAlarmSecond(s);
                    ClockDataTransmitter.getSignalSem().release();

                } else  {
                    ClockDataTransmitter.toggleAlarmIsOnBool();
                    ClockDataTransmitter.getClockOutput().setAlarmIndicator(ClockDataTransmitter.getAlarmIsOnBool());
                    ClockDataTransmitter.getSignalSem().release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
