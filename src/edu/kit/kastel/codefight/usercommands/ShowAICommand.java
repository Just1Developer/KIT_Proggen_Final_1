package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.GamePhase;

import java.util.Optional;

/**
 * This command print an AI's current state to the console.
 *
 * @author uwwfh
 */
final class ShowAICommand implements Command {
    
    private static final String COMMAND_DESCRIPTION = "show-ai: Shows the current state of an AI. Format: show-ai <name>";
    private static final String ERROR_UNKNOWN_NAME = "Could not find AI with name %s.";
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        Optional<AIPlayer> player = Main.getCodefight().getPlayingAIbyName(commandArguments[0]);
        return player.map(aiPlayer -> new CommandResult(CommandResultType.SUCCESS, aiPlayer.toString())).orElseGet(() ->
                new CommandResult(CommandResultType.FAILURE, ERROR_UNKNOWN_NAME.formatted(commandArguments[0])));
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return 1;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        return 0;
    }
    
    @Override
    public boolean isValidInGamePhase(GamePhase gamePhase) {
        return gamePhase == GamePhase.INGAME;
    }
    
    /**
     * Gets the command description for this command.
     * @return The description of the command.
     */
    public static String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
