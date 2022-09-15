package clock.io;

public class DisplayTimeThread extends Thread {

    private boolean isAlarm = false;
    long t0 = System.currentTimeMillis();
    int index = 0;
    @Override
    public void run() {
        while (true) {

            try {
                long now = System.currentTimeMillis();
                index += 1;
                sleep(t0 + index * 1000 - now);

                ClockDataTransmitter.getSignalSem().acquire();
                int seconds = ClockDataTransmitter.getTimeSecond();
                seconds += 1;

                seconds = seconds % (3600 * 24); //Ser till att sekunder aldrig överstiger ett dygn
                ClockDataTransmitter.setTimeSecond(seconds);
                ClockDataTransmitter.getClockOutput().displayTime((seconds / 3600) % 24, (seconds / 60) % 60, seconds % 60);


                int alarmTime = ClockDataTransmitter.getAlarmSecond();
                if (alarmTime == seconds) {
                    ClockDataTransmitter.setAlarmIsSounding(true);
                }
                if (ClockDataTransmitter.getAlarmIsOnBool() && ClockDataTransmitter.getAlarmIsSounding()){
                    ClockDataTransmitter.getClockOutput().alarm();
                } else {
                    ClockDataTransmitter.setAlarmIsSounding(false);
                }
                ClockDataTransmitter.getSignalSem().release();

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
