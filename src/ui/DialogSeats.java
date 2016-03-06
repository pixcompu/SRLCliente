package ui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import logic.CinemaFunction;
import logic.CinemaManager;
import logic.Room;
import logic.SeatState;

/**
 *
 * @author PIX
 */
public class DialogSeats extends JDialog implements ActionListener {

    private final JPanel content;
    private final JButton[][] seats;
    private final Set<String> purchased;
    private final CinemaFunction function;

    public DialogSeats(Frame owner, boolean modal, String itemID) {
        super(owner, modal);
        function = CinemaManager.getInstance().getById(itemID);
        Room functionRoom = function.getRoom();
        setTitle("Asientos para " + function.getMovie().getMovieName());
        int[][] seatsState = functionRoom.getSeats();
        int rows = functionRoom.getRows();
        int columns = functionRoom.getColumns();
        content = new JPanel();
        seats = new JButton[rows][columns];
        purchased = new HashSet<>();
        JButton btnPurchase = new JButton("Comprar Seleccionados");
        btnPurchase.addActionListener(this);

        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JPanel panelSeats = getSeats(seatsState, rows, columns);
        panelSeats.setAlignmentX(CENTER_ALIGNMENT);
        content.add(panelSeats);
        content.add(btnPurchase);
        setContentPane(content);
        content.setBackground(Color.BLUE.darker().darker());
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private JPanel getSeats(int[][] seatsState, int rows, int columns) {
        JPanel panelSeats = new JPanel();
        panelSeats.setLayout(new GridLayout(rows, columns));

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seats[i][j] = new JButton(" ");
                seats[i][j].setActionCommand(i + ":" + j);
                seats[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String coordinates = e.getActionCommand();
                        int row = Integer.parseInt(coordinates.split(":", 2)[0]);
                        int column = Integer.parseInt(coordinates.split(":", 2)[1]);

                        if (seatsState[row][column] == SeatState.FREE) {
                            seats[row][column].setBackground(getColor(SeatState.SELECTED_USER));
                            CinemaManager.getInstance().changeSeatState(function.getMovie().getMovieID(), row, column, SeatState.SELECTED);
                            purchased.add(coordinates);
                        } else {
                            JOptionPane.showMessageDialog(null, "No puedes seleccionar este asiento");
                        }
                    }
                });
                seats[i][j].setBackground(getColor(seatsState[i][j]));
                panelSeats.add(seats[i][j]);
            }
        }

        return panelSeats;
    }

    private Color getColor(int seatsState) {
        switch (seatsState) {
            case SeatState.FREE:
                return Color.GREEN;
            case SeatState.SELECTED:
                return Color.GRAY.darker();
            case SeatState.PRE_TAKEN:
                return Color.BLUE;
            case SeatState.TAKEN:
                return Color.RED;
            case SeatState.SELECTED_USER:
                return Color.GRAY;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (purchased.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No has seleccionado nada");
        } else {
            DialogPurchase dialogPurchase = new DialogPurchase(this, true, purchased);
            dialogPurchase.setVisible(true);
            dispose();
        }
    }
}
