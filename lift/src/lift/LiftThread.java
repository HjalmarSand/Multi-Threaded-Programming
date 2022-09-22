package lift;

public class LiftThread extends Thread {
    private LiftMonitor monitor;
    public LiftThread (LiftMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {

                int currentFloor = monitor.getCurrentFloor();
                int toFloor = monitor.getToFloor();


                //en wait ser till att om stick figures är där går vi ej vidare, innan vi kör wait måste is moving ändras till false
                //Vi skulle vilja göra så att isMoving är true om vi inte är i wait
                monitor.moveLift(currentFloor, toFloor);
                //om jag kommer hit har wait ovan försvunnit, så liften ska röra på sig även i viewen
                monitor.getLiftView().moveLift(currentFloor, toFloor);



        }
    }
}
