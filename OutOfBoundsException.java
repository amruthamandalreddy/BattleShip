/**
 * A BattleshipException that informs the program that it attempted to place a ship outside the bounds of the board
 */
public class OutOfBoundsException extends BattleshipException{

    private static final String  PAST_EDGE="Coordinates are past board edge";
    public OutOfBoundsException(int row, int column) {
        super(row, column, PAST_EDGE);
    }
}
