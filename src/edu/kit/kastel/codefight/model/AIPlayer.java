package edu.kit.kastel.codefight.model;

import edu.kit.kastel.codefight.aicommands.AICommand;
import edu.kit.kastel.codefight.aicommands.AICommandFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The AI player moving inside the memory.
 *
 * @author uwwfh
 */
public final class AIPlayer {
    
    private static final String AI_STRING_FORMAT_DEAD = "%s (STOPPED@%d)";
    private static final String AI_STRING_FORMAT_ALIVE = "%s (RUNNING@%d)%nNext Command: %s @%d";
    private static final String PERISH_MESSAGE = "%s executed %d steps until stopping.%n";
    private static final String DUPLICATE_NAME_FORMAT = "%s#%d";

    private String name;
    private final List<AICommand> instructions;
    private int memoryPtr;
    private int moveCounter;
    private boolean isDead;
    private AIPrintWrapper printWrapper;
    
    /**
     * Constructs a new AI Player with the specified name and instructions.
     * @param name The AI name.
     * @param commands The starting instructions
     */
    public AIPlayer(String name, List<AICommand> commands) {
        this.name = name;
        this.instructions = commands;
        this.printWrapper = null;
        reset();
    }
    
    /**
     * Constructs a new AI Player from a given Player, essentially creating a clone.
     * Clones the list of instructions by value.
     * @param player The player to clone.
     */
    public AIPlayer(AIPlayer player) {
        this.name = player.getAIName();
        this.instructions = player.getInstructions();
        this.printWrapper = player.getPrintWrapper();
        this.isDead = player.isDead;
        this.moveCounter = player.moveCounter;
        this.memoryPtr = player.memoryPtr;
    }
    
    /**
     * Sets the name of the AI to a duplicate format with the ID.
     *
     * @param id The ID of the AI, so the duplicate number.
     */
    public void setNameToDuplicate(int id) {
        this.name = DUPLICATE_NAME_FORMAT.formatted(this.name, id);
    }
    
    /**
     * Gets the PrintWrapper for this AI. May be null.
     * @return This AI's PrintWrapper.
     */
    public AIPrintWrapper getPrintWrapper() {
        return this.printWrapper;
    }
    
    /**
     * Sets the PrintWrapper for this AI. Used in show-memory command.
     * @param printWrapper The new PrintWrapper.
     */
    public void setPrintWrapper(AIPrintWrapper printWrapper) {
        this.printWrapper = printWrapper;
    }
    
    /**
     * Resets the player's move counter and alive status.
     */
    public void reset() {
        moveCounter = 0;
        memoryPtr = 0;
        isDead = false;
    }
    
    /**
     * If the AI is dead.
     * @return If the AI is dead.
     */
    public boolean isDead() {
        return this.isDead;
    }
    
    /**
     * Sets the AI to dead. Will stop increasing
     * the move counter when dead.
     * Also prints the current status of the AI.
     */
    public void kill() {
        this.isDead = true;
        System.out.printf(PERISH_MESSAGE.formatted(getAIName(), getMoveCount()));
    }
    
    /**
     * Gets the name of the AI.
     * @return The name of the AI.
     */
    public String getAIName() {
        return this.name;
    }
    
    /**
     * Gets the current location address of the AI in memory.
     * @return The address of the AI.
     */
    public int getMemoryPtr() {
        return memoryPtr;
    }
    
    /**
     * Sets the location address of the AI player.
     * Does not sanitize the address, though it is sanitized before acting on it.
     * @param ptr The new memory pointer.
     */
    public void setMemoryPtr(int ptr) {
        this.memoryPtr = Memory.sanitizeAddress(ptr);
    }
    
    /**
     * Moves 1 tile on the Memory, so increases the pointer by 1.
     */
    public void moveByOne() {
        setMemoryPtr(getMemoryPtr() + 1);
    }
    
    /**
     * Gets the amount of moves the AI has played in its current game.
     * @return The amount of moves so far.
     */
    public int getMoveCount() {
        return moveCounter;
    }
    
    /**
     * Increases the number of moves the AI as made by 1.
     * Will do nothing if the AI is dead.
     */
    public void increaseMoveCount() {
        this.moveCounter++;
    }
    
    /**
     * Gets a copy of the instructions of this AI.
     * @return A copy of the commands.
     */
    public List<AICommand> getInstructions() {
        List<AICommand> copy = new ArrayList<>();
        for (AICommand cmd : instructions) {
            copy.add(AICommandFactory.createCommand(cmd.getType(), cmd.getFirstArgument(), cmd.getSecondArgument()));
        }
        return copy;
    }
    
    /**
     * Generates a unique hashcode for the AI.
     * The current location in memory is not taking into account.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, instructions);
    }
    
    /**
     * Compares this AIPlayer object to another.
     * Returns true if the objects are the same.
     * @param obj The object to compare against
     * @return True if they are the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        // Compare exact class types, but use this to account for inherited types
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        return obj.hashCode() == this.hashCode();
    }
    
    @Override
    public String toString() {
        return isDead() ? AI_STRING_FORMAT_DEAD.formatted(getAIName(), getMoveCount())
                : AI_STRING_FORMAT_ALIVE.formatted(getAIName(), getMoveCount(),
                AICommandFactory.commandToString(Codefight.getMemory().readMemory(getMemoryPtr()).getCommand()), getMemoryPtr());
    }
}
