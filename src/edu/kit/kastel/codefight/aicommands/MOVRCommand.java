package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.Memory;

/**
 * The relative move command. Copies contents from one memory cell to another.
 *
 * @author uwwfh
 */
final class MOVRCommand implements AICommand {
    
    private final int argumentA;
    private final int argumentB;
    
    /**
     * Constructs a new relative move command. Constructed by the CommandFactory.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    MOVRCommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.MOV_R;
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
        final int originPtr = Memory.sanitizeAddress(player.getMemoryPtr() + argumentA);
        final int destinationPtr = Memory.sanitizeAddress(player.getMemoryPtr() + argumentB);
        Memory memory = Codefight.getMemory();
        memory.cloneMemory(originPtr, destinationPtr, player.getPrintWrapper());
        
        player.moveByOne();
    }
}
