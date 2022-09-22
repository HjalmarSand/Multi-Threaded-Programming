
import lift.*;

public class OnePersonRidesLift {

    public static void main(String[] args) {
        final int NBR_FLOORS = 10, MAX_PASSENGERS = 5;

        LiftView  view = new LiftView(NBR_FLOORS, MAX_PASSENGERS);

        LiftMonitor monitor = new LiftMonitor(NBR_FLOORS, MAX_PASSENGERS, view);
        new LiftThread(monitor).start();

        System.out.println("test");

        for(int i = 0; i < 20; i++) {
            System.out.println("test2");
            new PassengerThread(monitor, view.createPassenger()).start();
        }
    }
}