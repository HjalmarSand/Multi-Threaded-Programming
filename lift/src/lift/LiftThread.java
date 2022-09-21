package lift;

public class LiftThread extends Thread {
    private LiftMonitor monitor;
    public LiftThread (LiftMonitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            monitor.moveLift();
        }
    }
}
