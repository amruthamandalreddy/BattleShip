public class CellPlayedException extends BattleshipException {
    public static final String ALREADY_HIT=	"This cell has already been hit";

    public CellPlayedException(int row, int column) {
        super(row, column, ALREADY_HIT);
    }
}
