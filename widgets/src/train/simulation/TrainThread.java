package train.simulation;
import train.model.Route;
import train.model.Segment;

import javax.management.monitor.Monitor;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TrainThread extends Thread {
    private TrainDataMointor monitor;
    public TrainThread (TrainDataMointor monitor) {
        this.monitor = monitor;
    }

    //private TrainDataMointor monitor = new TrainDataMointor();
    private Route route = TrainDataMointor.getTrainView().loadRoute();
    ConcurrentLinkedDeque<Segment> queue = new ConcurrentLinkedDeque<>();

    @Override
    public synchronized void run() {
        for (int i = 0; i < 3; i++) {
            Segment first = route.next();
            monitor.addToSet(first);
            queue.addFirst(first);
            first.enter();
        }

        while (true) {
            runTrain();
        }
    }

    private synchronized void runTrain() {
        Segment head = route.next();
        monitor.addToSet(head);
        head.enter();
        queue.addFirst(head);
        Segment tail = queue.removeLast();
        tail.exit();
        monitor.removeFromSet(tail);
    }
}
