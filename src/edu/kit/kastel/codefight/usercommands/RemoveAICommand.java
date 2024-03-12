package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.GamePhase;

/**
 * This command removes an {@link edu.kit.kastel.codefight.model.AIPlayer AI Player} from available AIs.
 * This means the AI cannot be used when starting games in the future.
 *
 * @author uwwfh
 */
final class RemoveAICommand implements Command {
    
    private static final int ARGUMENTS_REQUIRED = 1;
    private static final int ARGUMENTS_OPTIONAL = 0;
    
    private static final String COMMAND_DESCRIPTION = "remove-ai: Removes an existing AI from Codefight. Format: remove-ai <name>";
    private static final String ERROR_UNKNOWN_NAME = "Could not find AI with name %s.";
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        boolean success = Codefight.removeAI(commandArguments[0]);
        if (success) {
            return new CommandResult(CommandResultType.SUCCESS, commandArguments[0]);
        }
        return new CommandResult(CommandResultType.FAILURE, ERROR_UNKNOWN_NAME.formatted(commandArguments[0]));
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return ARGUMENTS_REQUIRED;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        return ARGUMENTS_OPTIONAL;
    }
    
    @Override
    public boolean isValidInGamePhase(GamePhase gamePhase) {
        return gamePhase == GamePhase.INITIALIZATION;
    }
    
    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
