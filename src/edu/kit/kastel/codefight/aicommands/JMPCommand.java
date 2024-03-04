package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Memory;

/**
 * The jump command. Teleports the player to a different address.
 *
 * @author uwwfh
 */
public final class JMPCommand implements AICommand {
    
    private final int argumentA;
    private final int argumentB;
    
    /**
     * Constructs a new jump command.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    public JMPCommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.JMP;
    }
    
    @Override
    public int getFirstArgument() {
        return argumentA;
    }
    
    @Override
    public int getSecondArgument() {
        return argumentB;
    }
    
    @Override
    public void execute(AIPlayer player) {
        player.setMemoryPtr(Memory.sanitizeAddress(player.getMemoryPtr() + argumentA));
    }
}
