package edu.kit.kastel.codefight;

import edu.kit.kastel.codefight.model.AIPrintWrapper;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.GamePhase;
import edu.kit.kastel.codefight.model.MemoryInitType;
import edu.kit.kastel.codefight.usercommands.CommandHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the entry point of the program.
 *
 * @author uwwfh
 */
public final class Main {
    
    /**
     * Symbol Index for show-memory command.
     * The index of the character for an unmodified cell in the saved symbols.
     */
    public static final int INDEX_UNMODIFIED = 0;
    /**
     * Symbol Index for show-memory command.
     * The index of the character the symbol marking the edges of selected memory in the saved symbols.
     */
    public static final int INDEX_EDGE_SYMBOL = 1;
    /**
     * Symbol Index for show-memory command.
     * The index of the character representing the next AI command in the saved symbols.
     */
    public static final int INDEX_NEXT_AI_COMMAND = 2;
    /**
     * Symbol Index for show-memory command.
     * The index of the character representing any other current AI command in the saved symbols.
     */
    public static final int INDEX_ANY_AI_COMMAND = 3;
    
    /**
     * The minimum memory size.
     */
    public static final int MIN_MEMORY_SIZE = 7;
    /**
     * The maximum memory size.
     */
    public static final int MAX_MEMORY_SIZE = 1337;
    
    /**
     * The minimum args length. Consists of: Size (1), 4 Symbols (4) and at least 2 pairs of printing wrappers for AIs,
     * since every game needs at least 2 AIs (4+2n).
     */
    private static final int MIN_ARGS_LENGTH = 9;
    private static final int MEMORY_CHARS_SIZE = 4;
    
    private static final String INVALID_ARGUMENTS_MESSAGE = "Error: invalid command line arguments.";
    private static final String INVALID_PHASE_START = "Tried to initialize a game while ingame.";
    private static final String INVALID_PHASE_END = "Tried to end the game while not ingame.";
    private static final String INVALID_PHASE_INGAME = "Tried to play while not ingame.";
    private static final String GAME_STARTED_MSG = "Welcome to CodeFight 2024. Enter 'help' for more details.";
    
    private static GamePhase currentPhase;
    private static Codefight currentGame;
    
    private static String[] memoryChars;
    private static List<AIPrintWrapper> printWrappers;
    
    private Main() { }
    
    /**
     * Starts the program.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        currentPhase = GamePhase.INITIALIZATION;
        if (!parseArguments(args)) {
            System.err.println(INVALID_ARGUMENTS_MESSAGE);
            return;
        }
        System.out.println(GAME_STARTED_MSG);
        CommandHandler commandHandler = new CommandHandler();
        commandHandler.handleUserInput();
    }
    
    /**
     * Gets the current phase.
     * @return The current phase.
     */
    public static GamePhase getCurrentPhase() {
        return currentPhase;
    }
    
    /**
     * Gets the memory representation symbol based on the cell's priority level.
     * As some characters take up more than 16 bits, a character is represented using a String object.
     * <p></p>
     * If the priority is out of range, returns the default (lowest) priority character.
     *
     * @param priority The symbol priority
     * @return The character representing the cell.
     */
    public static String getMemorySymbol(int priority) {
        if (priority >= memoryChars.length || priority < 0) {
            return memoryChars[memoryChars.length - 1];
        }
        return memoryChars[priority];
    }
    
    /**
     * Gets a PrintWrapper from the ones initially specified by command line arguments.
     * If an invalid index is given, returns null wrapper.
     * @param index The index of the PrintWrapper.
     * @return The PrintWrapper of the given index.
     */
    public static AIPrintWrapper getPrintWrapper(int index) {
        if (index < 0 || index >= printWrappers.size()) {
            return AIPrintWrapper.NULL_WRAPPER;
        }
        return printWrappers.get(index);
    }
    
    /**
     * Gets the total amount of available PrintWrappers.
     * @return The amount of PrintWrappers.
     */
    public static int getPrintWrapperCount() {
        return printWrappers.size();
    }
    
    /**
     * Returns the current game of codefight or null, if no game is played.
     * @return Current Codefight or null.
     */
    public static Codefight getCodefight() {
        return currentGame;
    }
    
    /**
     * Enters the INGAME Phase and plays Codefight on the current model.
     * @throws IllegalStateException If a game is already on.
     * @param model The model to play the game on.
     */
    public static void playCodefight(Codefight model) {
        if (currentPhase != GamePhase.INITIALIZATION) {
            throw new IllegalStateException(INVALID_PHASE_START);
        }
        currentPhase = GamePhase.INGAME;
        currentGame = model;
    }
    
    /**
     * Plays a given number of moves in the current codefight game.
     * @throws IllegalStateException If there is no game to perform the steps on.
     * @param steps The number of steps.
     */
    public static void next(int steps) {
        if (currentGame == null) {
            throw new IllegalStateException(INVALID_PHASE_INGAME);
        }
        currentGame.next(steps);
    }
    
    /**
     * Ends the currently active Codefight game.
     * @throws IllegalStateException If there is no game to end.
     */
    public static void endGame() {
        if (currentPhase != GamePhase.INGAME) {
            throw new IllegalStateException(INVALID_PHASE_END);
        }
        assert currentGame != null;
        currentPhase = GamePhase.INITIALIZATION;
        currentGame = null;
    }
    
    /**
     * Parses the command line arguments and returns true if the arguments were parsed successfully.
     * @param args The command line arguments.
     * @return If parsing was successful.
     */
    private static boolean parseArguments(String[] args) {
        // Argument structure: size + 4 symbols + even arguments. Arguments must be uneven number
        if (args.length % 2 != 1 || args.length < MIN_ARGS_LENGTH) {
            return false;
        }
        
        // Setup Memory
        int memSize;
        try {
            memSize = Integer.parseInt(args[0]);
        } catch (NumberFormatException ignored) {
            return false;
        }
        if (memSize < MIN_MEMORY_SIZE || memSize > MAX_MEMORY_SIZE) {
            return false;
        }
        setupMemory(memSize);
        
        // Setup Memory Symbols
        memoryChars = new String[MEMORY_CHARS_SIZE];
        for (int i = 0; i < MEMORY_CHARS_SIZE; i++) {
            if (isNotSingleCharacter(args[i + 1])) {
                return false;
            }
            memoryChars[i] = args[i + 1];
        }
        
        // Setup Print Wrappers
        printWrappers = new ArrayList<>();
        for (int i = memoryChars.length + 1; i < args.length; i += 2) {
            if (isNotSingleCharacter(args[i]) || isNotSingleCharacter(args[i + 1])) {
                return false;
            }
            printWrappers.add(new AIPrintWrapper(args[i], args[i + 1]));
        }
        
        return true;
    }
    
    /**
     * Sets up the memory to its default parameters.
     * @param size The size of the memory.
     */
    private static void setupMemory(int size) {
        Codefight.initMemory(size, MemoryInitType.INIT_MODE_STOP, 0);
    }
    
    /**
     * Checks if a command line argument is a single unicode character.
     * Supports characters that go beyond the capacity of 1 char in Unicode.
     * @param argument The command line argument
     * @return True if the String consists of a single character.
     */
    private static boolean isNotSingleCharacter(String argument) {
        if (argument == null || argument.isEmpty()) {
            return true;
        }
        if (argument.length() == 1) {
            return false;
        }
        // Some characters take up 2 spaces but form a surrogate pair. In this case, it can still be one character
        return !(argument.length() == 2 && Character.isSurrogatePair(argument.charAt(0), argument.charAt(1)));
    }
}
