package ui;

import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dialog;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import static java.lang.Thread.sleep;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import logic.CinemaFunction;
import remote.RemoteConection;
import logic.SeatState;
import ui.util.Notifier;
import ui.util.TableModel;
import ui.util.ThemeValues;

/**
 *
 * @author PIX
 */
public class DialogPurchase extends JDialog implements Runnable, WindowListener{

    private final JPanel content;
    private final TableModel modeloTabla;
    private final JLabel timerLabel;
    private final ThemeValues theme = ThemeValues.getInstance();
    private final int TIMEOUT_SECONDS = 300;
    private int timeRemaining = 50;
    private boolean TIMER_RUNNING = true;
    private final int roomID;
    private final Set<String> purchasedItems;
    private Thread timer;

    public DialogPurchase(Dialog owner, boolean modal, Set<String> purchasedItems, CinemaFunction function) {
        super(owner, modal);
        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.BLUE.darker().darker());
        
        roomID = function.getRoom().getId();
        this.purchasedItems = purchasedItems;

        String[] header = {"Fila", "Columna", "Precio"};
        modeloTabla = new TableModel(header);
        JPanel panelDetailsPurchase = getPanelDetails(purchasedItems);

        JPanel panelBtn = new JPanel();
        timerLabel = new JLabel("--:--");
        timerLabel.setForeground(Color.YELLOW);
        timerLabel.setFont(theme.getHeaderFont(20));
        panelBtn.add(Box.createHorizontalGlue());
        panelBtn.add(timerLabel);
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        panelBtn.add(Box.createHorizontalGlue());
        JButton confirmPurchase = new JButton("Confirmar Compra");
        confirmPurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    stopTimer();
                    purchaseSeats(purchasedItems, function);
                    JOptionPane.showMessageDialog(null, "Comprado");
                    dispose();
                } catch (Exception ee) {
                    JOptionPane.showMessageDialog(null, "No se pudieron comprar" + ee.getMessage());
                }
            }
        });
        confirmPurchase.setMargin(new Insets(5, 5, 5, 5));
        panelBtn.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
        panelBtn.add(confirmPurchase);
        panelBtn.setBackground(theme.getBackgroundColor());

        JLabel windowTitle = new JLabel("Resumen de Compra");
        windowTitle.setFont(theme.getHeaderFont(20));
        windowTitle.setAlignmentX(CENTER_ALIGNMENT);
        windowTitle.setForeground(Color.YELLOW);

        content.add(Box.createVerticalGlue());
        content.add(windowTitle);
        content.add(panelDetailsPurchase);
        content.add(panelBtn);
        setContentPane(content);
        addWindowListener(this);
        startTimer();
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel getPanelDetails(Set<String> purchasedItems) {
        JScrollPane scrollingArea = new JScrollPane();

        JPanel purchaseTicket = new JPanel();
        purchaseTicket.setLayout(new BoxLayout(purchaseTicket, BoxLayout.Y_AXIS));

        JTable table = new JTable(modeloTabla);
        scrollingArea.add(table);
        scrollingArea.setViewportView(table);

        int totalPurchasePrice = 0;
        for (String item : purchasedItems) {

            int itemPrice = 25;

            ArrayList<String> row = new ArrayList();
            row.add(item.split(":")[0]);
            row.add(item.split(":")[1]);
            row.add(String.valueOf(itemPrice));
            modeloTabla.addRow(row);
            totalPurchasePrice += itemPrice;
        }

        purchaseTicket.add(scrollingArea);
        JPanel itemPurchaseTicket = new JPanel();
        itemPurchaseTicket.setBackground(Color.BLACK);
        itemPurchaseTicket.setLayout(new BoxLayout(itemPurchaseTicket, BoxLayout.X_AXIS));
        JLabel total = new JLabel("Total : ");
        JLabel amount = new JLabel("$" + String.valueOf(totalPurchasePrice));
        total.setFont(theme.getHeaderFont(25));
        total.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        amount.setFont(theme.getHeaderFont(30));
        amount.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        total.setForeground(Color.WHITE);
        amount.setForeground(Color.WHITE);
        itemPurchaseTicket.add(total);
        itemPurchaseTicket.add(Box.createHorizontalGlue());
        itemPurchaseTicket.add(amount);

        purchaseTicket.setBackground(Color.BLUE.darker().darker().darker());
        purchaseTicket.add(itemPurchaseTicket);
        return purchaseTicket;
    }

    private void purchaseSeats(Set<String> purchasedItems, CinemaFunction function) throws Exception {
        for (String item : purchasedItems) {
            int row = Integer.parseInt(item.split(":")[0]);
            int column = Integer.parseInt(item.split(":")[1]);
            RemoteConection.getInstance().getRemoteObject().changeSeatState(function.getMovie().getId(), row, column, SeatState.TAKEN);
        }
    }

   @Override
    public void windowClosing(WindowEvent e) {
        undoAllSelections();
        stopTimer();
        System.out.println("Bye");
    }

    @Override
    public void run() {
        while (TIMER_RUNNING) {
            while (timeRemaining > 0) {
                try {
                    setTimerText("Confirme en " + timeRemaining + " segundos, por favor");
                    sleep(1000);
                    timeRemaining--;
                } catch (InterruptedException ex) {
                    Logger.getLogger(SeatsHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (TIMER_RUNNING) {
                undoAllSelections();
                Notifier.showMesage("Pasaron 50 segundos desde tu ultima seleccion, ¡lo sentimos!");

            }
            TIMER_RUNNING = false;
            this.dispose();
        }
    }

    public void startTimer() {
        timeRemaining = TIMEOUT_SECONDS;
        TIMER_RUNNING = true;
        if (timer == null || !timer.isAlive()) {
            timer = new Thread(this);
            timer.start();
        } 
    }

    public void stopTimer() {
        timeRemaining = 0;
        TIMER_RUNNING = false;
    }

    private void undoAllSelections() {
       
            try {
                RemoteConection.getInstance().getRemoteObject().changeSeatState(roomID, purchasedItems, SeatState.FREE);
            } catch (RemoteException ex) {
                System.err.println("Error : " + ex.getMessage());
            } catch (Exception ex) {
            Logger.getLogger(DialogPurchase.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void setTimerText(String text) {
        this.timerLabel.setText(text);
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
