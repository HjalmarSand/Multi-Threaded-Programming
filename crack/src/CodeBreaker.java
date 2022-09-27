import java.awt.*;
import java.math.BigInteger;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import client.view.StatusWindow;
import client.view.WorklistItem;
import network.Sniffer;
import network.SnifferCallback;

public class CodeBreaker implements SnifferCallback {

    private final JPanel workList;
    private final JPanel progressList;
    
    private final JProgressBar mainProgressBar;
    private static final CodeBreaker codeBreaker = new CodeBreaker();

    // -----------------------------------------------------------------------
    
    private CodeBreaker() {
        StatusWindow w  = new StatusWindow();

        workList        = w.getWorkList();
        progressList    = w.getProgressList();
        mainProgressBar = w.getProgressBar();
    }
    
    // -----------------------------------------------------------------------
    
    public static void main(String[] args) {

        /*
         * Most Swing operations (such as creating view elements) must be performed in
         * the Swing EDT (Event Dispatch Thread).
         * 
         * That's what SwingUtilities.invokeLater is for.
         */
        new Sniffer(codeBreaker).start();
    }

    // -----------------------------------------------------------------------

    /** Called by a Sniffer thread when an encrypted message is obtained. */
    @Override
    public void onMessageIntercepted(String message, BigInteger n) {
        System.out.println("message intercepted (N=" + n + ")...");
        updateWindow(message, n);
    }

    public void updateWindow(String message, BigInteger n) {
        SwingUtilities.invokeLater(() -> {
            codeBreaker.workList.add(new WorklistItem(n, message));
            new Sniffer(codeBreaker).start();
        });
    }
}
