/*
 * Copyright (c) 2024, KASTEL. All rights reserved.
 */

package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.model.GamePhase;

/**
 * This interface represents an executable command.
 *
 * @author Programmieren-Team
 */
public interface Command {
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    CommandResult execute(String[] commandArguments);
    
    /**
     * Returns the number of required arguments that the command expects.
     *
     * @return the number of required arguments.
     */
    int getRequiredArgumentCount();
    
    /**
     * Returns the number of optional arguments that the command might expect in addition.
     *
     * @return the number of optional arguments.
     */
    int getOptionalArgumentCount();
    
    /**
     * Gets if the command is valid in the specified GamePhase.
     *
     * @param gamePhase The current phase of the game.
     * @return True if the command is valid in the specified Gamephase.
     */
    boolean isValidInGamePhase(GamePhase gamePhase);
    
    /**
     * Gets the command description for this command.
     * @return The description of the command.
     */
    String getCommandDescription();
}
