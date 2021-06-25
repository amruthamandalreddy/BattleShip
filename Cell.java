import java.io.Serializable;


/**
 * A single spot on the Battleship game board.
 * A cell knows if there is a ship on it, and it remember
 * if it has been hit.
 */
public class Cell implements Serializable {

    private int row;

    private int column;

    private boolean isHit;

    private Ship ship;

    /**
     * Character to display for a ship that has been entirely sunk
     */
    public static final char SUNK_SHIP_SECTION = '*';

    /**
     * Character to display for a ship that has been hit but not sunk
     */
    public static final char HIT_SHIP_SECTION = '☐';

    /**
     * Character to display for a water cell that has been hit
     */
    public static final char HIT_WATER = '.';

    /**
     * Character to display for a water cell that has not been hit.
     * This character is also used for an unhit ship segment.
     */
    public static final char PRISTINE_WATER = '_';

    /**
     * Character to display for a ship section that has not been
     * sunk, when revealing the hidden locations of ships
     */
    public static final char HIDDEN_SHIP_SECTION = 'S';

    public Cell(int row, int column) {
        this.row = row;
        this.column = column;
        this.isHit = false;
        this.ship = null;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Ship getShip(){
        return ship;
    }

    public boolean isHit() {
        return isHit;
    }

    /**
     * Place a ship on this cell. Of course, ships typically cover
     * more than one Cell, so the same ship will usually be passed
     * to more than one Cell's putShip method.
     *
     * @param ship the ship that is to be on this Cell
     * @throws OverlapException if there is already a ship here.
     */
    public void putShip(Ship ship) throws OverlapException {
        if (this.ship == null) {
            this.ship = ship;
        } else {
            throw new OverlapException(row,column);
        }
    }

    /**
     *  Simulate hitting this water cell. If there is a ship here, it will also be hit.
     *  Calling this method changes the status of the cell, as reflected by displayChar() and displayHitStatus().
     * @throws CellPlayedException
     */
    public void hit() throws  CellPlayedException{
        if(!isHit){
            this.isHit = true;
            if(this.ship!=null){
                this.ship.hit();
            }
        }
        else {
            throw new CellPlayedException(row,column);
        }
    }

    /**
     * This method returns the hit status of the cell
     * @return
     */
    public char displayHitStatus() {
        if (this.isHit == false) {
            return PRISTINE_WATER;
        } else if (this.ship != null && this.isHit == true) {
            if(this.ship.isSunk()){
                return SUNK_SHIP_SECTION;
            }
            else{
                return HIT_SHIP_SECTION;
            }

        }
        else {
            return HIT_WATER;
        }
    }

    /**
     * This method returns the status of the cell
     * @return
     */
    public char displayChar() {
        if(this.isHit == false){
            if(this.ship !=null){
                return HIDDEN_SHIP_SECTION;
            }
            else{
                return PRISTINE_WATER;
            }
        }
        else{
            if(this.ship!=null){
                if(this.ship.isSunk() == true){
                return SUNK_SHIP_SECTION;}
                else{
                    return HIT_SHIP_SECTION;
                }
            }
            else{
                return HIT_WATER;
            }
        }
    }

}
