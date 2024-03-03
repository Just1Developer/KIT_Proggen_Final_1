package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;

/**
 * The stop command. Kills the AI that executes it.
 *
 * @author uwwfh
 */
public class StopCommand implements AICommand {
    
    final int argumentA;
    final int argumentB;
    
    /**
     * Constructs a new Stop command.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    public StopCommand(int argA, int argB) {
        this.argumentA = argA;
        this.argumentB = argB;
    }
    
    @Override
    public AICommandType getType() {
        return AICommandType.STOP;
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
        player.kill();
    }
}
