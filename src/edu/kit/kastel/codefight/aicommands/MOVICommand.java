package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.Memory;

/**
 * The intermediate move command. Copies contents from one memory cell to another but treats
 * the target memory cells' arguments as pointers.
 *
 * @author uwwfh
 */
public class MOVICommand implements AICommand {
    
    final int argumentA;
    final int argumentB;
    
    /**
     * Constructs a new intermediate move command.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    public MOVICommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.MOV_I;
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
        final int originPtr = player.getMemoryPtr() + argumentA;
        int destinationPtr = player.getMemoryPtr() + argumentB;
        Memory memory = Codefight.getMemory();
        destinationPtr += memory.readMemory(destinationPtr).getArgumentB();
        memory.cloneMemory(originPtr, destinationPtr, player.getPrintWrapper());
        
        player.moveByOne();
    }
}
