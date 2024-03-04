package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.Memory;
import edu.kit.kastel.codefight.model.MemoryCell;

/**
 * The jump command. Teleports the player to a different address, but only if the value of
 * B of the relative cell defined by B is zero.
 *
 * @author uwwfh
 */
public final class JMZCommand implements AICommand {
    
    private static final int JUMP_CONDITION_VALUE = 0;
    
    final int argumentA;
    final int argumentB;
    
    /**
     * Constructs a new jump command.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    public JMZCommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.JMZ;
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
        Memory memory = Codefight.getMemory();
        MemoryCell checkCell = memory.readMemory(Memory.sanitizeAddress(player.getMemoryPtr() + argumentB));
        if (checkCell.getArgumentB() == JUMP_CONDITION_VALUE) {
            player.setMemoryPtr(Memory.sanitizeAddress(player.getMemoryPtr() + argumentA));
        } else {
            player.moveByOne();
        }
    }
}
