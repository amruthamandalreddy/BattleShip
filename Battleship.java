/**
 * This program is a simplified text-based version of the well-known game Battleship.
 *  @author      Amrutha Mandalreddy
 */

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.List;

public class Battleship implements Serializable {

    public static final String ALL_SHIPS_SUNK = "All ships sunk!";
    public static final String BAD_ARG_COUNT = "Wrong number of arguments for command";
    public static final String BAD_COMMAND = "Bad command";
    public static final String BAD_CONFIG_FILE = "Malformed board text file";
    public static final String DIM_TOO_BIG = "Board dimensions too big to display.";
    public static final int MAX_DIM = 20;
    public static final String MISSING_SETUP_FILE = "No setup file specified.";
    public static final String PROMPT = "> ";
    public static final String WHITESPACE = "\\s+";

    private Board board;

    private BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public Battleship() {
    }

    public Battleship(int rows, int columns) {
        this.board = new Board(rows, columns);
    }

    public Board getBoard() {
        return board;
    }

    /**
     * Setup board
     * @param lines
     * @return
     * @throws OverlapException
     * @throws OutOfBoundsException
     */
    public boolean setup(List<String> lines) throws OverlapException, OutOfBoundsException {
        try {
            for (String line : lines) {
                String[] words = line.split(WHITESPACE);
                if (words.length == 4) {
                    Ship.Orientation orientation;
                    int uRow, lCol, length;
                    uRow = Integer.parseInt(words[0]);
                    lCol = Integer.parseInt(words[1]);
                    length = Integer.parseInt(words[3]);
                    if (words[2].equalsIgnoreCase("HORIZONTAL")) {
                        orientation = Ship.Orientation.HORIZONTAL;
                    } else if (words[2].equalsIgnoreCase("VERTICAL")) {
                        orientation = Ship.Orientation.VERTICAL;
                    } else {
                        return false;
                    }
                    new Ship(board, uRow, lCol, orientation, length);
                } else {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Prompt player for input.
     * If all ships are sunk then exit the game
     */
    public void promptPlayer() {
        if (!board.allSunk()) {
            System.out.print(PROMPT);
        } else {
            System.out.println(ALL_SHIPS_SUNK);
            quit();
        }
    }

    /**
     * Play Battleship game
     */
    public void play() {
        boolean exit = false;
        String input = "";
        board.display(System.out);
        while (!exit) {
            promptPlayer();
            try {
                input = bufferedReader.readLine();
                String[] commands = input.split(WHITESPACE);
                if (commands.length > 0) {
                    if (commands[0].equalsIgnoreCase("q")) {
                        if (commands.length == 1) {
                            quit();
                        } else {
                            System.out.println(BAD_ARG_COUNT + " q");
                        }
                    } else if (commands[0].equalsIgnoreCase("!")) {
                        if (commands.length == 1) {
                            cheat();
                        } else {
                            System.out.println(BAD_ARG_COUNT + " !");
                        }
                    } else if (commands[0].equalsIgnoreCase("s")) {

                        if (commands.length == 2) {
                            save(commands[1]);
                        } else {
                            System.out.println(BAD_ARG_COUNT + " s");
                        }
                    } else if (commands[0].equalsIgnoreCase("h")) {
                        if (commands.length == 3) {
                            int rowNum = Integer.parseInt(commands[1]);
                            int colNum = Integer.parseInt(commands[2]);
                            hit(rowNum, colNum);
                            board.display(System.out);
                        } else {
                            System.out.println(BAD_ARG_COUNT + " h");
                        }
                    } else {
                        System.out.println(" Enter a valid command ");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                System.out.println(BAD_COMMAND);
            } catch (BattleshipException e) {
                System.out.println(e.getClass().getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Hit a cell on the board
     * @param row
     * @param col
     * @throws OutOfBoundsException
     * @throws CellPlayedException
     */
    public void hit(int row, int col) throws OutOfBoundsException, CellPlayedException {
        board.getCell(row, col).hit();
    }

    /**
     * Display all ship locations in the board
     */
    public void cheat() {
        board.fullDisplay(System.out);
    }

    /**
     * Check arguments count
     * @param args
     * @param num
     * @return
     */
    public static boolean checkArgCount(String[] args, int num) {
        if (args.length == 1) {
            return true;
        } else {
            return false;
        }
    }

    /***
     * Save game into a file
     * @param fileName
     * @return
     */
    public boolean save(String fileName) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);) {
            objectOutputStream.writeObject(board);
            return true;
        } catch (IOException e) {
            System.out.println("Error saving game progress in " + fileName);
            return false;
        }
    }

    /**
     * Quit the game
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Loading game from a save .bin file
     * @param fileName
     */

    public static Battleship readFromTxtFile(String fileName) throws IOException, OverlapException, OutOfBoundsException {
        try{
        Battleship battleship = null;
        List<String> lines = Files.readAllLines(Paths.get(fileName));
        String[] dimensions = lines.get(0).split(WHITESPACE);
        lines.remove(0);
        if (dimensions.length == 2) {
            int rows = Integer.parseInt(dimensions[0]);
            int columns = Integer.parseInt(dimensions[1]);
            if (rows <= MAX_DIM && columns <= MAX_DIM) {
                battleship = new Battleship(rows, columns);
                boolean isSetup= battleship.setup(lines);
                if(!isSetup){
                    System.out.println(BAD_CONFIG_FILE);
                    return null;
                }
            } else {
                System.out.println(DIM_TOO_BIG);
                return null;
            }

        } else {
            System.out.println(BAD_CONFIG_FILE);
            return null;
        }
        return battleship;}
        catch (RuntimeException e){
            System.out.println(BAD_CONFIG_FILE);
            return null;
        }
    }

    public void readFromBinFile(String fileName) {
        try (
                FileInputStream fileInputStream = new FileInputStream(fileName);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {
            board = (Board) objectInputStream.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println(MISSING_SETUP_FILE + " " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main class that runs the game
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) {
        if (checkArgCount(args, 1)) {
            System.out.print("Checking if " + args[0] + " is a saved game file...");
            try {
                if (args[0].endsWith(".bin")) {
                    System.out.println("yes");
                    Battleship battleship = new Battleship();
                    battleship.readFromBinFile(args[0]);
                    battleship.play();
                } else if (args[0].endsWith(".txt")) {
                    System.out.println("no; will read as a text setup file.");
                    Battleship battleship = Battleship.readFromTxtFile(args[0]);
                    if(battleship == null){
                        System.exit(0);
                    }
                    else {
                        battleship.play();
                    }

                } else {
                    System.out.println(BAD_CONFIG_FILE);
                }
            } catch (NoSuchFileException | FileNotFoundException e) {
                System.out.println(MISSING_SETUP_FILE + " " + args[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (BattleshipException e) {
                System.out.println(e.getMessage());
            }


        } else {
            System.out.println(MISSING_SETUP_FILE);
        }

    }
}
