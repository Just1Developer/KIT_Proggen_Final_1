package edu.kit.kastel.codefight.usercommands;

import edu.kit.kastel.codefight.aicommands.AICommand;
import edu.kit.kastel.codefight.aicommands.AICommandFactory;
import edu.kit.kastel.codefight.aicommands.AICommandType;
import edu.kit.kastel.codefight.model.AIPlayer;
import edu.kit.kastel.codefight.model.Codefight;
import edu.kit.kastel.codefight.model.GamePhase;
import edu.kit.kastel.codefight.model.Memory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This command adds an AI with instructions to the Codefight game quits a {@link CommandHandler command handler}.
 *
 * @author uwwfh
 */
final class AddAICommand implements Command {
    
    private static final String COMMAND_DESCRIPTION =
            "add-ai: Adds a new AI to the game. Format: add-ai <name> <commands: CMD,argA,argB,...>.";
    
    private static final String ERROR_NAME_CONTAINS_DISALLOWED_CHAR = "AI name must not contain a space";
    private static final String ERROR_NAME_TAKEN = "An AI with that name already exists";
    private static final String ERROR_INVALID_FORMAT = "The instructions are not in a valid format. Valid format is: "
            + "CMD,argA,argB,...(more instructions)";
    private static final String ERROR_PARSE_INSTRUCTIONS = "An error occurred while parsing instructions."
                    + " Unrecognized command pattern or arguments. Is there at least one non-STOP command?";
    private static final String TOO_MANY_INSTRUCTIONS =
            "Too many instructions, in no scenario would this AI not override another player's commands and cause an error";
    
    private static final int GROUP_INDEX_COMMAND = 0;
    private static final int GROUP_INDEX_TYPE = 1;
    private static final int GROUP_INDEX_ARG_A = 2;
    private static final int GROUP_INDEX_ARG_B = 3;
    
    /**
     * The regex for a single instruction. Used to parse instructions one at a time.
     * Regex breakdown:
     * ^ declares String start, so always only the first instruction is parsed
     * ([A-Z_]{3,5}) is the AI command. Validation via parsing to enum.
     * (-?\d+) are the two arguments, validity is confirmed when parsing to an integer. Optional negative sign.
     * ',?' The comma at the end is used to capture a comma, if there is one. The whole string not ending with a comma
     * is asserted before parsing.
     */
    private static final String REGEX_NEXT_INSTRUCTION = "^([A-Z_]{3,5}),(-?\\d+),(-?\\d+),?";
    private static final String DISALLOWED_CHAR_SEQ = " ";
    private static final String COMMA_CHAR = ",";
    
    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    @Override
    public CommandResult execute(String[] commandArguments) {
        
        String aiName = commandArguments[0];
        if (aiName.contains(DISALLOWED_CHAR_SEQ)) {
            return new CommandResult(CommandResultType.FAILURE, ERROR_NAME_CONTAINS_DISALLOWED_CHAR);
        }
        if (Codefight.containsAI(aiName)) {
            return new CommandResult(CommandResultType.FAILURE, ERROR_NAME_TAKEN);
        }
        if (commandArguments[1].endsWith(COMMA_CHAR)) {
            return new CommandResult(CommandResultType.FAILURE, ERROR_INVALID_FORMAT);
        }
        
        List<AICommand> instructions = parseInstructions(commandArguments[1]);
        if (instructions == null) {
            return new CommandResult(CommandResultType.FAILURE, ERROR_PARSE_INSTRUCTIONS);
        }
        if (instructions.size() > Math.ceil(Memory.getMemorySize() / 2.0)) {
            return new CommandResult(CommandResultType.FAILURE, TOO_MANY_INSTRUCTIONS);
        }
        
        Codefight.addAI(new AIPlayer(aiName, instructions));
        
        return new CommandResult(CommandResultType.SUCCESS, aiName);
    }
    
    /**
     * Parses instructions to an AI command list from a single string.
     * If instructions are invalid or any issues arise, returns null.
     * @param instructions The instructions.
     * @return List of commands or null.
     */
    private static List<AICommand> parseInstructions(final String instructions) {
        Pattern pattern = Pattern.compile(REGEX_NEXT_INSTRUCTION);
        Matcher matcher;
        List<AICommand> commands = new ArrayList<>();
        String remainingInstructions = instructions;
        boolean hasFoundNonStopCommand = false;
        
        while (!remainingInstructions.isEmpty()) {
            matcher = pattern.matcher(remainingInstructions);
            if (!matcher.find()) {
                return null;
            }
            // Parse instruction
            AICommandType type;
            int argA;
            int argB;
            
            type = AICommandType.valueOf(matcher.group(GROUP_INDEX_TYPE));
            try {
                argA = Integer.parseInt(matcher.group(GROUP_INDEX_ARG_A));
                argB = Integer.parseInt(matcher.group(GROUP_INDEX_ARG_B));
            } catch (NumberFormatException e) {
                // Invalid Command or integer parsing failed
                return null;
            }
            // Remove instruction
            remainingInstructions = remainingInstructions.substring(matcher.group(GROUP_INDEX_COMMAND).length());
            
            hasFoundNonStopCommand |= type != AICommandType.STOP;
            
            // Create command and add it
            commands.add(AICommandFactory.createCommand(type, argA, argB));
        }
        return hasFoundNonStopCommand ? commands : null;
    }
    
    @Override
    public int getRequiredArgumentCount() {
        return 2;
    }
    
    @Override
    public int getOptionalArgumentCount() {
        return 0;
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
