import java.io.Serializable;

/**
 * A single ship in a Battleship game
 */
public class Ship implements Serializable {

    private int length;

    private int numOfHits;

    public static final String SUNK_MESSAGE = "A battleship has been sunk!";

    public int getLength() {
        return length;
    }

    public int getNumOfHits() {
        return numOfHits;
    }

    /**
     * Orientation is a property of a ship.
     * The names of the enum values were chosen because they
     * are descriptive and match the words put in the configuration
     * files.
     *
     * @see Orientation#valueOf(String)
     */
    public enum Orientation {
        HORIZONTAL(0, 1), VERTICAL(1, 0);

        /**
         * Use it to loop through all of the cell locations a ship
         * is on, based on the upper left end of the ship.
         */
        public int rDelta, cDelta;

        /**
         * Associate a direction vector with the orientation.
         *
         * @param rDelta how much the row number changes in this direction
         * @param cDelta how much the column number changes
         *               in this direction
         */
        Orientation(int rDelta, int cDelta) {
            this.rDelta = rDelta;
            this.cDelta = cDelta;
        }
    }

    /**
     * Initialize this new ship's state. Tell the Board object
     * and each involved Cell object about the existence of this
     * ship by trying to put the ship at each applicable Cell.
     *
     * @param board  holds a collection of ships
     * @param uRow   the uppermost row that the ship is on
     * @param lCol   the leftmost column that the ship is on
     * @param ort    the ship's orientation
     * @param length how many cells the ship is on
     * @throws OverlapException     if this ship would overlap another one
     *                              that already exists
     * @throws OutOfBoundsException if this ship would extend beyond
     *                              the board
     */
    // Write your code here.
    public Ship(Board board, int uRow, int lCol, Orientation ort, int length) throws OverlapException, OutOfBoundsException {

        this.length = length;
        int x=0,y=0;

        Cell cell;
        for (int i = 0; i < length; i++) {
                x=i*ort.rDelta;
                y=i*ort.cDelta;
            cell = board.getCell(uRow +x, lCol +y);
            cell.putShip(this);
        }
        board.addShip(this);
    }

    private Ship(int length) {
        this.length = length;
    }

    /**
     * Hit the ship
     * Display Sunk Message if the ship is sunk
     */
    public void hit() {
        if (!isSunk()) {
            numOfHits++;
        }
        if(isSunk()){
        System.out.println(SUNK_MESSAGE);
        }
    }

    /**
     * Verify if the ship is sunk
     * @return
     */
    public boolean isSunk() {
        if (numOfHits == length) {
            return true;
        } else {
            return false;
        }
    }



}
