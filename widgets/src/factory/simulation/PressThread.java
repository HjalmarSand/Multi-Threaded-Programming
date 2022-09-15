package factory.simulation;

import factory.model.Conveyor;
import factory.model.Tool;
import factory.model.Widget;

public class PressThread extends Thread{
    private Tool press;
    private FactoryMonitor monitor;
    private Conveyor conveyor;
    public PressThread (Tool press, FactoryMonitor monitor, Conveyor conveyor) {
        this.press = press;
        this.monitor = monitor;
        this.conveyor = conveyor;
    }

    @Override
    public void run () {
        while (true) {
            press.waitFor(Widget.GREEN_BLOB);
            try {monitor.pressSem.acquire();
            conveyor.off();
            press.performAction();
            monitor.pressSem.release();

            monitor.paintSem.acquire();
            conveyor.on();
            monitor.paintSem.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
