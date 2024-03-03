/*
 * Copyright (c) 2023, KASTEL. All rights reserved.
 */

package edu.kit.kastel.codefight.usercommands;

/**
 * This enum represents the types that a result of a command can be.
 *
 * @author Programmieren-Team
 */
public enum CommandResultType {
    
    /**
     * The command was executed successfully.
     */
    SUCCESS,
    
    /**
     * An error occurred during processing the command.
     */
    FAILURE
}
