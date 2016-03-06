package logic;

/**
 *
 * @author PIX
 */
public class Movie {
    private final String movieID;
    private final String movieName;
    private final String movieDescription;

    public Movie(String movieID, String movieName, String movieDescription) {
        this.movieID = movieID;
        this.movieName = movieName;
        this.movieDescription = movieDescription;
    }

    public String getMovieID() {
        return movieID;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieDescription() {
        return movieDescription;
    }
    
}
