package remote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author PIX
 */
public class RemoteConection {

    private static final String MANAGER_OBJECT_NAME = "Movie";
    private static final String MANAGER_OBJECT_IP_ADDRESS = "127.0.0.1";
    private static IRemoteMovie remoteObject;
    private static RemoteConection instance;

    private RemoteConection() {
    }

    public static RemoteConection getInstance() {
        if (instance == null) {
            instance = new RemoteConection();
        }
        return instance;
    }

    public void connect() throws Exception {
        if (remoteObject == null) {
            try {
                Registry registry = LocateRegistry.getRegistry(MANAGER_OBJECT_IP_ADDRESS, 1098);

                for (String service : registry.list()) {
                    System.out.println("Service : " + service);
                }
                IRemoteMovie remoteObject = (IRemoteMovie) registry.lookup(MANAGER_OBJECT_NAME);

                boolean existsService = remoteObject != null;

                if (existsService) {
                    RemoteConection.remoteObject = remoteObject;
                } else {
                    throw new Exception("No service Found :( Sowwy");
                }

            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
                throw new Exception("No se pudo conectar al servicio");
            }
        }
    }

    public IRemoteMovie getRemoteObject() throws Exception {
        if (remoteObject != null) {
            return remoteObject;
        } else {
            throw new Exception("Try to connect first!");
        }
    }
}
