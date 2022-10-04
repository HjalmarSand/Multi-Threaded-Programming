package actor;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public class ActorThread<M> extends Thread {
    public BlockingQueue<M> incomingMessages = new LinkedBlockingDeque<>();


    /** Called by another thread, to send a message to this thread. */
    public void send(M message) {
        incomingMessages.add(message);
    }
    
    /** Returns the first message in the queue, or blocks if none available. */
    protected M receive() throws InterruptedException {
        return incomingMessages.take();
    }
    
    /** Returns the first message in the queue, or blocks up to 'timeout'
        milliseconds if none available. Returns null if no message is obtained
        within 'timeout' milliseconds. */
    protected M receiveWithTimeout(long timeout) throws InterruptedException {
        return incomingMessages.poll(timeout, TimeUnit.MILLISECONDS);
    }
}