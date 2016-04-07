package ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import logic.CinemaFunction;
import logic.Movie;
import ui.util.ThemeValues;

/**
 *
 * @author PIX
 */
public class FunctionCardBuilder {

    private JPanel card;
    private final CinemaFunction function;
    private final JFrame actualUI;
    private final Color BORDER_COLOR = Color.YELLOW;
    private final Color ITEM_BACKGROUND_COLOR = Color.GREEN;
    private final Color INFORMATION_FONT_COLOR = Color.WHITE;
    private final Color BACKGROUND_COLOR = Color.BLACK;
    private final ActionListener selectionHandler = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            DialogSeats dialogSeats = new DialogSeats(actualUI, true, function);
            dialogSeats.setVisible(true);
        }
    };

    public FunctionCardBuilder(JFrame actualUI, CinemaFunction function) {
        this.function = function;
        this.actualUI = actualUI;
    }

    public void build() {
        card = getContainer();
        JLabel poster = getPoster("/ui/resources/deadpoolIcon.jpg");
        JPanel information = getInformationPanel();
        card.add(poster);
        card.add(information);
    }

    public JPanel getCard() {
        return card;
    }

    private JPanel getContainer() {
        JPanel functionPanel = new JPanel();
        setSize(functionPanel, getSize());
        functionPanel.setBackground(ITEM_BACKGROUND_COLOR);
        functionPanel.setLayout(new BoxLayout(functionPanel, BoxLayout.X_AXIS));
        functionPanel.setBorder(new LineBorder(BORDER_COLOR));
        return functionPanel;
    }

    private Dimension getSize() {
        Dimension screenSize = ThemeValues.getInstance().getScreenSize();
        int cardWidth = (screenSize.width / 100) * 80;
        int cardHeigth = (screenSize.height / 100) * 30;
        return new Dimension(cardWidth, cardHeigth);
    }

    public Font getCardTitleFont() {
        return new Font("Century Gothic", Font.PLAIN, 40);
    }

    private void setSize(Component component, Dimension dimension) {
        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setSize(dimension);
        component.setMaximumSize(dimension);
    }

    private JLabel getPoster(String imageURL) {
        Dimension posterSize = getPosterSize();
        JLabel functionPoster = new JLabel();
        Dimension posterDimension = new Dimension(posterSize.width, posterSize.height);
        ImageIcon poster = new ImageIcon(getClass().getResource(imageURL));
        Image scaledPoster = poster.getImage().getScaledInstance(
                posterSize.width, posterSize.height, Image.SCALE_DEFAULT);
        functionPoster.setIcon(new ImageIcon(scaledPoster));
        setSize(functionPoster, posterDimension);
        return functionPoster;
    }

    private Dimension getPosterSize() {
        Dimension cardSize = getSize();
        int posterWidth = 180;
        int posterHeight = cardSize.height;
        return new Dimension(posterWidth, posterHeight);
    }

    private JPanel getInformationPanel() {

        Movie movie = function.getMovie();
        Dimension cardSize = getSize();
        int informationWidth = cardSize.width - getPosterSize().width;
        int informationHeight = cardSize.height;

        JPanel information = new JPanel();
        Dimension informationSize = new Dimension(informationWidth, informationHeight);
        setSize(information, informationSize);
        information.setBackground(BACKGROUND_COLOR);
        information.setLayout(new BoxLayout(information, BoxLayout.Y_AXIS));

        JPanel title = getTitlePanel(movie.getName());
        JPanel description = getDescriptionPanel(movie.getDescription());
        JPanel schedule = getSchedulePanel(function.getSchedule());
        JPanel controls = getControls();

        information.add(title);
        information.add(description);
        information.add(schedule);
        information.add(controls);

        return information;
    }

    private JPanel getTitlePanel(String title) {
        JPanel panelTitle = new JPanel();
        panelTitle.setBackground(ITEM_BACKGROUND_COLOR.darker().darker());
        panelTitle.setForeground(INFORMATION_FONT_COLOR);
        JLabel movieTitle = new JLabel(title);
        movieTitle.setForeground(INFORMATION_FONT_COLOR);
        movieTitle.setFont(getCardTitleFont());
        panelTitle.add(movieTitle);
        return panelTitle;
    }

    private JPanel getDescriptionPanel(String description) {
        JPanel panelDescription = new JPanel();
        panelDescription.setBackground(ITEM_BACKGROUND_COLOR.darker().darker().darker());
        JLabel movieDescription = new JLabel("\"" + description + "\"");
        movieDescription.setForeground(INFORMATION_FONT_COLOR);
        panelDescription.add(movieDescription);
        return panelDescription;
    }

    private JPanel getSchedulePanel(String schedule) {
        JPanel panelSchedule = new JPanel();
        panelSchedule.setBackground(ITEM_BACKGROUND_COLOR.darker().darker().darker().darker());
        JLabel movieSchedule = new JLabel(schedule);
        movieSchedule.setForeground(INFORMATION_FONT_COLOR);
        panelSchedule.add(movieSchedule);
        return panelSchedule;
    }

    private JPanel getControls() {
        JPanel panelAction = new JPanel();
        panelAction.setLayout(new BoxLayout(panelAction, BoxLayout.X_AXIS));
        panelAction.setBackground(BACKGROUND_COLOR);
        JButton btn = new JButton("Comprar boletos");
        btn.setActionCommand(String.valueOf(function.getMovie().getId()));
        panelAction.add(Box.createHorizontalGlue());
        btn.setMargin(new Insets(5, 5, 5, 5));
        panelAction.setBorder(new EmptyBorder(new Insets(10, 0, 10, 10)));
        btn.addActionListener(selectionHandler);
        panelAction.add(btn);
        return panelAction;
    }
}
