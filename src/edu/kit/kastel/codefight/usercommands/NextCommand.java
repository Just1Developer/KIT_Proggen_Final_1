package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.model.GamePhase;

/**
 * This command plays moves in the running Codefight game.
 *
 * @author Programmieren-Team
 */
final class NextCommand implements Command {
    
    private static final String COMMAND_DESCRIPTION = "next: Makes a given number of moves in the current codefight game."
            + "Format: next <steps>";
    private static final String STEPS_NOT_VALID_INT = "The steps must be specified as a non-negative integer.";
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        int steps = 1;
        if (commandArguments != null && commandArguments.length > 0) {
            try {
                steps = Integer.parseInt(commandArguments[0]);
            } catch (NumberFormatException e) {
                return new CommandResult(CommandResultType.FAILURE, STEPS_NOT_VALID_INT);
            }
            if (steps < 0) {
                return new CommandResult(CommandResultType.FAILURE, STEPS_NOT_VALID_INT);
            }
        }
        Main.next(steps);
        return new CommandResult(CommandResultType.SUCCESS, null);
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return 0;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        return 1;
    }
    
    @Override
    public boolean isValidInGamePhase(GamePhase gamePhase) {
        return gamePhase == GamePhase.INGAME;
    }
    
    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
