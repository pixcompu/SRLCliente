package logic;

/**
 *
 * @author PIX
 */
public class Room {
    private final int rows;
    private final int columns;
    private int[][] seats;

    public Room(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        setUpSeats();
    }
    
    public int[][] getSeats() {
        return seats;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
    
    private void setUpSeats() {
        seats = new int[rows][columns];
        for(int[] row : seats){
            for(int i = 0; i<columns; i++){
                row[i] = SeatState.FREE;
            }
        }
    }
   
    
}
