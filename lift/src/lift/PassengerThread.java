package lift;

public class PassengerThread extends Thread {
    LiftMonitor monitor;
    Passenger pass;
    int fromFloor;
    int toFloor;


    public PassengerThread(LiftMonitor monitor, Passenger pass) {
        this.monitor = monitor;
        this.pass = pass;

        this.fromFloor = pass.getStartFloor();
        this.toFloor = pass.getDestinationFloor();
        monitor.getToEnterArray()[fromFloor]++;
    }

    @Override
    public void run() {
        while(true) {

            pass.begin();

            if(fromFloor == monitor.getCurrentFloor()) {
                monitor.enterIfAllowed();
            }

            if(toFloor == monitor.getCurrentFloor()) {

            }
        }
    }

}
