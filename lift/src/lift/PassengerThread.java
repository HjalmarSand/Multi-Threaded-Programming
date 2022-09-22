package lift;

public class PassengerThread extends Thread {
    LiftMonitor monitor;
    Passenger pass;
    int fromFloor;
    int toFloor;


    public PassengerThread(LiftMonitor monitor, Passenger pass) {
        System.out.println("Hey hey!!");
        this.monitor = monitor;
        this.pass = pass;

        this.fromFloor = pass.getStartFloor();
        this.toFloor = pass.getDestinationFloor();
        monitor.incrementToEnter(fromFloor);
        System.out.println("LOOOL");
    }

    @Override
    public void run() {
        pass.begin();
        monitor.enterIfAllowed(pass);

        monitor.leaveIfAllowed(pass);
        pass.end();
    }

}
