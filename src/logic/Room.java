package logic;

import java.io.Serializable;

/**
 *
 * @author PIX
 */
public class Room implements Serializable{
    private int id;
    private int rows;
    private int columns;
    private int[][] seats;

    public int getSeatsValue() {
        return seatsValue;
    }

    public void setSeatsValue(int seatsValue) {
        this.seatsValue = seatsValue;
    }

    private int seatsValue = 0;

    public Room(int idRoom,int rows, int columns) {
        this.id = idRoom;
        this.rows = rows;
        this.columns = columns;
        setUpSeats();
    }

    public Room() {
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int[][] getSeats() {
        return seats;
    }

    public void setSeats(int[][] seats) {
        this.seats = seats;
    }

    public void setUpSeats() {
        seats = new int[rows][columns];
        for(int[] row : seats){
            for(int i = 0; i<columns; i++){
                row[i] = SeatState.FREE;
            }
        }
    }
   
    
}
