package edu.kit.kastel.codefight.model;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.aicommands.AICommand;
import edu.kit.kastel.codefight.aicommands.AICommandType;
import edu.kit.kastel.codefight.model.diagnostic.InvalidPointerException;
import edu.kit.kastel.codefight.model.diagnostic.OutOfMemoryException;
import edu.kit.kastel.codefight.usercommands.CommandHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * The entire memory / playing field of the game.
 * Generates new memory on the fly as soon as it's needed and saves it till the end.
 *
 * @author uwwfh
 */
public class Memory {
    
    private static final String INTEGER_FORMAT = "%d";
    private static final String ERROR_MAX_MEM = "%strying to allocate too much memory. Max size is %s."
            .formatted(CommandHandler.ERROR_PREFIX, INTEGER_FORMAT);
    private static final String ERROR_MIN_MEM = "%stoo little memory allocated. Minimum size is %s."
            .formatted(CommandHandler.ERROR_PREFIX, INTEGER_FORMAT);
    private static final String ERROR_INIT_OUT_OF_MEMORY = "%stoo little memory for too much instructions at setup!"
            .formatted(CommandHandler.ERROR_PREFIX);
    private static final String ERROR_AI_INSTRUCTION_OUT_OF_MEM = "%sAI is setting up too many instructions in too little space."
            .formatted(CommandHandler.ERROR_PREFIX);
    private static final String ERROR_INVALID_POINTER = "%sinvalid Memory Address: %s"
            .formatted(CommandHandler.ERROR_PREFIX, INTEGER_FORMAT);
    
    private static Random randomCellGenerator;
    private static long cellGenerationSeed;
    private static MemoryInitType memoryInitType;
    private static int memorySize;
    
    private final HashMap<Integer, MemoryCell> memory;
    
    /**
     * Creates a new memory object.
     */
    public Memory() {
        memory = new HashMap<>();
    }
    
    /**
     * Initializes memory size, init type and seed parameters.
     * @throws IllegalArgumentException If the specified memory size is smaller than the minimum required memory size
     *        or larger than the maximum memory size.
     * @param memorySize The size of the memory unit.
     * @param memoryInitType The initialization type of unpopulated memory cells.
     * @param seed The seed for the random generator. If init type is STOP_FILL, parameter will be ignored.
     */
    public static void initMemory(int memorySize, MemoryInitType memoryInitType, long seed) {
        if (memorySize > Main.MAX_MEMORY_SIZE) {
            throw new IllegalArgumentException(ERROR_MAX_MEM.formatted(Main.MAX_MEMORY_SIZE));
        }
        if (memorySize < Main.MIN_MEMORY_SIZE) {
            throw new IllegalArgumentException(ERROR_MIN_MEM.formatted(Main.MIN_MEMORY_SIZE));
        }
        setMemoryInitType(memoryInitType, seed);
        Memory.memorySize = memorySize;
    }

    /**
     * Gets the size of the memory.
     * @return The memory size.
     */
    public static int getMemorySize() {
        return memorySize;
    }
    
    /**
     * Gets the memory init type.
     * @return The memory init type.
     */
    public static MemoryInitType getMemoryInitType() {
        return memoryInitType;
    }
    
    /**
     * Gets the seed used for cell generation.
     * @return The cell generation seed.
     */
    public static long getCellGenerationSeed() {
        return cellGenerationSeed;
    }
    
    /**
     * Sets the memory init type and the seed for the random generator.
     * Setting the seed is only required if init mode is random.
     * @param memoryInitType The initialization type of unpopulated memory cells.
     * @param seed The seed for the random generator. If init type is STOP_FILL, parameter will be ignored.
     */
    public static void setMemoryInitType(MemoryInitType memoryInitType, long seed) {
        Memory.memoryInitType = memoryInitType;
        if (memoryInitType == MemoryInitType.INIT_MODE_RANDOM) {
            randomCellGenerator = new Random(seed);
            cellGenerationSeed = seed;
        }
    }

    /**
     * Gets the memory cell at a given address.
     * Address is modified to fit inside the bounds, overflow
     * is not possible.
     *
     * @throws InvalidPointerException If the pointer is not registered in the memory.
     * @param address The memory address.
     * @return The memory cell at that address.
     */
    public MemoryCell readMemory(final int address) {
        int sanitizedAddr = sanitizeAddress(address);
        if (memory.containsKey(sanitizedAddr)) {
            return memory.get(sanitizedAddr);
        }
        throw new InvalidPointerException(ERROR_INVALID_POINTER.formatted(address));
    }
    
    /**
     * Writes a given memory cell content to an address of the memory.
     * Address is modified to fit inside the bounds, overflow
     * is not possible.
     * Marks the cell as modified.
     *
     * @param address The address.
     * @param content The content to write.
     * @param lastModifiedWrapper The last modified property of the cell. Null represents 'unmodified'.
     */
    public void writeToMemory(final int address, final MemoryCell content, AIPrintWrapper lastModifiedWrapper) {
        writeToMemory(address, content, lastModifiedWrapper, false);
    }
    
    /**
     * Writes a given memory cell content to an address of the memory.
     * Address is modified to fit inside the bounds, overflow
     * is not possible.
     * If the address is already saved in memory and not specified otherwise,
     * marks the cell as modified.
     *
     * @param address The address.
     * @param content The content to write.
     * @param lastModifiedWrapper The last modified property of the cell. Null represents 'unmodified'.
     * @param isUnmodified If the cell should be marked as unmodified. If false, cell will be marked as modified.
     */
    private void writeToMemory(final int address, final MemoryCell content, AIPrintWrapper lastModifiedWrapper, boolean isUnmodified) {
        int sanitizedAddr = sanitizeAddress(address);
        if (memory.containsKey(sanitizedAddr)) {
            content.setUnmodified(isUnmodified);
        }
        content.setLastModifiedBy(lastModifiedWrapper);
        memory.put(sanitizedAddr, content);
    }
    
    /**
     * Writes a number of memory cells to the memory starting at a given address.
     * Starting address is modified to fit inside the bounds, overflow
     * is not possible.
     *
     * @param startingAddress     The address of the first cell to write.
     * @param contents            All the memory to write.
     * @param lastModifiedWrapper The last modified property of the cell. Null represents 'unmodified'.
     */
    private void populateMemory(final int startingAddress, final List<AICommand> contents, AIPrintWrapper lastModifiedWrapper) {
        int ptr = startingAddress;
        for (AICommand content : contents) {
            MemoryCell cell = new MemoryCell(content);
            writeToMemory(ptr, cell, lastModifiedWrapper, true);
            ptr++;
        }
    }
    
    /**
     * Copies a memory cell from one address to another and overrides the
     * contents that are stored at the target address.
     * Method will store a duplicate of the cell at the target address.
     * <p></p>
     * Will sanitize addresses to fit within memory bounds.
     *
     * @param addressFrom The origin address.
     * @param addressTo The target address.
     * @param lastModifiedWrapper The last modified property of the cell.
     */
    public void cloneMemory(final int addressFrom, final int addressTo, AIPrintWrapper lastModifiedWrapper) {
        writeToMemory(addressTo, readMemory(addressFrom).copy(), lastModifiedWrapper);
    }

    /**
     * Sanitizes an address, so crops it between the bounds of valid memory addresses.
     * This is done to ensure looping and wrap-around, meaning when reaching one end
     * of the memory space then looping around to the other end.
     *
     * @param address The address.
     * @return Sanitized (valid) address
     */
    public int sanitizeAddress(int address) {
        int sanitized = address % getMemorySize();
        if (sanitized < 0) {
            sanitized += getMemorySize();
        }
        return sanitized;
    }
    
    /**
     * Generates and populates a new MemoryCell based on the setting
     * on how to deal with generating memory.
     *
     * @return The new MemoryCell object, unassigned.
     */
    private MemoryCell generateNewMemoryContent() {
        if (memoryInitType == MemoryInitType.INIT_MODE_STOP) {
            return new MemoryCell();
        }
        AICommandType[] cmdTypes = AICommandType.values();
        AICommandType nextType = cmdTypes[randomCellGenerator.nextInt(cmdTypes.length)];
        int argA = randomCellGenerator.nextInt();
        int argB = randomCellGenerator.nextInt();
        return new MemoryCell(nextType, argA, argB);
    }
    
    /**
     * Resets the Memory and populates memory using the player's initial data.
     * Populates the remaining spaces according to the memory fill type.
     * <p></p>
     * Also resets the cell generation Random object.
     * Returns true if the reset failed.
     *
     * @throws OutOfMemoryException If the AIs try to override already populated memory at setup.
     * @param players The players that will be playing this round.
     * @return If the reset was successful.
     */
    boolean reset(List<AIPlayer> players) {
        populateEntireMemory();
        final int spacing = memorySize / players.size();
        int currentPtr = 0;
        // First, assert space validity
        for (int i = 0; i < players.size(); i++) {
            AIPlayer player = players.get(i);
            // Only take spacing like this into account if it's not the last player
            if (spacing < player.getInstructions().size() && i < players.size() - 1) {
                System.err.println(ERROR_AI_INSTRUCTION_OUT_OF_MEM);
                return false;
            }
            if (currentPtr + player.getInstructions().size() > memorySize) {
                System.err.println(ERROR_INIT_OUT_OF_MEMORY);
                return false;
            }
            currentPtr += spacing;
        }
        
        currentPtr = 0;
        for (AIPlayer player : players) {
            populateMemory(currentPtr, player.getInstructions(), player.getPrintWrapper());
            int ptr = currentPtr;
            // Find the first non-STOP command. There has to be one.
            while (readMemory(ptr).getSavedCommandType() == AICommandType.STOP) {
                ptr++;
            }
            player.setMemoryPtr(ptr);
            currentPtr += spacing;
        }
        return true;
    }
    
    /**
     * Populates the entire memory with brand-new cells.
     */
    private void populateEntireMemory() {
        for (int ptr = 0; ptr < memorySize; ++ptr) {
            writeToMemory(ptr, generateNewMemoryContent(), null, true);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder memoryBuilder = new StringBuilder();
        for (int ptr = 0; ptr < memorySize; ptr++) {
            assert memory.containsKey(ptr);
            memoryBuilder.append(getSingleCharacterRepresentation(ptr));
        }
        return memoryBuilder.toString();
    }
    
    /**
     * Converts the Memory to its string representation by the rules given by the task.
     * Also inserts edge markers at the specified start and end addresses.
     * @param startAddressMarker The sanitized address of the marker beginning.
     * @param endAddressMarker The sanitized address of the marker ending.
     * @return The Memory object as String with markers
     */
    public String toString(int startAddressMarker, int endAddressMarker) {
        StringBuilder memoryBuilder = new StringBuilder();
        for (int ptr = 0; ptr < memorySize; ptr++) {
            assert memory.containsKey(ptr);
            if (ptr == startAddressMarker || ptr == endAddressMarker) {
                memoryBuilder.append(Main.getMemorySymbol(Main.INDEX_EDGE_SYMBOL));
            }
            memoryBuilder.append(getSingleCharacterRepresentation(ptr));
        }
        return memoryBuilder.toString();
    }
    
    /**
     * Gets the single character representation for a memory cell at a given address.
     * @param cellAddress The sanitized cell address.
     * @return Single character representation.
     */
    public String getSingleCharacterRepresentation(int cellAddress) {
        if (Main.getCodefight().getNextAIAddress() == cellAddress) {
            return Main.getMemorySymbol(Main.INDEX_NEXT_AI_COMMAND);
        }
        // Check if address is any other's next address.
        for (AIPlayer player : Main.getCodefight().getAliveAIs()) {
            if (player.getMemoryPtr() == cellAddress) {
                return Main.getMemorySymbol(Main.INDEX_ANY_AI_COMMAND);
            }
        }
        
        MemoryCell cell = readMemory(cellAddress);
        if (cell.getPrintWrapper() == null || cell.getPrintWrapper().equals(AIPrintWrapper.NULL_WRAPPER)) {
            return Main.getMemorySymbol(Main.INDEX_UNMODIFIED);
        }
        
        // Presence is asserted via isUnmodified call
        AIPrintWrapper printWrapper = cell.getPrintWrapper();
        return cell.isBomb() ? printWrapper.bombSymbol() : printWrapper.defaultSymbol();
    }

}
