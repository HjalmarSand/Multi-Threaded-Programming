package clock.io;

import clock.AlarmClockEmulator;

import java.util.concurrent.Semaphore;

public class ClockDataTransmitter {
    private static AlarmClockEmulator emulator = new AlarmClockEmulator();
    private static ClockInput in  = emulator.getInput();
    private static Semaphore sem = in.getSemaphore();
    private static ClockOutput out = emulator.getOutput();
    private static boolean alarmIsOnBool = false;
    private static boolean alarmIsSounding = false;
    private static Semaphore signalSem = new Semaphore(1);

    private static int alarmHour;
    private static int alarmMinute;
    private static int alarmSecond;

    private static int timeSecond = 0;
    public static void setAlarmSecond(int second) {
        alarmSecond = second;
    }

    public static int getAlarmSecond (){
        return alarmSecond;
    }
    public static void setTimeSecond(int second) {
        timeSecond = second;
    }

    public static int getTimeSecond(){
        return timeSecond;
    }

    public static boolean getAlarmIsOnBool(){
        return alarmIsOnBool;
    }

    public static void toggleAlarmIsOnBool(){
        alarmIsOnBool = !alarmIsOnBool;
    }

    public static boolean getAlarmIsSounding(){
        return alarmIsSounding;
    }

    public static void setAlarmIsSounding (boolean bool) {
        alarmIsSounding = bool;
    }
    public static void toggleAlarmIsSounding(){
        alarmIsSounding = !alarmIsSounding;
    }

    public static Semaphore getSignalSem() {
        return signalSem;
    }

    public static AlarmClockEmulator getAlarmClockEmulator() {
        return emulator;
    }



    public static ClockInput getClockInput(){
        return in;
    }

    public static Semaphore getSemaphore(){
        return sem;
    }

    public static ClockOutput getClockOutput() {
        return out;
    }






}
