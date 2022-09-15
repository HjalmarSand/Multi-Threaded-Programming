package factory.simulation;

import factory.model.Conveyor;
import factory.model.Tool;
import factory.model.Widget;

public class PaintThread extends Thread{
    private Tool paint;
    private FactoryMonitor monitor;
    private Conveyor conveyor;

    public PaintThread (Tool paint, FactoryMonitor monitor, Conveyor conveyor) {
        this.paint = paint;
        this.monitor = monitor;
        this.conveyor = conveyor;
    }
    @Override
    public void run () {
        while (true) {
            paint.waitFor(Widget.ORANGE_MARBLE);
            try {
                monitor.paintSem.acquire();
            conveyor.off();
            paint.performAction();
            monitor.paintSem.release();

            monitor.pressSem.acquire();
            conveyor.on();
            monitor.pressSem.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
