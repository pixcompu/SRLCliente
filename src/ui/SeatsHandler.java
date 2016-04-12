package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import logic.Observer;
import logic.Room;
import logic.SeatState;
import remote.IRemoteMovie;
import remote.RemoteClient;
import remote.RemoteConection;
import ui.util.Notifier;
import ui.util.Timer;

/**
 *
 * @author PIX
 */
public class SeatsHandler extends WindowAdapter implements ActionListener, Observer {

    private final Set<String> prePurchased;
    private RemoteClient client;
    private IRemoteMovie manager;
    private final DialogSeats actualUI;
    private int[][] seatsState;
    private int roomID;
    private Timer timer;
    
    private final ActionListener prePurchaseHandler = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (prePurchased.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No has seleccionado nada");
            } else {

                try {
                    prePurchaseSeats();
                    timer.stop();
                    actualUI.showPurchseDialog(prePurchased);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }

    };

    public SeatsHandler(DialogSeats actualUI) {
        this.timer = new Timer(50, this);
        this.actualUI = actualUI;
        prePurchased = new HashSet();

    }

    public Room refreshRoom(int id) throws RemoteException {
        Room room = manager.getRoom(id);
        roomID = id;
        seatsState = room.getSeats();
        return room;
    }

    public void connect() throws Exception {
        client = new RemoteClient();
        client.connect();
        client.setObserver(this);
        manager = RemoteConection.getInstance().getRemoteObject();
    }

    public ActionListener getPrePurchaseHandler() {
        return prePurchaseHandler;
    }

    public void receiveMessage(String serverMessage) {
        int roomId = Integer.parseInt(serverMessage.split(":")[0]);
        int row = Integer.parseInt(serverMessage.split(":")[1]);
        int column = Integer.parseInt(serverMessage.split(":")[2]);
        int value = Integer.parseInt(serverMessage.split(":")[3]);
        if (roomId == this.roomID) {
            actualUI.updateSeatColor(row, column, value);
            seatsState[row][column] = value;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String coordinates = e.getActionCommand();
        int row = Integer.parseInt(coordinates.split(":", 2)[0]);
        int column = Integer.parseInt(coordinates.split(":", 2)[1]);
        
        if (seatsState[row][column] == SeatState.FREE) {
            markAsSelected(row, column);
            timer.start();
            timer.restart( true );
        } else if (prePurchased.contains(coordinates)) {
            markAsFree(row, column);
            boolean hasItems = prePurchased.size() > 0;
            timer.restart( hasItems );
        } else {
            Notifier.showMesage("No puedes seleccionar este asiento");
        }
    }

    private void prePurchaseSeats() throws Exception {
        for (String item : prePurchased) {
            int row = Integer.parseInt(item.split(":")[0]);
            int column = Integer.parseInt(item.split(":")[1]);
            manager.changeSeatState(roomID, row, column, SeatState.PRE_TAKEN);
        }
    }

    private void markAsSelected(int row, int column) {
        try {
            manager.changeSeatState(roomID, row, column, SeatState.SELECTED);
            seatsState[row][column] = SeatState.SELECTED;
            prePurchased.add(row + ":" + column);
            actualUI.updateSeatColor(row, column, SeatState.SELECTED_USER);
        } catch (Exception ex) {
            Notifier.showMesage("Hubo un problema al procesar su seleccion");
        }

    }

    private void markAsFree(int row, int column) {
        try {
            manager.changeSeatState(roomID, row, column, SeatState.FREE);
            seatsState[row][column] = SeatState.FREE;
            prePurchased.remove(row + ":" + column);
            actualUI.updateSeatColor(row, column, SeatState.FREE);
        } catch (Exception ex) {
            Notifier.showMesage("Hubo un problema al procesar su seleccion");
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        undoAllSelections();
        timer.stop();
    }

    private void undoAllSelections() {
        boolean hasItemsSelected = prePurchased.size() > 0;
        if (hasItemsSelected) {
            try {
                manager.changeSeatState(roomID, prePurchased, SeatState.FREE);
            } catch (RemoteException ex) {
                System.err.println("Error : " + ex.getMessage());
            }
        }
    }

    @Override
    public void update(int code, String message) {
        switch( code ){
            case Timer.TIME_OVER:
                undoAllSelections();
                Notifier.showMesage("Pasaron 50 segundos desde tu ultima seleccion, ¡lo sentimos!");
                break;
            case Timer.TIME_UPDATE:
                actualUI.setTimerText("Realize su compra en " + message + " segundos, por favor");
                break;
            case Timer.TIME_RESET:
                actualUI.setTimerText("--:--");
                break;
            default:
                System.out.println("Codigo desconocido " + code);
        }
    }

    
}
