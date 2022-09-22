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

        System.out.println("LOOOL");
    }

    @Override
    public void run() {
            pass.begin();
            monitor.incrementToEnter(fromFloor);

            monitor.prepareToEnter(pass);
            pass.enterLift();
            monitor.decreaseEnteringPassengers();

            monitor.prepareToLeave(pass);
            pass.exitLift();
            monitor.decreaseExitingPassengers();

            pass.end();
    }
}
