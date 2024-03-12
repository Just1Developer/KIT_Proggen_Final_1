package edu.kit.kastel.codefight;

import edu.kit.kastel.codefight.model.AIPrintWrapper;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.GamePhase;
import edu.kit.kastel.codefight.model.MemoryInitType;
import edu.kit.kastel.codefight.usercommands.CommandHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private static final int ARGS_INDEX_MEM_SIZE = 0;
    private static final int ARGS_INDEX_BEGIN_MEM_CHARS = 1;
    private static final int ARGS_INDEX_BEGIN_PLAYER_SYMBOLS = MEMORY_CHARS_SIZE + ARGS_INDEX_BEGIN_MEM_CHARS;
    private static final int ARGS_PLAYER_SYMBOL_OTHER = 1;
    private static final int PLAYER_SYMBOL_AMOUNT = 2;
    private static final int INIT_MODE_STOP_SEED = 0;
    
    private static final String INVALID_ARGUMENTS_MESSAGE = "%sinvalid command line arguments.".formatted(CommandHandler.ERROR_PREFIX);
    private static final String ARGUMENTS_NOT_UNIQUE = "%sall character arguments must be unique!".formatted(CommandHandler.ERROR_PREFIX);
    private static final String INVALID_PHASE_START = "%stried to initialize a game while ingame.".formatted(CommandHandler.ERROR_PREFIX);
    private static final String INVALID_PHASE_END = "%stried to end the game while not ingame.".formatted(CommandHandler.ERROR_PREFIX);
    private static final String INVALID_PHASE_INGAME = "%stried to play while not ingame.".formatted(CommandHandler.ERROR_PREFIX);
    private static final String GAME_STARTED_MSG = "Welcome to CodeFight 2024. Enter 'help' for more details.";
    
    /**
     * The single character that is invalid for symbols and must not appear in the symbols.
     */
    private static final String INVALID_SYMBOL_CHAR = " ";
    
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
        Optional<String> argumentResult = parseArguments(args);
        if (argumentResult.isPresent()) {
            System.err.println(argumentResult.get());
            return;
        }
        System.out.println(GAME_STARTED_MSG);
        CommandHandler.initialize();
        CommandHandler.handleUserInput();
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
     * Parses the command line arguments and returns the occurred error if the arguments were parsed unsuccessfully.
     * @param args The command line arguments.
     * @return The error message to print. Null if there was no error.
     */
    private static Optional<String> parseArguments(String[] args) {
        Set<String> knownCharacters = new HashSet<>();
        // Argument structure: size + 4 symbols + even arguments. Arguments must be uneven number
        if (args.length % 2 != 1 || args.length < MIN_ARGS_LENGTH) {
            return Optional.of(INVALID_ARGUMENTS_MESSAGE);
        }
        
        // Set up Memory
        int memSize;
        try {
            memSize = Integer.parseInt(args[ARGS_INDEX_MEM_SIZE]);
        } catch (NumberFormatException ignored) {
            return Optional.of(INVALID_ARGUMENTS_MESSAGE);
        }
        if (memSize < MIN_MEMORY_SIZE || memSize > MAX_MEMORY_SIZE) {
            return Optional.of(INVALID_ARGUMENTS_MESSAGE);
        }
        setupMemory(memSize);
        
        // Set up Memory Symbols
        memoryChars = new String[MEMORY_CHARS_SIZE];
        for (int i = ARGS_INDEX_BEGIN_MEM_CHARS; i < MEMORY_CHARS_SIZE + ARGS_INDEX_BEGIN_MEM_CHARS; i++) {
            // Check validity
            if (args[i].contains(INVALID_SYMBOL_CHAR)) {
                return Optional.of(INVALID_ARGUMENTS_MESSAGE);
            }
            // Check uniqueness
            if (!knownCharacters.add(args[i])) {
                return Optional.of(ARGUMENTS_NOT_UNIQUE);
            }
            // Counter addition of begin index
            memoryChars[i - ARGS_INDEX_BEGIN_MEM_CHARS] = args[i];
        }
        
        // Setup Print Wrappers
        printWrappers = new ArrayList<>();
        for (int i = ARGS_INDEX_BEGIN_PLAYER_SYMBOLS; i < args.length; i += PLAYER_SYMBOL_AMOUNT) {
            // Check validity
            if (args[i].contains(INVALID_SYMBOL_CHAR) || args[i + ARGS_PLAYER_SYMBOL_OTHER].contains(INVALID_SYMBOL_CHAR)) {
                return Optional.of(INVALID_ARGUMENTS_MESSAGE);
            }
            // Check uniqueness
            if (!knownCharacters.add(args[i]) || !knownCharacters.add(args[i + ARGS_PLAYER_SYMBOL_OTHER])) {
                return Optional.of(ARGUMENTS_NOT_UNIQUE);
            }
            printWrappers.add(new AIPrintWrapper(args[i], args[i + ARGS_PLAYER_SYMBOL_OTHER]));
        }
        
        return Optional.empty();
    }
    
    /**
     * Sets up the memory to its default parameters.
     * @param size The size of the memory.
     */
    private static void setupMemory(int size) {
        Codefight.initMemory(size, MemoryInitType.INIT_MODE_STOP, INIT_MODE_STOP_SEED);
    }
}
