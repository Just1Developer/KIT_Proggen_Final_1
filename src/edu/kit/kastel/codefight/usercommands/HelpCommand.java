package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.model.GamePhase;

import java.util.List;

/**
 * This command displays help for the current GamePhase.
 *
 * @author uwwfh
 */
final class HelpCommand implements Command {
    
    private static final String COMMAND_DESCRIPTION = "help: Displays all in this phase available commands.";
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        List<String> commandDescriptions = CommandHandler.getCommandDescriptionsAlphabetically(Main.getCurrentPhase());
        for (String help : commandDescriptions) {
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
        return true;
    }
    
    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
