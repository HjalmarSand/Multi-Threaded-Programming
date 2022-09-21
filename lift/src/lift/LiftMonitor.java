package lift;

public class LiftMonitor {
    private int maxCapacity;
    private int numberOfFloors;
    private LiftView view;
    private int currentFloor;
    private int[] toEnter;
    private int[] toExit;
    private int currentPassengers;
    private boolean isMoving;
    private boolean isMovingUp; //what if it is not moving

    public LiftMonitor(int numberOfFloors, int maxLiftCapacity, LiftView view) {
        this.maxCapacity = maxLiftCapacity;
        this.numberOfFloors = numberOfFloors;
        this.view = view;
    }


    public synchronized int getCurrentFloor() {
        return currentFloor;
    }

    public synchronized void enterIfAllowed(Passenger pass) {
        try {
            while (currentPassengers == maxCapacity || isMoving) {
                    wait();
            }
            view.openDoors(pass.getStartFloor());
            pass.enterLift();
            currentPassengers++;
            toEnter[pass.getStartFloor()]--;
            toExit[pass.getDestinationFloor()]++;
            view.closeDoors();
            notifyAll(); //TODO se över så att hissen börjar röra sig igen

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //kolla om utrymme finns, kolla om öppna dörrar
    }

    public synchronized void leaveIfAllowed(Passenger pass) {
        try {
            while (isMoving || currentFloor != pass.getDestinationFloor()) {
                wait();
            }
            view.openDoors(pass.getDestinationFloor());
            pass.exitLift();
            currentPassengers--;
            toExit[pass.getDestinationFloor()]--;
            view.closeDoors();
            notifyAll();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void moveLift() {
        try {
            while (toEnter[currentFloor] != 0 || toExit[currentFloor] != 0) {
                wait();
            }

            if ((currentFloor == 0 && !isMovingUp)  || (currentFloor == numberOfFloors && isMovingUp)) {
                isMovingUp = !isMovingUp;
            }

            if (isMovingUp) {
                view.moveLift(currentFloor, currentFloor + 1);
            } else {
                view.moveLift(currentFloor, currentFloor - 1);
            }
                notifyAll();

        }  catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized int[] getToEnterArray() {
        return toEnter;
    }

    public synchronized int[] getToExitArray() {
        return toExit;
    }
}
