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
    private final CommandHandler commandHandler;

    /**
     * Constructs a new QuitCommand.
     *
     * @param commandHandler the command handler to quit
     */
    QuitCommand(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }
    
    /**
     * Executes the command.
     *
     * @param ignored the arguments of the command, ignored
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] ignored) {
        commandHandler.quit();
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
    
    /**
     * Gets the command description for this command.
     * @return The description of the command.
     */
    public static String getCommandDescription() {
        return COMMAND_DESCRIPTION;
    }
}
