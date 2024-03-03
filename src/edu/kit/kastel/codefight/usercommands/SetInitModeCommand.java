package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.model.GamePhase;
import edu.kit.kastel.codefight.model.Memory;
import edu.kit.kastel.codefight.model.MemoryInitType;

/**
 * This command sets the initialization mode of the memory to the specified argument.
 *
 * @author uwwfh
 */
final class SetInitModeCommand implements Command {
    
    
    private static final String COMMAND_DESCRIPTION =
            "set-init-mode: Sets the init mode that determines how remaining memory addresses are filled at the start of the game.";
    private static final int UPPER_BOUND = 1337;
    private static final int LOWER_BOUND = -1337;
    private static final String ERROR_RANDOM_REQUIRES_SEED =
            "The random init mode requires a seed between %d and %d".formatted(UPPER_BOUND, LOWER_BOUND);
    private static final String ERROR_SEED_OUT_OF_BOUNDS =
            "The specified seed must be between %d and %d".formatted(UPPER_BOUND, LOWER_BOUND);
    private static final String ERROR_SEED_NAN = "The seed must be a number between %d and %d".formatted(UPPER_BOUND, LOWER_BOUND);
    private static final String ERROR_INVALID_INIT_MODE = "Unknown init mode: %s, options are: INIT_MODE_RANDOM, INIT_MODE_STOP";
    private static final String SUCCESS_OUTPUT = "Changed init mode from %s to %s";
    private static final String INIT_MODE_RANDOM = "INIT_MODE_RANDOM";
    private static final String INIT_MODE_STOP = "INIT_MODE_STOP";
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        MemoryInitType type = parseInitMode(commandArguments[0]);
        if (type == null) {
            return new CommandResult(CommandResultType.FAILURE, ERROR_INVALID_INIT_MODE);
        }
        if (type == MemoryInitType.INIT_MODE_RANDOM && commandArguments.length == 1) {
            return new CommandResult(CommandResultType.FAILURE, ERROR_RANDOM_REQUIRES_SEED);
        }
        
        final long seed;
        if (type == MemoryInitType.INIT_MODE_RANDOM) {
            try {
                // Can be int because of bound restrictions
                seed = Integer.parseInt(commandArguments[1]);
            } catch (NumberFormatException e) {
                return new CommandResult(CommandResultType.FAILURE, ERROR_SEED_NAN);
            }
            if (seed < LOWER_BOUND || seed > UPPER_BOUND) {
                return new CommandResult(CommandResultType.FAILURE, ERROR_SEED_OUT_OF_BOUNDS);
            }
        } else {
            seed = 0;
        }
        
        String strOldInitMode = getInitModeString(Memory.getMemoryInitType(), Memory.getCellGenerationSeed());
        String strNewInitMode = getInitModeString(type, seed);
        
        Memory.setMemoryInitType(type, seed);
        
        return new CommandResult(CommandResultType.SUCCESS, SUCCESS_OUTPUT.formatted(strOldInitMode, strNewInitMode));
    }
    
    private String getInitModeString(MemoryInitType initType, long seed) {
        if (initType == MemoryInitType.INIT_MODE_RANDOM) {
            return "%s %d".formatted(initType, seed);
        }
        return initType.toString();
    }
    
    /**
     * Parses a string to a memory initialization mode.
     * If no mode matches, null is returned instead of throwing an error like valueOf.
     * @param argument The init mode string.
     * @return The Memory Init Type.
     */
    private MemoryInitType parseInitMode(String argument) {
        return switch (argument) {
            case INIT_MODE_RANDOM -> MemoryInitType.INIT_MODE_RANDOM;
            case INIT_MODE_STOP -> MemoryInitType.INIT_MODE_STOP;
            default -> null;
        };
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return 1;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        return 1;
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
