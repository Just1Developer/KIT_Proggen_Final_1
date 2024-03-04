package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.Memory;
import edu.kit.kastel.codefight.model.MemoryCell;

/**
 * The swap command. Swaps arguments A from the first relative cell with argument B from the second
 * relative cell.
 *
 * @author uwwfh
 */
public class SWAPCommand implements AICommand {
    
    final int argumentA;
    final int argumentB;
    
    /**
     * Constructs a new swap command.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    public SWAPCommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.SWAP;
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
        final MemoryCell firstCell = memory.readMemory(Memory.sanitizeAddress(player.getMemoryPtr() + argumentA));
        final MemoryCell secondCell = memory.readMemory(Memory.sanitizeAddress(player.getMemoryPtr() + argumentB));
        int temp = firstCell.getArgumentA();
        firstCell.setArgumentA(secondCell.getArgumentB(), player.getPrintWrapper());
        secondCell.setArgumentB(temp, player.getPrintWrapper());
        
        player.moveByOne();
    }
}
