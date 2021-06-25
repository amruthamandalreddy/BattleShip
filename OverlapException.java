/**
 * A BattleshipException that informs the program that it attempted to place a ship where there is already another ship
 */
public class OverlapException extends BattleshipException {

    private static final String OVERLAP="Ships placed in overlapping positions";

    /**
     * The constructor stores the coordinates of intersection and sets the error message to OVERLAP.
     * @param row
     * @param column
     */
    public OverlapException(int row, int column) {
        super(row, column, OVERLAP);
    }
}
