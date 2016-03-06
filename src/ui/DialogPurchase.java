package ui;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author PIX
 */
public class DialogPurchase extends JDialog {

    private JPanel content;

    public DialogPurchase(Dialog owner, boolean modal, Set<String> purchasedItems) {
        super(owner, modal);
        content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.BLUE.darker().darker());
        JPanel panelDetailsPurchase = getPanelDetails(purchasedItems);
        JButton confirmPurchase = new JButton("Confirmar Compra");
        confirmPurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Comprado");
                dispose();
            }
        });
        content.add(panelDetailsPurchase);
        content.add(confirmPurchase);
        setContentPane(content);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel getPanelDetails(Set<String> purchasedItems) {
        JPanel purchaseTicket = new JPanel();
        purchaseTicket.setLayout(new BoxLayout(purchaseTicket, BoxLayout.Y_AXIS));
        int totalPurchasePrice = 0;
        for (String item : purchasedItems) {
            JPanel itemPurchaseTicket = new JPanel();
            itemPurchaseTicket.setBackground(Color.WHITE);
            itemPurchaseTicket.setLayout(new BoxLayout(itemPurchaseTicket, BoxLayout.X_AXIS));
            String textSeatCoordinates = "Fila : " + item.split(":")[0] + ", Columna : " + item.split(":")[1];
            JLabel seatCoordinates = new JLabel(textSeatCoordinates);
            JLabel price = new JLabel("$25");
            totalPurchasePrice += 25;
            itemPurchaseTicket.add(seatCoordinates);
            itemPurchaseTicket.add(Box.createHorizontalGlue());
            itemPurchaseTicket.add(price);
            purchaseTicket.add(itemPurchaseTicket);
        }
        JPanel itemPurchaseTicket = new JPanel();
        itemPurchaseTicket.setBackground(Color.WHITE);
        itemPurchaseTicket.setLayout(new BoxLayout(itemPurchaseTicket, BoxLayout.X_AXIS));
        JLabel total = new JLabel("Total : ");
        JLabel amount = new JLabel("$" + String.valueOf(totalPurchasePrice));
        itemPurchaseTicket.add(total);
        itemPurchaseTicket.add(Box.createHorizontalGlue());
        itemPurchaseTicket.add(amount);
        purchaseTicket.add(itemPurchaseTicket);
        return purchaseTicket;
    }

}
