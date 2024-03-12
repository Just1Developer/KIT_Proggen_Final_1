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
final class SWAPCommand implements AICommand {
    
    private final int argumentA;
    private final int argumentB;
    
    /**
     * Constructs a new swap command. Constructed by the CommandFactory.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    SWAPCommand(int argA, int argB) {
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
    public void execute(AIPlayer player) {
        Memory memory = Codefight.getMemory();
        final MemoryCell firstCell = memory.readMemory(Memory.sanitizeAddress(player.getMemoryPtr() + argumentA));
        final MemoryCell secondCell = memory.readMemory(Memory.sanitizeAddress(player.getMemoryPtr() + argumentB));
        int temp = firstCell.getArgumentA();
        firstCell.setArgumentA(secondCell.getArgumentB(), player.getPrintWrapper());
        secondCell.setArgumentB(temp, player.getPrintWrapper());
        
        player.moveByOne();
    }
}
