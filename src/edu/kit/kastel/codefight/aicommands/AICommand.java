package edu.kit.kastel.codefight.aicommands;

import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;

/**
 * A base interface for all AI commands. The contents of
 * the command should be final.
 *
 * @author uwwfh
 */
public interface AICommand {
    
    /**
     * Gets the type of the command.
     * @return The command type.
     */
    AICommandType getType();
    
    /**
     * Gets the first argument (A).
     * @return The first arg value.
     */
    int getFirstArgument();
    
    /**
     * Gets the first argument (B).
     * @return The first arg value.
     */
    int getSecondArgument();
    
    /**
     * Executes the command.
     *
     * @param codefight     the model to execute the command on
     * @param player        the player to perform the command on
     */
    void execute(Codefight codefight, AIPlayer player);
}
