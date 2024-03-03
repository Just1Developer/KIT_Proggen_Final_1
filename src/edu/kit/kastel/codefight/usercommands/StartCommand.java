package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.GamePhase;

/**
 * This command starts a game with a given list of distinct AIs.
 *
 * @author uwwfh
 */
final class StartCommand implements Command {
    
    private static final String COMMAND_DESCRIPTION = "start-game: starts the game with a given list of distinct AIs."
            + "Format: start <ai1> <ai2> ...";
    
    private static final String UNKNOWN_AIS_OR_DUPLICATES = "All AIs must be registered to Codefight and distinct.";
    private static final String TOO_FEW_PRINTERS = "Not enough String representations for AIs have been defined.";
    private static final String SUCCESS_MESSAGE = "Game started.";
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        Codefight model = new Codefight(commandArguments);
        // Since invalid AIs and duplicates are not allowed, if any occur the numbers will not match
        if (model.getTotalPlayingAICount() != commandArguments.length) {
            return new CommandResult(CommandResultType.FAILURE, UNKNOWN_AIS_OR_DUPLICATES);
        }
        if (model.getTotalPlayingAICount() < Main.getPrintWrapperCount()) {
            return new CommandResult(CommandResultType.FAILURE, TOO_FEW_PRINTERS);
        }
        Main.playCodefight(model);
        return new CommandResult(CommandResultType.SUCCESS, SUCCESS_MESSAGE);
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return 2;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        // In theory, all AIs. So, get how many there are, and subtract the one required
        return Codefight.getAICount() - 2;
    }
    
    @Override
    public boolean isValidInGamePhase(GamePhase gamePhase) {
        return gamePhase == GamePhase.INITIALIZATION;
    }
    
    /**
     * Gets the command description for this command.
     * @return The description of the command.
     */
    public static String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
