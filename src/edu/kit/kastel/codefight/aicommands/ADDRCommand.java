package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.Memory;
import edu.kit.kastel.codefight.model.MemoryCell;

/**
 * The add relative command. Adds entries A from the current cell and B from the target cell defined by
 * argument B together and stores the result in B from the target cell.
 *
 * @author uwwfh
 */
public class ADDRCommand implements AICommand {
    
    final int argumentA;
    final int argumentB;
    
    /**
     * Constructs a new relative add command.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    public ADDRCommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.ADD_R;
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
        Memory memory = Codefight.getMemory();
        MemoryCell target = memory.readMemory(player.getMemoryPtr() + argumentB);
        target.setArgumentB(argumentA + target.getArgumentB(), player.getPrintWrapper());
        
        player.moveByOne();
    }
}
