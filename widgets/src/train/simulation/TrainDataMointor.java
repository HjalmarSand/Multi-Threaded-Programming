package train.simulation;

import train.model.Segment;
import train.view.TrainView;

import java.util.HashSet;
import java.util.Set;

public class TrainDataMointor {
    private  Set<Segment> currTrainSegs = new HashSet<>();
    static TrainView view = new TrainView();


    public synchronized void addToSet(Segment segment) {
       try {
         while (currTrainSegs.contains(segment)) {
             wait();
            }
            currTrainSegs.add(segment);
         } catch (java.lang.InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static TrainView getTrainView () {
        return view;
    }

    public synchronized void removeFromSet(Segment seg) {
        currTrainSegs.remove(seg);
        //notifyAll();
    }
}
