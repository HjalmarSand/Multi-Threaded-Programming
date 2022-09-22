package lift;

import java.util.concurrent.Semaphore;

public class LiftMonitor {
    private int maxCapacity;
    private int numberOfFloors;
    private LiftView view;
    private int currentFloor = 0;
    private int[] toEnter;
    private int[] toExit;
    private int currentPassengers;
    private boolean isMoving;
    private boolean isMovingUp = false; //what if it is not moving
    private boolean doorClosed = true;
    private int waitingPassengers;
    public Semaphore animationSemaphore = new Semaphore(1);
    private int enteringPassengers;
    private int exitingPassengers;

    public LiftMonitor(int numberOfFloors, int maxLiftCapacity, LiftView view) {
        this.maxCapacity = maxLiftCapacity;
        this.numberOfFloors = numberOfFloors;
        toEnter = new int[numberOfFloors];
        toExit = new int[numberOfFloors];
        this.view = view;
    }

    public synchronized void decreaseEnteringPassengers() {
        enteringPassengers--;
        notifyAll();
    }

    public synchronized void decreaseExitingPassengers() {
        exitingPassengers--;
        notifyAll();
    }



    public synchronized LiftView getLiftView() {
        return view;
    }


    public synchronized int getToFloor () {

        if ((currentFloor == 0 && !isMovingUp)  || (currentFloor == numberOfFloors - 1  && isMovingUp)) {
            isMovingUp = !isMovingUp;
        }
        if (isMovingUp) {
            System.out.println(currentFloor + 1);
            return currentFloor + 1;
        } else {
            System.out.println(currentFloor -1);

            return currentFloor - 1;
        }    }

    public synchronized void incrementToEnter(int floor) {
        System.out.println(floor);
        toEnter[floor]++;
        waitingPassengers++;
        notifyAll();
    }

    public synchronized int getCurrentFloor() {
        return currentFloor;
    }

    public synchronized void prepareToEnter(Passenger pass) {
        try {

            //System.out.println("Is moving: " + isMoving);
            while (currentPassengers == maxCapacity || pass.getStartFloor() != currentFloor || isMoving) {
                //varje gång hissen byter våning notifiar den, så wait här avbryts
                //System.out.println("Is moving: " + isMoving);
                //System.out.println("Current floor elevator: " + currentFloor + " Passenger floor: " + pass.getStartFloor());
                wait();
            }
            System.out.println("Enter lift is moving");
            //pass.enterLift();
            enteringPassengers++;
            currentPassengers++;
            waitingPassengers--;
            toEnter[pass.getStartFloor()]--;
            toExit[pass.getDestinationFloor()]++;

            notifyAll(); //TODO se över så att hissen börjar röra sig igen

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //kolla om utrymme finns, kolla om öppna dörrar
    }

    public synchronized void prepareToLeave(Passenger pass) {
        try {
            while (currentFloor != pass.getDestinationFloor() || isMoving) {
                wait();
            }
            currentPassengers--;
            exitingPassengers++;
            toExit[pass.getDestinationFloor()]--;
            notifyAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void moveLift(int currentFloor, int toFloor) {
        try {

            while ((toEnter[currentFloor] != 0 && currentPassengers < 4)
                    || toExit[currentFloor] != 0
                    || waitingPassengers + currentPassengers == 0
                    || exitingPassengers + enteringPassengers > 0) {

                System.out.println("enters move lift wait");

                if(doorClosed) {
                    view.openDoors(currentFloor);
                    doorClosed = false;
                }

                isMoving = false;
                System.out.println("Lift is falling asleep");
                notifyAll();
                wait();
            }
            isMoving = true;
            if(!doorClosed) {
                view.closeDoors();
                doorClosed = true;
            }

            this.currentFloor = toFloor;

            System.out.println(toEnter[0] + " " + toEnter[1] + " " + toEnter[2] + " " + toEnter[3] +
                    " " + toEnter[4] + " " + toEnter[5] + " " + toEnter[6]);

            notifyAll();
        }  catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
