package logic;

import java.io.Serializable;

/**
 *
 * @author PIX
 */
public class CinemaFunction implements Serializable{
    private int id;
    private  Room room;
    private  Movie movie;
    private  String schedule;

    public CinemaFunction(Room room, Movie movie, String schedule) {
        this.room = room;
        this.movie = movie;
        this.schedule = schedule;
    }

    public CinemaFunction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
