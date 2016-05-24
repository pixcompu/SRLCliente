package remote;

import java.io.*;
import java.net.Socket;
import ui.SeatsHandler;

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
                    observer.receiveMessage(line);
                }
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
        finally {
            listener = null;
            try {
                outputStream.close();
            }
            catch (IOException io) {
                io.printStackTrace();
            }
        }
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
