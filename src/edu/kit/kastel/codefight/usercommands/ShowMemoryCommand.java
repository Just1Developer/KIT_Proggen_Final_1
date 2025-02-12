package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.GamePhase;
import edu.kit.kastel.codefight.model.Memory;
import edu.kit.kastel.codefight.model.MemoryCell;

/**
 * This command prints the memory to the console. It also provides the option
 * to print a section of the memory in more detail as well.
 *
 * @author uwwfh
 */
final class ShowMemoryCommand implements Command {
    
    private static final int ARGUMENTS_REQUIRED = 0;
    private static final int ARGUMENTS_OPTIONAL = 1;
    
    private static final int DETAIL_SEGMENT_LENGTH = 10;
    private static final int STRING_START_INDEX = 0;
    private static final String COMMAND_DESCRIPTION = ("show-memory: Shows the entire memory and, if specified, a small section (%d cells)"
            + " in greater detail. Format: show-memory [address of begin detailed segment]").formatted(DETAIL_SEGMENT_LENGTH);
    private static final String DETAIL_ADDRESS_NAN = "The address to begin the detailed section must be a number.";
    private static final String ADDRESS_OUT_OF_BOUNDS = "The specified address is out of bounds of the memory.";
    private static final String CELL_DETAIL_FORMAT = "%s %s: %s | %s | %s";
    private static final String OUTPUT_FORMAT_DETAIL = "%s%n%s";
    private static final char SPACE = ' ';
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        if (commandArguments.length == 0) {
            return new CommandResult(CommandResultType.SUCCESS, Codefight.getMemory().toString());
        }
        
        // Insert Address edge sections and update memory string
        
        int startAddress;
        try {
            startAddress = Integer.parseInt(commandArguments[0]);
        } catch (NumberFormatException e) {
            return new CommandResult(CommandResultType.FAILURE, DETAIL_ADDRESS_NAN);
        }
        
        if (startAddress < 0 || startAddress >= Memory.getMemorySize()) {
            return new CommandResult(CommandResultType.FAILURE, ADDRESS_OUT_OF_BOUNDS);
        }
        
        final int actualSegmentLength = Math.min(DETAIL_SEGMENT_LENGTH, Memory.getMemorySize());
        
        StringBuilder detailBuilder = new StringBuilder();
        
        int longestAddress = 0;
        int longestCmdName = 0;
        int longestEntryColA = 0;
        int longestEntryColB = 0;
        
        // Get the in total longest string representations for width of columns.
        // Does not take the symbol (left most) column into account.
        // Also adds no padding for symbol length as that was not asked for.
        for (int ptr = 0; ptr < actualSegmentLength; ++ptr) {
            longestAddress = Math.max(longestAddress,
                    String.valueOf(Memory.sanitizeAddress(ptr + startAddress)).length());
            longestCmdName = Math.max(longestCmdName,
                    String.valueOf(Codefight.getMemory().readMemory(ptr + startAddress).getSavedCommandType()).length());
            longestEntryColA = Math.max(longestEntryColA,
                    String.valueOf(Codefight.getMemory().readMemory(ptr + startAddress).getArgumentA()).length());
            longestEntryColB = Math.max(longestEntryColB,
                    String.valueOf(Codefight.getMemory().readMemory(ptr + startAddress).getArgumentB()).length());
        }
        
        for (int ptr = 0; ptr < actualSegmentLength; ++ptr) {
            int address = Memory.sanitizeAddress(ptr + startAddress);
            detailBuilder.append(getDetailedCellCommand(Codefight.getMemory().getSingleCharacterRepresentation(address),
                    address, longestAddress, longestCmdName, longestEntryColA, longestEntryColB));
            if (ptr < actualSegmentLength - 1) {
                detailBuilder.append(System.lineSeparator());
            }
        }
        
        int endAddress = Memory.sanitizeAddress(startAddress + actualSegmentLength - 1);
        String memory = Codefight.getMemory().toString(startAddress, endAddress);
        
        return new CommandResult(CommandResultType.SUCCESS, OUTPUT_FORMAT_DETAIL.formatted(memory, detailBuilder));
    }
    
    /**
     * Gets the cell command as detailed string.
     * @param symbol The symbol representation.
     * @param sanitizedAddress The sanitized address.
     * @return The Cell and command as formatted String.
     */
    private String getDetailedCellCommand(
            String symbol,
            int sanitizedAddress,
            int longestAddress,
            int longestCmdName,
            int longestEntryColA,
            int longestEntryColB) {
        MemoryCell cell = Codefight.getMemory().readMemory(sanitizedAddress);
        return CELL_DETAIL_FORMAT.formatted(symbol,
                fillFront(sanitizedAddress, longestAddress),
                fillFront(cell.getSavedCommandType(), longestCmdName),
                fillFront(cell.getArgumentA(), longestEntryColA),
                fillFront(cell.getArgumentB(), longestEntryColB)
                );
    }
    
    /**
     * Fills the front of any Object's string representation with spaces
     * up until the desired target length is reached.
     * @param value The object. Converted to String using valueOf.
     * @param targetLength The target length.
     * @return The Object with leading spaces to achieve target length.
     */
    private String fillFront(Object value, int targetLength) {
        String strValue = String.valueOf(value);
        StringBuilder builder = new StringBuilder(strValue);
        for (int i = 0; i < targetLength - strValue.length(); ++i) {
            builder.insert(STRING_START_INDEX, SPACE);
        }
        return builder.toString();
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return ARGUMENTS_REQUIRED;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        return ARGUMENTS_OPTIONAL;
    }
    
    @Override
    public boolean isValidInGamePhase(GamePhase gamePhase) {
        return gamePhase == GamePhase.INGAME;
    }
    
    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
