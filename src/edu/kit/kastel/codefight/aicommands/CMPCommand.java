package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.Memory;

/**
 * The compare command. Compares values A and B from relative cells. If they are equal,
 * the next AI's turn will be skipped.
 *
 * @author uwwfh
 */
public final class CMPCommand implements AICommand {
    
    private final int argumentA;
    private final int argumentB;
    
    /**
     * Constructs a new compare command.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    public CMPCommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.CMP;
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
        final int firstArgPtr = player.getMemoryPtr() + argumentA;
        final int secondArgPtr = player.getMemoryPtr() + argumentB;
        Memory memory = Codefight.getMemory();
        if (memory.readMemory(firstArgPtr).getArgumentA() != memory.readMemory(secondArgPtr).getArgumentB()) {
            player.moveByOne();
        }
        player.moveByOne();
    }
}
