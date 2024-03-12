package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;

/**
 * The stop command. Kills the AI that executes it.
 *
 * @author uwwfh
 */
final class STOPCommand implements AICommand {
    
    private static final int DEFAULT_PARAMETER_VALUE = 0;
    
    private final int argumentA;
    private final int argumentB;
    
    /**
     * Constructs a new default Stop command. Both parameters will be 0.
     * Constructed by the CommandFactory.
     */
    STOPCommand() {
        this.argumentA = DEFAULT_PARAMETER_VALUE;
        this.argumentB = DEFAULT_PARAMETER_VALUE;
    }
    
    /**
     * Constructs a new Stop command. Constructed by the CommandFactory.
     * @param argA The first argument.
     * @param argB The second argument.
     */
    STOPCommand(int argA, int argB) {
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
    public void execute(AIPlayer player) {
        player.kill();
    }
}
