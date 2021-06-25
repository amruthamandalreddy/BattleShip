import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * The class to represent the grid of cells (squares).
 * A collection of ships is also kept so the Board
 * can be asked if the game is over.
 */
public class Board implements Serializable {

    private Cell[][] cells;

    private List<Ship> ships;

    public Board(int rows,int columns){
        this.cells = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
              this.cells[i][j] = new Cell(i,j);
            }
        }
        this.ships = new ArrayList<>();
    }

    /**
     * Fetch the Cell object at the given location.
     * @param row row number (0-based)
     * @param column column number (0-based)
     * @return the Cell created for this position on the board
     * @throws OutOfBoundsException if either coordinate is negative or too high
     */
    public Cell getCell(int row, int column) throws OutOfBoundsException {
        try{
        return cells[row][column];}
        catch (IndexOutOfBoundsException e){
            throw new OutOfBoundsException(row,column);
        }
    }

    public int getHeight(){
        return cells[0].length;
    }

    public int getWidth(){
        return cells.length;
    }

    public List<Ship> getShips() {
        return ships;
    }

    /**
     * Useful for debugging.
     * This is not the method that displays the board to the user.
     * @return
     */
    @Override
    public String toString() {
        return "Board{" +
                "cells=" + Arrays.toString(cells) +
                '}';
    }

    /**
     * Display the board in character form to the user.
     * Cells' display characters are described in Cell. Output is double-spaced in both dimensions.
     * The numbers of the columns appear above the first row,
     * and the numbers of each row appears to the left of the row.
     * @param printStream
     */
    public void display(PrintStream printStream){
        try {
            printStream.print('\n');
            printStream.print("  ");
            for(int k=0;k< getHeight();k++){
                printStream.print(k+" ");
            }
            printStream.write('\n');
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    if (j == 0) {
                        printStream.print(i+" ");
                    }
                    printStream.print(getCell(i, j).displayHitStatus());
                    printStream.print(' ');
                }
                printStream.print('\n');
            }
        }
        catch (OutOfBoundsException e){

        }
    }

    public void fullDisplay(PrintStream printStream){
        try {
            printStream.print('\n');
            printStream.print("  ");
            for(int k=0;k< getHeight();k++){
                printStream.print(k+" ");
            }
            printStream.write('\n');
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    if (j == 0) {
                        printStream.print(i+" ");
                    }
                    printStream.print(getCell(i, j).displayChar());
                    printStream.print(' ');
                }
                printStream.print('\n');
            }
        }
        catch (OutOfBoundsException e){

        }
    }


    /**
     * Add a ship to the board. The only current reason that the
     * board needs direct access to the ships is to poll them to see if they are all sunk and the game is over.
     * @see Cell#putShip(Ship)
     * @param ship the as-yet un-added ship
     * @rit.pre This ship has already informed the Cells of the board
     *    that are involved.
     */

    public  void addShip(Ship ship){
         this.ships.add(ship);
    }

    public boolean allSunk(){
        boolean allSunk = true;
        for (Ship ship:this.ships) {
            if(ship.isSunk()==false){
                allSunk = false;
                break;
            }
        }
        return allSunk;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(ships);
        result = 31 * result + Arrays.hashCode(cells);
        return result;
    }
}
