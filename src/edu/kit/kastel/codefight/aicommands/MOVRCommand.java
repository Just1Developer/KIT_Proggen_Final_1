package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.Memory;

/**
 * The relative move command. Copies contents from one memory cell to another.
 *
 * @author uwwfh
 */
public class MOVRCommand implements AICommand {
    
    final int argumentA;
    final int argumentB;
    
    /**
     * Constructs a new relative move command.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    public MOVRCommand(int argA, int argB) {
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
    public void execute(Codefight ignored, AIPlayer player) {
        final int originPtr = Memory.sanitizeAddress(player.getMemoryPtr() + argumentA);
        final int destinationPtr = Memory.sanitizeAddress(player.getMemoryPtr() + argumentB);
        Memory memory = Codefight.getMemory();
        memory.cloneMemory(originPtr, destinationPtr, player.getPrintWrapper());
        
        player.moveByOne();
    }
}
