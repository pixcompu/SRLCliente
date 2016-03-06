package ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;
import javax.swing.JFrame;
import logic.Cinema;
import logic.CinemaFunction;
import logic.Movie;
import logic.Room;

/**
 *
 * @author PIX
 */
public class Principal {

    public static void main(String[] args) {
        Cinema cinepolis = getDummyCinema();
        cinepolis.printAllFunctionsInformation();
    }

    private static void changeSeatStates(Room room, int seatsNumber) {
        Random randomGen = new Random();
        for (int i = 0; i <= seatsNumber; i++) {
            int state = randomGen.nextInt(4);
            int row = randomGen.nextInt(room.getRows());
            int column = randomGen.nextInt(room.getColumns());
            room.changeSeatState(row, column, state);
        }
    }

    private static Cinema getDummyCinema() {
        Cinema cinema = new Cinema("Cinepolis");
        
        Movie avengers = new Movie("0", "Avengers The Movie", "A film about hulk and shit");
        Room roomOne = new Room(5, 10);
        CinemaFunction avengersFunction = new CinemaFunction(roomOne, avengers, "4:00PM - 6:00PM");
        
        Movie zootopia = new Movie("1", "Zootopia", "The sloth is awesome");
        Room roomTwo = new Room(5, 10);
        CinemaFunction zootopiaFunction = new CinemaFunction(roomTwo, zootopia, "4:00PM - 6:00PM");
        
        Movie deadpool = new Movie("2", "Deadpool", "Fucking deadpool");
        Room roomThree = new Room(5, 10);
        CinemaFunction deadpoolFunction = new CinemaFunction(roomThree, deadpool, "4:00PM - 6:00PM");
        
        Movie reenevant = new Movie("3", "The Reenevant", "Leo won a Oscar");
        Room roomFour = new Room(5, 10);
        CinemaFunction reenevantFunction = new CinemaFunction(roomFour, reenevant, "4:00PM - 6:00PM");
        
        cinema.addFunction(avengersFunction);
        cinema.addFunction(zootopiaFunction);
        cinema.addFunction(deadpoolFunction);
        cinema.addFunction(reenevantFunction);
        
        return cinema;
    }
}
