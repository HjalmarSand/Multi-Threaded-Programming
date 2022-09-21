package train.simulation;

import train.view.TrainView;

public class TrainSimulation {

    public static void main(String[] args) {

        TrainDataMointor monitor = new TrainDataMointor();

        for(int i = 0; i < 20; i++) {
            new TrainThread(monitor).start();
        }


        /*for (int i = 1; i < 4; i++) {
            Segment first1 = route1.next();
            queue.addFirst(first1);
            first1.enter();

            Segment first2 = route2.next();
            queue.addFirst(first2);
            first2.enter();

            Segment first3 = route3.next();
            queue.addFirst(first3);
            first3.enter();
        }

        while (true) {
            Segment head1 = route1.next();
            head1.enter();
            queue.addFirst(head1);
            Segment temp1     = queue.removeLast();
            temp1.exit();

            Segment head2 = route2.next();
            head2.enter();
            queue.addFirst(head2);
            Segment temp2     = queue.removeLast();
            temp2.exit();

            Segment head3 = route3.next();
            head3.enter();
            queue.addFirst(head3);
            Segment temp3    = queue.removeLast();
            temp3.exit();
        }
         */
    }
}
