package logic;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author PIX
 */
public class CinemaManager implements Manager<CinemaFunction> {

    private static ArrayList<CinemaFunction> catalog;
    private static CinemaManager manager;

    private CinemaManager() {}
    
    public static CinemaManager getInstance(){
        if( manager == null ){
            manager = new CinemaManager();
            catalog = new ArrayList<>();
        }
        return manager;
    }
    
    @Override
    public void add(CinemaFunction newItem) {
        catalog.add(newItem);
    }

    @Override
    public void update(CinemaFunction updatedItem) {
        int itemIndex = getItemIndex(updatedItem.getMovie().getMovieID());
        catalog.set(itemIndex, updatedItem);
    }

    @Override
    public void remove(String itemID) {
        int itemIndex = getItemIndex(itemID);
        catalog.remove(itemIndex);
    }

    @Override
    public Iterator<CinemaFunction> getAll() {
        return catalog.iterator();
    }

    @Override
    public CinemaFunction getById(String itemID) {
        int itemIndex = getItemIndex(itemID);
        return catalog.get(itemIndex);
    }

    private int getItemIndex(String movieID) {
        for (int i = 0; i < catalog.size(); i++) {
            
            CinemaFunction current = catalog.get(i);
            
            if( current.getMovie().getMovieID().equals(movieID)){
                return i;
            }
        }
        return -1;
    }
    
     public void changeSeatState(String itemID, int row, int column, int newState){

         CinemaFunction function = getById(itemID);
         
         int rows = function.getRoom().getRows();
         int columns = function.getRoom().getColumns();
         
        boolean isValidLocation = 
                (row < rows) & (column < columns) & (row >= 0) & (column >= 0);
        
        if( isValidLocation ){
            function.getRoom().getSeats()[row][column] = newState;
        }else{
            System.out.println("Bastard!!!!");
        }
    }
}
