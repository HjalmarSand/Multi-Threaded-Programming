package factory.simulation;

import factory.model.Conveyor;
import factory.model.Tool;
import factory.model.Widget;

public class FactoryController {
    
    public static void main(String[] args) {
        Factory factory = new Factory();
        Conveyor conveyor = factory.getConveyor();
        Tool press = factory.getPressTool();
        Tool paint = factory.getPaintTool();
        FactoryMonitor monitor = new FactoryMonitor();

        PaintThread paintThread = new PaintThread(paint, monitor, conveyor);
        PressThread pressThread = new PressThread(press, monitor, conveyor);

        pressThread.start();
        paintThread.start();



    }
}
