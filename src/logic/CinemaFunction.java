package logic;

/**
 *
 * @author PIX
 */
public class CinemaFunction {
    
    private final Room room;
    private final Movie movie;
    private final String schedule;

    public CinemaFunction(Room room, Movie movie, String schedule) {
        this.room = room;
        this.movie = movie;
        this.schedule = schedule;
    }

    public Room getRoom() {
        return room;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getSchedule() {
        return schedule;
    }
    
     public void printFunctionInformation(){
        System.out.println("Movie ID : " + movie.getMovieID());
        System.out.println("Movie Name : " + movie.getMovieName());
        System.out.println("Movie Description : " + movie.getMovieDescription());
        System.out.println("Function Schedule : " + schedule);
        System.out.println("Seats : ");
        for(int[] seatRow : room.getSeats()){
            for(int i = 0; i < seatRow.length; i++){
                System.out.print("[" + seatRow[i] + "] ");
            }
            System.out.println("");
        }
    }
}
