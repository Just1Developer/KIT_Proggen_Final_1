/*
 * Copyright (c) 2024, KASTEL. All rights reserved.
 */

package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.model.GamePhase;

/**
 * This command quits a {@link CommandHandler command handler}.
 *
 * @author Programmieren-Team
 * @author uwwfh
 */
final class QuitCommand implements Command {
    
    private static final String COMMAND_DESCRIPTION = "quit: Quits the application immediately.";
    
    /**
     * Executes the command.
     *
     * @param ignored the arguments of the command, ignored
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] ignored) {
        CommandHandler.quit();
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
