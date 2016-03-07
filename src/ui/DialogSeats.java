package ui;

import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int heigth = screenSize.height / 2;
        content = new JPanel();
        Dimension modalDimension = new Dimension(width, heigth);
        content.setSize(modalDimension);
        content.setMaximumSize(modalDimension);
        content.setMinimumSize(modalDimension);
        content.setPreferredSize(modalDimension);
        
        function = CinemaManager.getInstance().getById(itemID);
        Room functionRoom = function.getRoom();
        setTitle("Asientos de la Sala");
        int[][] seatsState = functionRoom.getSeats();
        int rows = functionRoom.getRows();
        int columns = functionRoom.getColumns();
        seats = new JButton[rows][columns];
        purchased = new HashSet<>();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(Box.createVerticalGlue());
        
        JLabel windowTitle = new JLabel("Seleccione los asientos que desea comprar");
        
        windowTitle.setFont(new Font("Impact", Font.PLAIN, 20));
        windowTitle.setAlignmentX(CENTER_ALIGNMENT);
        windowTitle.setForeground(Color.YELLOW);
        content.add(windowTitle);
        JPanel panelSeats = getSeats(seatsState, rows, columns);
         content.add(Box.createVerticalGlue());
        panelSeats.setAlignmentX(CENTER_ALIGNMENT);
        content.add(panelSeats);
        
        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        JButton btnPurchase = new JButton("Comprar Seleccionados");
        btnPurchase.addActionListener(this);
        btnPurchase.setMargin(new Insets(5, 5, 5, 5));
        btnPurchase.setAlignmentX(RIGHT_ALIGNMENT);
        panelBtn.add(Box.createHorizontalGlue());
        panelBtn.add(btnPurchase);
        panelBtn.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
        panelBtn.setBackground(Color.BLUE.darker().darker().darker());
        content.add(panelBtn);
        setContentPane(content);
        content.setBackground(Color.BLUE.darker().darker());
        this.pack();
        this.setLocationRelativeTo(null);
    }

    private JPanel getSeats(int[][] seatsState, int rows, int columns) {
        JPanel panelSeats = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = ((screenSize.width / 2)/100)*90;
        int heigth = ((screenSize.height / 2)/100)*80;
        Dimension seatsDimension = new Dimension(width, heigth);
        
        panelSeats.setSize(seatsDimension);
        panelSeats.setMinimumSize(seatsDimension);
        panelSeats.setMaximumSize(seatsDimension);
        panelSeats.setPreferredSize(seatsDimension);
        panelSeats.setBackground(Color.WHITE);
        
        panelSeats.setAlignmentX(CENTER_ALIGNMENT);
        
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
                        } else if(purchased.contains(coordinates)) {
                            seats[row][column].setBackground(getColor(SeatState.FREE));
                            CinemaManager.getInstance().changeSeatState(function.getMovie().getMovieID(), row, column, SeatState.FREE);
                            purchased.remove(coordinates);
                        }else{
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
