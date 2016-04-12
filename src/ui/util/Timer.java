package ui.util;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Observer;
import ui.SeatsHandler;

/**
 *
 * @author PIX
 */
public class Timer implements Runnable{
    
    public static final int TIME_OVER = 0;
    public static final int TIME_UPDATE = 1;
    public static final int TIME_RESET = 2;
    
    private final Observer observer;
    private int timeRemaining;
    private Thread thread;
    private boolean TIMER_RUNNING = true;
    private final int TIMEOUT_SECONDS;
    
    public Timer(int timeoutSeconds, Observer observer){
        this.TIMEOUT_SECONDS = timeoutSeconds;
        this.observer = observer;
    }
    
    
    @Override
    public void run() {
        while (TIMER_RUNNING) {
            while (timeRemaining > 0) {
                try {
                    System.out.println(timeRemaining);
                    observer.update(TIME_UPDATE, String.valueOf(timeRemaining));
                    sleep(1000);
                    timeRemaining--;
                } catch (InterruptedException ex) {
                    Logger.getLogger(SeatsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (TIMER_RUNNING) {
                observer.update(TIME_OVER, "--:--");
            }
            TIMER_RUNNING = false;
            observer.update(TIME_RESET, "--:--");
        }
    }

    public void start() {
        timeRemaining = TIMEOUT_SECONDS;
        TIMER_RUNNING = true;
        if (thread == null || !thread.isAlive()) {
            thread = new Thread(this);
            thread.start();
        } 
    }

    public void restart(boolean hasItems) {
        if ( hasItems ) {
            timeRemaining = TIMEOUT_SECONDS;
        } else {
            stop();
        }
    }

    public void stop() {
        timeRemaining = 0;
        TIMER_RUNNING = false;
    }
}
