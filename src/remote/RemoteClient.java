package remote;

import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * La caracteristica:
 *
 * Heterogeneidad Apertura o extensibilidad. Seguridad. Escalabilidad.
 * Tolerancia a fallos. Concurrencia. Transparencia
 */
public class RemoteClient implements Runnable {

    private final String CLIENT_IP_ADDRESS = "127.0.0.1";
    private final int CLIENT_PORT = 1099;
    protected DataInputStream i;
    protected DataOutputStream o;
    protected static Thread listener;

    public void run() {
        try {
            while (true) {
                String line = i.readUTF();
                int roomId = Integer.parseInt(line.split(":")[0]);
                int row = Integer.parseInt(line.split(":")[1]);
                int column = Integer.parseInt(line.split(":")[2]);
                int value = Integer.parseInt(line.split(":")[3]);
                System.out.println(line);
            }//end while
        }//end try
        catch (IOException io) {
            io.printStackTrace();
        }//end catch
        finally {
            listener = null;
            try {
                o.close();
            }//end try
            catch (IOException io) {
                io.printStackTrace();
            }//end catch
        }//end finally
    }

    public void connect() throws Exception {
        Socket socket = new Socket(CLIENT_IP_ADDRESS, CLIENT_PORT);
        this.i = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        this.o = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        listener = new Thread(this);
        listener.start();
    }
}
