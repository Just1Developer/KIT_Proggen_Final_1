package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.model.GamePhase;

/**
 * This command displays help for the current GamePhase.
 *
 * @author uwwfh
 */
final class HelpCommand implements Command {
    
    private static final String COMMAND_DESCRIPTION = "help: Displays all in this phase available commands.";
    
    private static final String[] HELP_INITIALIZATION = {
            AddAICommand.getCommandDescription(),
            HelpCommand.getCommandDescription(),
            QuitCommand.getCommandDescription(),
            RemoveAICommand.getCommandDescription(),
            SetInitModeCommand.getCommandDescription(),
            StartCommand.getCommandDescription(),
    };
    private static final String[] HELP_INGAME = {
            EndCommand.getCommandDescription(),
            HelpCommand.getCommandDescription(),
            NextCommand.getCommandDescription(),
            QuitCommand.getCommandDescription(),
            ShowAICommand.getCommandDescription(),
            ShowMemoryCommand.getCommandDescription(),
    };
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        String[] allHelp = Main.getCurrentPhase() == GamePhase.INITIALIZATION ? HELP_INITIALIZATION : HELP_INGAME;
        for (String help : allHelp) {
            System.out.println(help);
        }
        return new CommandResult(CommandResultType.SUCCESS, null);
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return 0;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        return 0;
    }
    
    @Override
    public boolean isValidInGamePhase(GamePhase gamePhase) {
        return false;
    }
    
    /**
     * Gets the command description for this command.
     * @return The description of the command.
     */
    public static String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
