package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.Main;
import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.GamePhase;

/**
 * This command ends the Codefight game if running.
 *
 * @author uwwfh
 */
final class EndCommand implements Command {
    
    private static final int ARGUMENTS_REQUIRED = 0;
    private static final int ARGUMENTS_OPTIONAL = 0;
    
    private static final String COMMAND_DESCRIPTION = "end-game: Ends the game.";
    
    private static final String RUNNING_AI_BUILDER_PREFIX = "Running AIs:";
    private static final String STOPPED_AI_BUILDER_PREFIX = "Stopped AIs:";
    
    private static final char LIST_SEPARATOR = ',';
    private static final String LISTING_FORMAT = " %s";
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        StringBuilder runningAIs = new StringBuilder(RUNNING_AI_BUILDER_PREFIX);
        for (AIPlayer player : Main.getCodefight().getAliveAIs()) {
            if (runningAIs.length() > RUNNING_AI_BUILDER_PREFIX.length()) {
                runningAIs.append(LIST_SEPARATOR);
            }
            runningAIs.append(LISTING_FORMAT.formatted(player.getAIName()));
        }
        StringBuilder stoppedAIs = new StringBuilder(STOPPED_AI_BUILDER_PREFIX);
        for (AIPlayer player : Main.getCodefight().getTotalIngameAIs()) {
            if (player.isDead()) {
                if (stoppedAIs.length() > STOPPED_AI_BUILDER_PREFIX.length()) {
                    stoppedAIs.append(LIST_SEPARATOR);
                }
                stoppedAIs.append(LISTING_FORMAT.formatted(player.getAIName()));
            }
        }
        
        StringBuilder output = new StringBuilder();
        if (runningAIs.length() > RUNNING_AI_BUILDER_PREFIX.length()) {
            output.append(runningAIs);
        }
        if (stoppedAIs.length() > STOPPED_AI_BUILDER_PREFIX.length()) {
            if (!output.isEmpty()) {
                output.append(System.lineSeparator());
            }
            output.append(stoppedAIs);
        }
        
        Main.endGame();
        return new CommandResult(CommandResultType.SUCCESS, output.isEmpty() ? null : output.toString());
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
        return gamePhase == GamePhase.INGAME;
    }
    
    @Override
    public String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
