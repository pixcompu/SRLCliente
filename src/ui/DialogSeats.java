package ui;

import java.awt.Color;
import java.awt.Component;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logic.CinemaFunction;
import logic.Room;
import logic.SeatState;
import ui.util.Notifier;
import ui.util.ThemeValues;

/**
 *
 * @author PIX
 */
public class DialogSeats extends JDialog{

    private final JPanel content = new JPanel();
    private JButton[][] seats;
    private final SeatsHandler seatsHandler;
    private final CinemaFunction function;
    private final ThemeValues theme = ThemeValues.getInstance();
    private JLabel timerLabel;

    public DialogSeats(Frame owner, boolean modal, CinemaFunction selected) {
        super(owner, modal);
        configureWindow();
        addHeader("Seleccione los asientos que desea comprar");
        function = selected;
        seatsHandler = new SeatsHandler(this);

        try {
            seatsHandler.connect();
            Room room = seatsHandler.refreshRoom(selected.getRoom().getId());
            JPanel seatsArea = getSeats(room);
            JPanel controls = getControlsPanel();
            content.add(Box.createVerticalGlue());
            content.add(seatsArea);
            content.add(controls);
            addWindowListener(seatsHandler);
        } catch (Exception ex) {
            ex.printStackTrace();
            Notifier.showMesage("Hubo un problema con la conexion");
        }
        pack();
        setLocationRelativeTo(null);
    }

    public void showPurchseDialog(Set<String> items) {
        DialogPurchase dialogPurchase = new DialogPurchase(this, true, items, function);
        dialogPurchase.setVisible(true);
        dispose();
    }
    
    public void updateSeatColor(int row, int column, int newState){
        seats[row][column].setBackground(getColor(newState));
    }
    
    public void setTimerText(String text){
        timerLabel.setText(text);
    }

    private JPanel getSeats(Room room) {
        JPanel panelSeats = new JPanel();
        int[][] seatsState = room.getSeats();
        int rows = room.getRows();
        int columns = room.getColumns();
        seats = new JButton[rows][columns];
        panelSeats.setAlignmentX(CENTER_ALIGNMENT);
        Dimension seatsDimension = getSeatsAreaSize();
        this.setSize(panelSeats, seatsDimension);
        panelSeats.setBackground(Color.WHITE);
        panelSeats.setAlignmentX(CENTER_ALIGNMENT);
        panelSeats.setLayout(new GridLayout(rows, columns));

        String buttonText = " ";
        String actionTextTemplate = "%d:%d";
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                seats[i][j] = new JButton(buttonText);
                seats[i][j].setActionCommand(String.format(actionTextTemplate, i, j));
                seats[i][j].addActionListener(seatsHandler);
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
                return Color.MAGENTA;
            default:
                throw new AssertionError();
        }
    }

    private void setSize(Component component, Dimension dimension) {
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setSize(dimension);
        component.setMaximumSize(dimension);
    }

    private void configureWindow() {
        Dimension screenSize = theme.getScreenSize();
        int width = screenSize.width / 2;
        int heigth = screenSize.height / 2;
        Dimension modalDimension = new Dimension(width, heigth);
        setSize(content, modalDimension);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(theme.getBackgroundColor());
        content.add(Box.createVerticalGlue());
        setTitle("Asientos de la Sala");
        setContentPane(content);
    }

    private void addHeader(String title) {
        JLabel windowTitle = new JLabel(title);
        windowTitle.setFont(theme.getHeaderFont(20));
        windowTitle.setAlignmentX(CENTER_ALIGNMENT);
        windowTitle.setForeground(theme.getWindowTitleColor());
        content.add(windowTitle);
    }

    private JPanel getControlsPanel() {
        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        JButton btnPurchase = new JButton("Comprar Seleccionados");
        btnPurchase.addActionListener(seatsHandler.getPrePurchaseHandler());
        btnPurchase.setMargin(new Insets(5, 5, 5, 5));
        btnPurchase.setAlignmentX(RIGHT_ALIGNMENT);
        
        timerLabel = new JLabel("--:--");
        timerLabel.setForeground(theme.getTimerTextColor());
        timerLabel.setFont(theme.getHeaderFont(20));
        panelBtn.add(Box.createHorizontalGlue());
        panelBtn.add(timerLabel);
        panelBtn.add(Box.createHorizontalGlue());
        panelBtn.add(btnPurchase);
        panelBtn.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
        panelBtn.setBackground(theme.getBackgroundColor());
        return panelBtn;
    }

    private Dimension getSeatsAreaSize() {
        Dimension screenSize = theme.getScreenSize();
        int width = ((screenSize.width / 2) / 100) * 90;
        int heigth = ((screenSize.height / 2) / 100) * 80;
        return new Dimension(width, heigth);
    }
    
}
