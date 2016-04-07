package ui;

import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;
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
import ui.util.TableModel;

/**
 *
 * @author PIX
 */
public class DialogPurchase extends JDialog {

    private JPanel content;
    private TableModel modeloTabla;

    public DialogPurchase(Dialog owner, boolean modal, Set<String> purchasedItems, CinemaFunction function) {
        super(owner, modal);
        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.BLUE.darker().darker());

        String[] header = {"Fila", "Columna", "Precio"};
        modeloTabla = new TableModel(header);
        JPanel panelDetailsPurchase = getPanelDetails(purchasedItems);

        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.X_AXIS));
        panelBtn.add(Box.createHorizontalGlue());
        JButton confirmPurchase = new JButton("Confirmar Compra");
        confirmPurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
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
        panelBtn.setBackground(Color.BLUE.darker().darker().darker());

        JLabel windowTitle = new JLabel("Resumen de Compra");
        windowTitle.setFont(new Font("Impact", Font.PLAIN, 20));
        windowTitle.setAlignmentX(CENTER_ALIGNMENT);
        windowTitle.setForeground(Color.YELLOW);

        content.add(Box.createVerticalGlue());
        content.add(windowTitle);
        content.add(panelDetailsPurchase);
        content.add(panelBtn);
        setContentPane(content);
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
        total.setFont(new Font("Impact", Font.PLAIN, 25));
        total.setBorder(new EmptyBorder(new Insets(10, 10, 10, 10)));
        amount.setFont(new Font("Impact", Font.PLAIN, 30));
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
}
