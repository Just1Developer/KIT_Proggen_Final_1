package edu.kit.kastel.codefight.aicommands;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that creates AI commands of specific types from enums. Used for parsing commands from enums to their
 * Command type.
 *
 * @author uwwfh
 */
public final class AICommandFactory {
    
    private static final String COMMAND_STRING_FORMAT = "%s|%d|%d";
    private static final Map<AICommandType, AICommandFactoryMethod> COMMAND_MAP = new HashMap<>();
    
    static {
        COMMAND_MAP.put(AICommandType.STOP, STOPCommand::new);
        COMMAND_MAP.put(AICommandType.MOV_R, MOVRCommand::new);
        COMMAND_MAP.put(AICommandType.MOV_I, MOVICommand::new);
        COMMAND_MAP.put(AICommandType.ADD, ADDCommand::new);
        COMMAND_MAP.put(AICommandType.ADD_R, ADDRCommand::new);
        COMMAND_MAP.put(AICommandType.JMP, JMPCommand::new);
        COMMAND_MAP.put(AICommandType.JMZ, JMZCommand::new);
        COMMAND_MAP.put(AICommandType.CMP, CMPCommand::new);
        COMMAND_MAP.put(AICommandType.SWAP, SWAPCommand::new);
    }
    
    private AICommandFactory() { }
    
    /**
     * Creates a command from it's enum type and arguments.
     * If, for some reason, no valid constructor is found, a default STOP command will be returned instead.
     * @param commandType The command type
     * @param argA The first argument of the command.
     * @param argB The second argument of the command.
     * @return An AI command matching that type.
     */
    public static AICommand createCommand(AICommandType commandType, int argA, int argB) {
        AICommandFactoryMethod factoryMethod = COMMAND_MAP.get(commandType);
        if (factoryMethod == null) {
            return new STOPCommand();
        }
        return factoryMethod.create(argA, argB);
    }
    
    /**
     * Converts any AI Command into it's String representation.
     * @param command The Command.
     * @return Command as String.
     */
    public static String commandToString(AICommand command) {
        return COMMAND_STRING_FORMAT.formatted(command.getType(), command.getFirstArgument(), command.getSecondArgument());
    }
    
    private interface AICommandFactoryMethod {
        AICommand create(int argA, int argB);
    }
}
