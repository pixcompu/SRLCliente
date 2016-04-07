package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import logic.Room;
import logic.SeatState;
import remote.IRemoteMovie;
import remote.RemoteClient;
import remote.RemoteConection;
import ui.util.Notifier;

/**
 *
 * @author PIX
 */
public class SeatsHandler implements ActionListener {

    private final Set<String> prePurchased;
    private RemoteClient client;
    private IRemoteMovie manager;
    private final DialogSeats actualUI;
    private int[][] seatsState;
    private int roomID;
    private final ActionListener prePurchaseHandler = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (prePurchased.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No has seleccionado nada");
            } else {

                try {
                    prePurchaseSeats();
                    actualUI.showPurchseDialog(prePurchased);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }

    };

    public SeatsHandler(DialogSeats actualUI) {
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
        manager = RemoteConection.getInstance().getRemoteObject();
    }

    public ActionListener getPrePurchaseHandler() {
        return prePurchaseHandler;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String coordinates = e.getActionCommand();
        int row = Integer.parseInt(coordinates.split(":", 2)[0]);
        int column = Integer.parseInt(coordinates.split(":", 2)[1]);

        if (seatsState[row][column] == SeatState.FREE) {
            markAsSelected(row, column);
        } else if (prePurchased.contains(coordinates)) {
            markAsFree(row, column);
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
            prePurchased.add(row + ":" + column);
            actualUI.updateSeatColor(row, column, SeatState.SELECTED_USER);
        } catch (Exception ex) {
            Notifier.showMesage("Hubo un problema al procesar su seleccion");
        }

    }

    private void markAsFree(int row, int column) {
        try {
            manager.changeSeatState(roomID, row, column, SeatState.FREE);
            prePurchased.remove(row + ":" + column);
            actualUI.updateSeatColor(row, column, SeatState.FREE);
        } catch (Exception ex) {
            Notifier.showMesage("Hubo un problema al procesar su seleccion");
        }
    }
}
