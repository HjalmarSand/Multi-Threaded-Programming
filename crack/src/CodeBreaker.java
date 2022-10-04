import java.awt.*;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import javax.swing.*;

import client.view.ProgressItem;
import client.view.StatusWindow;
import client.view.WorklistItem;
import network.Sniffer;
import network.SnifferCallback;
import rsa.Factorizer;
import rsa.ProgressTracker;

public class CodeBreaker implements SnifferCallback {

    private final JPanel workList;
    private final JPanel progressList;
    
    private final JProgressBar mainProgressBar;
    private final ExecutorService threadPool = Executors.newFixedThreadPool(4);
    public static int progressTracker = 0;
    private Semaphore sema = new Semaphore(1);

    // -----------------------------------------------------------------------
    
    private CodeBreaker() {
        StatusWindow w  = new StatusWindow();

        workList        = w.getWorkList();
        progressList    = w.getProgressList();
        mainProgressBar = w.getProgressBar();
        w.enableErrorChecks();

    }

    
    // -----------------------------------------------------------------------
    
    public static void main(String[] args) {

        /*
         * Most Swing operations (such as creating view elements) must be performed in
         * the Swing EDT (Event Dispatch Thread).
         * 
         * That's what SwingUtilities.invokeLater is for.
         */


        SwingUtilities.invokeLater(() -> {
            CodeBreaker codeBreaker = new CodeBreaker();
            new Sniffer(codeBreaker).start();
        });
    }

    // -----------------------------------------------------------------------

    /** Called by a Sniffer thread when an encrypted message is obtained. */
    @Override
    public void onMessageIntercepted(String message, BigInteger n) {
        SwingUtilities.invokeLater(() -> {
            WorklistItem item = new WorklistItem(n, message);
            JButton crackButton = new JButton("Crack");
            item.add(crackButton,BorderLayout.EAST);
            this.workList.add(item);


            crackButton.addActionListener(e -> {
                try {
                    sema.acquire();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                this.mainProgressBar.setMaximum(1000000 + mainProgressBar.getMaximum());
                sema.release();
                this.workList.remove(item);
                ProgressItem progressItem = new ProgressItem(n,message);
                JButton cancelButton = new JButton("Cancel");
                progressItem.add(cancelButton, BorderLayout.EAST);
                progressList.add(progressItem);

                ProgressTracker tracker = ppmDelta -> {
                    SwingUtilities.invokeLater(() -> {
                        progressItem.getProgressBar().setValue(progressItem.getProgressBar().getValue() + ppmDelta);
                        mainProgressBar.setValue(mainProgressBar.getValue() + ppmDelta);
                    });
                };

                JButton removeFinishedButton = new JButton("Remove");

                removeFinishedButton.addActionListener(f -> {
                    progressList.remove(progressItem);
                    try {
                        sema.acquire();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                    mainProgressBar.setValue(mainProgressBar.getValue() - 1000000);
                    mainProgressBar.setMaximum(mainProgressBar.getMaximum() - 1000000);
                    //progressTracker -= 1000000;
                    sema.release();
                });


                Future<String> futureItem = this.threadPool.submit(() -> {
                    try {
                        String progress = (Factorizer.crack(message,n, tracker));

                        SwingUtilities.invokeLater(() -> {
                            progressItem.getTextArea().setText(progress);
                            progressItem.add(removeFinishedButton,BorderLayout.EAST);
                            progressItem.remove(cancelButton);
                        });
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }, "alright, fuck you string");

                cancelButton.addActionListener(ex -> {
                    futureItem.cancel(true);
                    if(futureItem.isCancelled()) {
                        progressItem.remove(cancelButton);
                        progressItem.add(removeFinishedButton);
                        progressItem.getTextArea().setText("Cancelled");
                        mainProgressBar.setValue(mainProgressBar.getValue() +
                                (1000000 - progressItem.getProgressBar().getValue()));
                        progressItem.getProgressBar().setValue(1000000);
                    }
                });
            });
        });

        System.out.println("message intercepted (N=" + n + ")...");
    }

    public static void addToProgressTracker(int add)  {
         progressTracker += add ;
    }

}
