package remote;

import java.awt.*;
import java.io.*;
import java.net.Socket;
import ui.SeatsHandler;

/**
 * La caracteristica:

 Heterogeneidad Apertura outputStream extensibilidad. Seguridad. Escalabilidad.
 * Tolerancia a fallos. Concurrencia. Transparencia
 */
public class RemoteClient implements Runnable {

    private final String CLIENT_IP_ADDRESS = "127.0.0.1";
    private final int CLIENT_PORT = 1099;
    private SeatsHandler observer;
    protected DataInputStream inputStream;
    protected DataOutputStream outputStream;
    protected static Thread listener;

    public void run() {
        try {
            while (true) {
                String line = inputStream.readUTF();
                if( observer != null ){
                    observer.update(line);
                }
            }//end while
        }//end try//end try
        catch (IOException io) {
            io.printStackTrace();
        }//end catch
        finally {
            listener = null;
            try {
                outputStream.close();
            }//end try//end try
            catch (IOException io) {
                io.printStackTrace();
            }//end catch
        }//end finally
    }

    public void connect() throws Exception {
        Socket socket = new Socket(CLIENT_IP_ADDRESS, CLIENT_PORT);
        this.inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        listener = new Thread(this);
        listener.start();
    }
    
    public void setObserver(SeatsHandler handler){
        this.observer = handler;
    }
}
