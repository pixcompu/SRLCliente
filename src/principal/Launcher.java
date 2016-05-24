package principal;

import java.rmi.RemoteException;
import javax.swing.UnsupportedLookAndFeelException;
import remote.RemoteConection;
import ui.FormCinemaFunctions;
import ui.util.Notifier;

/**
 *
 * @author PIX
 */
public class Launcher {

    public static void main(String[] args) {
        try {
            
            RemoteConection remoteConection = RemoteConection.getInstance();
            remoteConection.connect();
            setApplicationTheme("Nimbus");
            new FormCinemaFunctions().setVisible(true);
            
        } catch (RemoteException e) {
            Notifier.showMesage("Revisa tu conexion a la red, error : " + e.getMessage());
        } catch (Exception ex) {
            Notifier.showMesage("Error desconocido : " + ex.getMessage());
        }
    }

    private static void setApplicationTheme(String theme) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            if (theme.equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    }
}
