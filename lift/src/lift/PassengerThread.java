package lift;

public class PassengerThread extends Thread {
    LiftMonitor monitor;
    Passenger pass;


    PassengerThread (LiftMonitor monitor, Passenger pass) {
        this.monitor = monitor;
        this.pass = pass;
    }


}
