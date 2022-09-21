package lift;

public class LiftMonitor {
    private int maxCapacity;
    private int numberOfFloors;
    private int currentFloor;
    private int[] toEnter;
    private int[] toExit;
    private int currentPassengers;
    private boolean isMoving;
    private boolean isMovingUp; //what if it is not moving

    public LiftMonitor(int numberOfFloors, int maxLiftCapacity) {
        this.maxCapacity = maxLiftCapacity;
        this.numberOfFloors = numberOfFloors;
    }


    public synchronized int getCurrentFloor() {
        return currentFloor;
    }

    public synchronized void enterIfAllowed() {
        try {
            while(currentPassengers == maxCapacity || isMoving) {
                    wait();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        //kolla om utrymme finns, kolla om öppna dörrar
    }

    public synchronized int[] getToEnterArray() {
        return toEnter;
    }

    public synchronized int[] getToExitArray() {
        return toExit;
    }
}
