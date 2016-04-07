package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import logic.CinemaFunction;
import logic.Room;

/**
 *
 * @author PIX
 */
public interface IRemoteMovie extends Remote{
    public Room getRoom( int roomID ) throws RemoteException;
    public ArrayList<CinemaFunction> findAll() throws RemoteException;
    public void changeSeatState(int roomID, int row, int column, int newSeatState) throws RemoteException;
}
