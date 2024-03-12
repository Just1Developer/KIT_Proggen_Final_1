package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.Memory;
import edu.kit.kastel.codefight.model.MemoryCell;

/**
 * The add command. Adds entries A and B together and stores the result in B.
 *
 * @author uwwfh
 */
final class ADDCommand implements AICommand {
    
    private final int argumentA;
    private final int argumentB;
    
    /**
     * Constructs a new add command. Constructed by the CommandFactory.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    ADDCommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.ADD;
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
        MemoryCell storage = memory.readMemory(player.getMemoryPtr());
        storage.setArgumentB(storage.getArgumentA() + storage.getArgumentB(), player.getPrintWrapper());
        player.moveByOne();
    }
}
