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
    
    private static final String UNKNOWN_AIS_OR_DUPLICATES = "all AIs must be registered to Codefight.";
    private static final String TOO_FEW_PRINTERS = "not enough String representations for AIs have been defined.";
    private static final String SUCCESS_MESSAGE = "Game started.";
    private static final int MINIMUM_AIS = 2;
    
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
        if (model.getTotalPlayingAICount() > Main.getPrintWrapperCount()) {
            return new CommandResult(CommandResultType.FAILURE, TOO_FEW_PRINTERS);
        }
        if (!model.wasSetupSuccess()) {
            // Error was already printed. Mark as success as to not print an error.
            return new CommandResult(CommandResultType.SUCCESS, null);
        }
        Main.playCodefight(model);
        return new CommandResult(CommandResultType.SUCCESS, SUCCESS_MESSAGE);
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return MINIMUM_AIS;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        // How many more could be added
        return Main.getPrintWrapperCount() - MINIMUM_AIS;
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
