package lift;

public class LiftThread extends Thread {
    private LiftMonitor monitor;
    public LiftThread (LiftMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("calling lift move in lift thread");
            monitor.moveLift();
        }
    }
}
