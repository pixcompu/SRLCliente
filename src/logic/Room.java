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
    
    public void changeSeatState(int row, int column, int newState){
        boolean isValidLocation = 
                (row < rows) & (column < columns) & (row >= 0) & (column >= 0);
        
        if( isValidLocation ){
            seats[row][column] = newState;
        }else{
            System.out.println("Bastard!!!!");
        }
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
