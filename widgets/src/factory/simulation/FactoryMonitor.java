package factory.simulation;

import factory.model.Tool;

import java.util.concurrent.Semaphore;

public class FactoryMonitor {
    public Semaphore paintSem = new Semaphore(1);
    public Semaphore pressSem = new Semaphore(1);


    public void press() {

    }

    public void paint() {

    }
}
