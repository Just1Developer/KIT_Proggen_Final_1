package edu.kit.kastel.codefight.model;

import edu.kit.kastel.codefight.aicommands.AICommand;
import edu.kit.kastel.codefight.aicommands.AICommandFactory;
import edu.kit.kastel.codefight.aicommands.AICommandType;

/**
 * A single memory unit. Stores the command as enum and two
 * integer parameters.
 *
 * @author uwwfh
 */
public class MemoryCell {
    
    private final AICommandType commandType;
    private int argumentA;
    private int argumentB;
    private AIPrintWrapper lastModifiedPrinter;
    private boolean isUnmodified;
    
    /**
     * Creates a new and empty AI command.
     * The Command Type will be STOP, all arguments are 0.
     */
    public MemoryCell() {
        this.commandType = AICommandType.STOP;
        this.argumentA = 0;
        this.argumentB = 0;
        this.lastModifiedPrinter = null;
        isUnmodified = true;
    }
    
    /**
     * Creates a new AI command from the given arguments.
     * @param commandType The command type
     * @param argA The first argument
     * @param argB The second argument
     */
    public MemoryCell(AICommandType commandType, int argA, int argB) {
        this.commandType = commandType;
        this.argumentA = argA;
        this.argumentB = argB;
        this.lastModifiedPrinter = null;
        isUnmodified = true;
    }
    
    /**
     * Creates a new memory cell from a given AI command.
     * @param command The command to copy the values from.
     */
    public MemoryCell(AICommand command) {
        this.commandType = command.getType();
        this.argumentA = command.getFirstArgument();
        this.argumentB = command.getSecondArgument();
        this.lastModifiedPrinter = null;
        isUnmodified = true;
    }
    
    /**
     * Gets the printWrapper of the AI that last modified the Cell.
     * @return The cell's printWrapper.
     */
    public AIPrintWrapper getPrintWrapper() {
        return lastModifiedPrinter;
    }
    
    /**
     * Sets the print wrapper for printing to memory to the specified one.
     * @param printWrapper The printWrapper of the AI that modified the cell.
     */
    public void setLastModifiedBy(AIPrintWrapper printWrapper) {
        lastModifiedPrinter = printWrapper;
    }
    
    /**
     * Gets if the cell has been unmodified since the start of the game.
     * @return If the cell is unmodified since start.
     */
    boolean isUnmodified() {
        return this.isUnmodified;
    }
    
    /**
     * Sets the unmodified parameter of the cell to true or false.
     * @param unmodified If the cell is unmodified.
     */
    void setUnmodified(boolean unmodified) {
        this.isUnmodified = unmodified;
    }
    
    /**
     * Gets the saved command type.
     * @return The saved command type.
     */
    public AICommandType getSavedCommandType() {
        return commandType;
    }
    
    /**
     * Gets the first argument saved in the memory cell.
     * @return The first argument.
     */
    public int getArgumentA() {
        return argumentA;
    }
    
    /**
     * Sets the first argument saved in the memory cell.
     * @param value The new argument value.
     * @param printWrapper The printWrapper of the AI that modified the cell.
     */
    public void setArgumentA(int value, AIPrintWrapper printWrapper) {
        this.argumentA = value;
        this.lastModifiedPrinter = printWrapper;
    }
    
    /**
     * Gets the second argument saved in the memory cell.
     * @return The second argument.
     */
    public int getArgumentB() {
        return argumentB;
    }
    
    /**
     * Sets the second argument saved in the memory cell.
     * @param value The new argument value.
     * @param printWrapper The printWrapper of the AI that modified the cell.
     */
    public void setArgumentB(int value, AIPrintWrapper printWrapper) {
        this.argumentB = value;
        this.lastModifiedPrinter = printWrapper;
    }
    
    /**
     * Constructs a new command with the data from the memory cell.
     * @return A new AI command from the cell.
     */
    public AICommand getCommand() {
        return AICommandFactory.createCommand(getSavedCommandType(), getArgumentA(), getArgumentB());
    }
    
    /**
     * If the cell is holding a command that is considered a bomb.
     * To be considered bomb, a command must satisfy at last one of these conditions:
     * 1. Command is STOP
     * 2. Command is JUMP command with entry A=0
     * 3. Command is JMZ command with both entries 0
     * @return If the command is a bomb.
     */
    public boolean isBomb() {
        return !isUnmodified && (getSavedCommandType() == AICommandType.STOP
                || (getSavedCommandType() == AICommandType.JMP && getArgumentA() == 0)
                || (getSavedCommandType() == AICommandType.JMZ && getArgumentA() == 0 && getArgumentB() == 0));
    }
    
    /**
     * Generates an exact copy of the memory cell.
     * Copied values: Command details, lastModifiedBy, isUnmodified.
     * @return A copy of the cell.
     */
    public MemoryCell copy() {
        MemoryCell memorycell = new MemoryCell(this.commandType, this.argumentA, this.argumentB);
        memorycell.setLastModifiedBy(getPrintWrapper());
        memorycell.setUnmodified(isUnmodified);
        return memorycell;
    }
}
